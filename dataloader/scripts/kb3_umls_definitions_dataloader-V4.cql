// @author Richard Bruskiewich (minor modifications)

// This Cypher Query Language (CQL) Script is for 
// loading the definitions for UMLS Predicates, 
// based on Predicate name matching
//
// See http://bmcbioinformatics.biomedcentral.com/articles/10.1186/1471-2105-12-486
//
// The script uses the CSV loader functionality of Neo4j.
//
// See the DATALOADING_R2.3_README.md for 
// more details on how to run the script.
//

//******Predicate Definition Loader*****
CREATE CONSTRAINT ON (predicate:Predicate) ASSERT predicate.name IS UNIQUE;

// ... First, I check for a local predicate definitions file...
LOAD CSV  FROM "file:///umls/umls_predicate.txt" AS row fieldterminator '|'
WITH row
MERGE (predicate:Predicate:IdentifiedEntity:DatabaseEntity {name : row[0]})
ON CREATE SET 
    predicate.uri  = "http://bmcbioinformatics.biomedcentral.com/articles/10.1186/1471-2105-12-486#"+row[0],
    predicate.accessionId = "UMLS:"+row[0],
    predicate.name = row[0],
    predicate.description = row[1],
    predicate.versionDate = timestamp(),
    predicate.version = TOINT('1')
ON MATCH SET 
    predicate.description = row[1]
;