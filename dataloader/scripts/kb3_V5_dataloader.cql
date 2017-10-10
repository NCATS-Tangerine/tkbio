// @author Richard Bruskiewich (minor modifications)
//
// This Cypher Query Language (CQL) Script is for 
// loading a file of WikiData indexed SemMedDb concept and statement data
//
// November 30th, 2016 to parse in the generated 
// SemMedDb Concept specification file
//
// Input data is in a tab delimited table file, one statement per row
//
//
// The script uses the CSV loader functionality of Neo4j.
//
// See the DATALOADING_R2.3_README.md for 
// more details on how to run the script.
//

// First, clean out the database(?)
//MATCH (n) DETACH DELETE n;

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

//
// concepts.tsv file format
//
// Columns: 
//
//		qid		semantic_groups		preflabel	synonyms	definition
//
// Where qid is the object id of a WikiData entity
//

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

// statements.tsv file format used for (meta-)data other than core concept descriptions
//
// Columns: 
//
// subject_qid	property_id	object_qid	reference_uri	reference_supporting_text	reference_date
//
// Where *_qid and property_id are object ids of WikiData entities, 
//

//******Predicate Loader*****
CREATE CONSTRAINT ON (predicate:Predicate) ASSERT predicate.uri IS UNIQUE;

// ... Capture Predicates from property_id columns in the input file...
USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/statements.tsv" AS row FIELDTERMINATOR '\t'
WITH 
	row AS row, 
	"http://www.wikidata.org/entity/"+row.property_id AS property_uri
MERGE (predicate:Predicate:IdentifiedEntity:DatabaseEntity { uri : property_uri })
ON CREATE SET 
    predicate.uri = property_uri,
    predicate.accessionId = "wd:"+row.property_id,
    predicate.versionDate = timestamp(),
    predicate.version = TOINT('1')
;

//****** PubMed Catalog ******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity{name : "PubMed"})
ON CREATE SET database.name = "PubMed",
    database.description = "Public Library of Medicine",
    database.url = "https://www.ncbi.nlm.nih.gov/pubmed/",
    database.nameSpacePrefix = "pmid",
    database.accessionId = "pmid:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
;

//******Reference loader******
// Here we assume PubMed identifiers... needs to be generalized
CREATE CONSTRAINT ON (reference:Reference) ASSERT reference.uri IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/statements.tsv" AS row FIELDTERMINATOR '\t'
WITH row, last(split(row.reference_uri,"/")) AS pmid, split(row.reference_date,"-") AS date
WHERE NOT row.reference_uri IS NULL
MERGE (reference:Reference:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { uri : row.reference_uri })
ON CREATE SET 
    reference.uri = row.reference_uri,
    reference.accessionId = "pmid:"+pmid,
    reference.pmid = pmid,
    reference.name = "pmid:"+pmid,
    reference.description = "",
    reference.year = CASE 
    					 WHEN 
    					 	size(date) < 3 OR 
    					 	trim(date[0]) = 'not set' OR 
    					 	length(trim(date[0])) = 0 
    					 THEN TOINT('0') 
    					 ELSE TOINT(date[0]) 
    				  END, 
    reference.month   = CASE 
    					WHEN size(date) < 3 OR 
    					 	 trim(date[0]) = 'not set' OR 
    					 	 length(trim(date[1])) = 0 
    					THEN TOINT('0') 
    					ELSE TOINT(date[1]) 
    				  END, 
    reference.day  = CASE 
    					WHEN size(date) < 3 OR 
    					 	 trim(date[0]) = 'not set' OR 
    					 	 length(trim(date[2])) = 0 
    					THEN TOINT('0') 
    					ELSE TOINT(date[2]) 
    				  END, 
    reference.versionDate = timestamp(),
    reference.version = TOINT('1')   
;

//******Annotation loader******
CREATE CONSTRAINT ON (annotation:Annotation) ASSERT annotation.accessionId IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/statements.tsv" AS row FIELDTERMINATOR '\t'
WITH row
MATCH (reference:Reference   { uri : row.reference_uri })
WITH row AS row, reference AS reference
MERGE (annotation:Annotation:IdentifiedEntity:DatabaseEntity { accessionId : "kba."+reference.name })
ON CREATE SET 
    annotation.uri = "http://knowledge.bio/annotation/"+reference.name,
    annotation.accessionId = "kba."+reference.name,
    annotation.name = row.reference_supporting_text,
    annotation.description = "",
    annotation.versionDate = timestamp(),
    annotation.version = TOINT('1')	
