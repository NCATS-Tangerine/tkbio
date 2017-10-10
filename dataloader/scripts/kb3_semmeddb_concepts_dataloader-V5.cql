// @author Richard Bruskiewich (minor modifications)
//
// This Cypher Query Language (CQL) Script is for 
// loading a file of SemMedDb concept descriptions
//
// November 30th, 2016 to parse in the generated 
// SemMedDb Concept specification file
//
// Input data is in a tab delimited table file, one statement per row
//
// Columns: 
//
//		qid		semantic_groups		preflabel	synonyms	definition
//
// Where qid is the object id of a WikiData entity
//
//
// The script uses the CSV loader functionality of Neo4j.
//
// See the DATALOADING_R2.3_README.md for 
// more details on how to run the script.
//

//****** WikiData Entities ******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity{name : "WikiData Entity"})
ON CREATE SET database.name = "WikiData Entity",
    database.description = "WikiData Entities",
    database.url = "http://www.wikidata.org/entity/",
    database.nameSpacePrefix = "wd",
    database.accessionId = "wd:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
;

//******Concept Loader*****
CREATE CONSTRAINT ON (concept:Concept) ASSERT concept.uri IS UNIQUE;

// Create Index for making initial search faster (need to review)
CREATE INDEX ON : Concept(name);
CREATE INDEX ON : Concept(usage);
CREATE INDEX ON : Concept(type);

USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/concepts.tsv" AS row FIELDTERMINATOR '\t'
WITH 
	row AS row, 
	"http://www.wikidata.org/entity/"+row.qid AS concept_uri,
	CASE  
		WHEN length(trim(row.semantic_groups)) = 0 THEN "OBJC"
		
		// if more than one semantic_groups specified, pick first one only for now
		WHEN size(split(row.semantic_groups,"|"))>1 THEN head(split(row.semantic_groups,"|")) 
		
		ELSE row.semantic_groups
	END AS concept_type
WHERE NOT concept_uri IS NULL
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { uri : concept_uri } )
ON CREATE SET 
    concept.uri = concept_uri,
    concept.accessionId = "wd:"+row.qid,
    concept.type = concept_type,
    concept.name = row.preflabel,
    concept.description = row.definition,
    concept.synonyms = row.synonyms,
    concept.usage = TOINT('0'),
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
MERGE (concept)-[:LIBRARY]->(library:Library)
ON CREATE SET 
    library.numberOfArchivedMaps = TOINT('0'),
    library.name = concept.name +"_Library",
    library.description = "Library Of "+ concept.name,
    library.accessionId = concept.accessionId,
    library.versionDate = timestamp(),
    library.version = TOINT('1')
;
