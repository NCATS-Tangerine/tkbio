// @author Lance Hannestad

CREATE (t:Dog:Animal {name: "Ugly Dog"});

//******Implicitome******
CREATE (database:ExternalDataBase:DatabaseEntity:IdentifiedEntity)
SET
    database.name = "Implicitome",
    database.description = "The Implicitome: A Resource for Rationalizing Gene-Disease Associations",
    database.url = "http://datadryad.org/resource/doi:10.5061/dryad.gn219",
    database.nameSpacePrefix = "?",
    database.accessionId = "?",
    database.versionDate = "?",
    database.versionDate = timestamp(),
    database.version = TOINT(1)
;

// Creating the Implicitome relation, of which there is only one: being quantifiably associated with
MERGE (predicate:Predicate:DatabaseEntity:IdentifiedEntity {name : "QUANTIFIABLY_ASSOCIATED_WITH"})
ON CREATE SET
    predicate.description = "",
    predicate.accessionID = "",
    predicate.versionDate = timestamp(),
    predicate.version = TOINT('1')
;

//******Loading Implicitome Relations and Relata******
CREATE CONSTRAINT ON (predication:Predication) ASSERT predication.predicationId IS UNIQUE;
CREATE CONSTRAINT ON (concept:Concept) ASSERT concept.cuid IS UNIQUE;

LOAD CSV FROM "file:///implicitome_tuples.txt" AS row fieldterminator '|'
MATCH (predicate:Predicate {name : "QUANTIFIABLY_ASSOCIATED_WITH"})
WITH predicate, row

MERGE (predication:Predication:DatabaseEntity:IdentifiedEntity {
    predicationId : row[0],
    name : "QUANTIFIABLY_ASSOCIATED_WITH",
    association_match_score : row[5],
    percentile_rank : row[6]
    //TODO: Evidence??
})

MERGE (predication)-[:PREDICATE]->(predicate)

MERGE (object:Concept:AnnotatedEntity:IdentifiedEntity {
    cuid : row[1],
    name : row[2]
})

MERGE (subject:Concept:AnnotatedEntity:IdentifiedEntity {
    cuid : row[3],
    name : row[4]
})

// WITH object, subject
//
// MATCH (cst_object)-[:ROOT_CONCEPT]->(object)
// MATCH (cst_subject)-[:ROOT_CONCEPT]->(subject)
//
// WITH cst_object, cst_subject

MERGE (predication)-[:OBJECT]->(object)
MERGE (predication)-[:SUBJECT]->(subject)
;
