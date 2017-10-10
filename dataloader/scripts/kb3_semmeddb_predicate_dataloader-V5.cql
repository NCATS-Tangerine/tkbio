// @author Richard Bruskiewich (minor modifications)
//
// This Cypher Query Language (CQL) Script is to ensure 
// pre-loading WikiData "predicates" found in subsequent statements
//
// November 30th, 2016 to load in predicate properties 
// from the generated statement specification file
//
// Input data is in a tab delimited table file, one statement per row
//
// Columns:
//
//           subject_qid	property_id	object_qid	reference_uri	reference_supporting_text
//
// where the property_id is assumed to be WikiData property entity object identifier,
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