MERGE (annotation)-[:REFERENCE]->(reference)
;

//******Evidence Loader******
// Evidence node used to anchor Annotation associated with Reference uri's
CREATE CONSTRAINT ON (evidence:Evidence) ASSERT evidence.accessionId   IS UNIQUE;

// Perhaps process this complex input in smaller commit blocks
USING PERIODIC COMMIT 1000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/statements.tsv" AS row FIELDTERMINATOR '\t'
WITH 
	row.reference_uri AS reference_uri,
	"http://www.wikidata.org/entity/"+row.subject_qid AS subject_uri,
	"http://www.wikidata.org/entity/"+row.property_id AS property_uri,
	"http://www.wikidata.org/entity/"+row.object_qid  AS object_uri
MATCH (subject:Concept     { uri : subject_uri })
MATCH (relation:Predicate  { uri : property_uri })
MATCH (object:Concept      { uri : object_uri })
MATCH (reference:Reference { uri : reference_uri })
WITH reference AS reference,
	 substring(subject.accessionId,3) + "." + substring(relation.accessionId,3) + "." + substring(object.accessionId,3) AS stmtId
MATCH (annotation:Annotation { accessionId : "kba."+reference.name })
MERGE (evidence:Evidence:IdentifiedEntity:DatabaseEntity { accessionId : "kbe:"+stmtId })
ON CREATE SET 
    evidence.uri = "http://knowledge.bio/evidence/"+stmtId,
    evidence.accessionId = "kbe:"+stmtId,
    evidence.name = "",
    evidence.description = "",
	evidence.count = TOINT('0'),
    evidence.versionDate = timestamp(),
    evidence.version = TOINT('1')
WITH evidence AS evidence, annotation AS annotation
MERGE (evidence)-[:ANNOTATION]->(annotation)
ON CREATE SET evidence.count = evidence.count + 1
;

//******Statement Loader******
CREATE CONSTRAINT ON (statement:Statement)   ASSERT statement.accessionId  IS UNIQUE;

// Perhaps process this complex input in smaller commit blocks
USING PERIODIC COMMIT 1000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/statements.tsv" AS row FIELDTERMINATOR '\t'
WITH 
	row as row,
	"http://www.wikidata.org/entity/"+row.subject_qid AS subject_uri,
	"http://www.wikidata.org/entity/"+row.property_id AS property_uri,
	"http://www.wikidata.org/entity/"+row.object_qid  AS object_uri
MATCH (subject:Concept    { uri : subject_uri })
MATCH (relation:Predicate { uri : property_uri })
MATCH (object:Concept     { uri : object_uri })
WITH subject   AS subject,
	 relation  AS relation,
	 object    AS object,
     substring(subject.accessionId,3) + "." + substring(relation.accessionId,3) + "." + substring(object.accessionId,3) AS stmtId
MATCH (evidence:Evidence:IdentifiedEntity:DatabaseEntity { accessionId : "kbe:"+stmtId })
MERGE (stmt:Statement:IdentifiedEntity:DatabaseEntity    { accessionId : "kbs:"+stmtId })
ON CREATE SET 
    stmt.name = subject.name+" - "+relation.name+" -> "+object.name,
    stmt.description = "",
    stmt.uri = "http://knowledge.bio/statement/"+stmtId,
    stmt.accessionId = "kbs:"+stmtId,
    stmt.versionDate = timestamp(),
    stmt.version = TOINT('1')
MERGE (subject)<-[:SUBJECT]-(stmt)-[:OBJECT]->(object)
ON CREATE SET subject.usage = subject.usage + 1, object.usage = object.usage + 1
MERGE (relation)<-[:RELATION]-(stmt)-[:EVIDENCE]->(evidence)
ON CREATE SET evidence.count = TOINT('1')
ON MATCH  SET evidence.count = evidence.count + 1
;

// Finally, "warm" up the cache...
//MATCH (n)
//OPTIONAL MATCH (n)-[r]->()
//RETURN count(n) + count(r);

