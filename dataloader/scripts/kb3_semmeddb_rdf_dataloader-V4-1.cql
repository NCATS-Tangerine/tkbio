// @author Richard Bruskiewich (minor modifications)

// This Cypher Query Language (CQL) Script is for 
// loading another sample (WikiData) RDF-like sample of SemMedDb data
//  semmed_all_associated_with_sentences_1.tsv
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

//******Concept Loader*****
CREATE CONSTRAINT ON (concept:Concept) ASSERT concept.uri IS UNIQUE;

// Create Index for making initial search faster (need to review)
CREATE INDEX ON : Concept(name);
CREATE INDEX ON : Concept(usage);
CREATE INDEX ON : Concept(type);

// ... Capture Concepts from subject columns in the input file...
USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/semmed_all_associated_with_sentences_1.tsv" AS row FIELDTERMINATOR '\t'
WITH row AS row, last(split(row.subject_uri,"/")) AS wikidataId
WHERE NOT row.subject_uri IS NULL
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity {uri : row.subject_uri })
ON CREATE SET 
    concept.uri = row.subject_uri,
    concept.accessionId = "wd:"+wikidataId,
    concept.type = upper(row.subject_semantic_group),
    concept.name = row.subject_preferred_name,
    concept.description = "",
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

//******Predicate Loader*****
CREATE CONSTRAINT ON (predicate:Predicate) ASSERT predicate.uri IS UNIQUE;

// ... Capture Predicates from predicate columns in the input file...
USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/semmed_all_associated_with_sentences_1.tsv" AS row FIELDTERMINATOR '\t'
WITH 
	row AS row, 
	CASE  
		WHEN left(row.property_uri,24) = "https://www.wikidata.org"   THEN "wd"
		WHEN left(row.property_uri,26) = "http://purl.obolibrary.org" THEN "obo" 
		ELSE "kb"
	END AS qualifier,
	CASE 
		WHEN left(row.property_uri,24) = "https://www.wikidata.org" THEN last(split(row.property_uri,":")) 
		ELSE last(split(row.property_uri,"/"))
	END AS wikidataId
WHERE NOT row.property_uri IS NULL
MERGE (predicate:Predicate:IdentifiedEntity:DatabaseEntity {uri : row.property_uri })
ON CREATE SET 
    predicate.uri  = row.property_uri,
    predicate.accessionId = qualifier+":"+wikidataId,
    predicate.name = row.property_name,
    predicate.description = "",
    predicate.versionDate = timestamp(),
    predicate.version = TOINT('1')
;

// ... Capture Concepts from object columns in the input file...
USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/semmed_all_associated_with_sentences_1.tsv" AS row FIELDTERMINATOR '\t'
WITH row, last(split(row.object_uri,"/")) AS wikidataId
WHERE NOT row.object_uri IS NULL
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity {uri : row.object_uri })
ON CREATE SET 
    concept.uri = row.object_uri,
    concept.accessionId = "wd:"+wikidataId,
    concept.type = upper(row.object_semantic_group),
    concept.name = row.object_preferred_name,
    concept.description = "",
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

//******Reference loader******
// Here we assume PubMed identifiers... needs to be generalized

CREATE CONSTRAINT ON (reference:Reference) ASSERT reference.uri IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/semmed_all_associated_with_sentences_1.tsv" AS row FIELDTERMINATOR '\t'
WITH row, last(split(row.reference_uri,"/")) AS pmid, split(row.reference_date,"/") AS date
WHERE NOT row.reference_uri IS NULL
MERGE (reference:Reference:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { uri : row.reference_uri })
ON CREATE SET 
    reference.uri = row.reference_uri,
    reference.accessionId = "pmid:"+pmid,
    reference.pmid = pmid,
    reference.name = "pmid:"+pmid,
    reference.description = "",
    reference.month = CASE 
    					 WHEN 
    					 	size(date) < 3 OR 
    					 	trim(date[0]) = 'not set' OR 
    					 	length(trim(date[0])) = 0 
    					 THEN TOINT('0') 
    					 ELSE TOINT(date[0]) 
    				  END, 
    reference.day   = CASE 
    					WHEN size(date) < 3 OR 
    					 	 trim(date[0]) = 'not set' OR 
    					 	 length(trim(date[1])) = 0 
    					THEN TOINT('0') 
    					ELSE TOINT(date[1]) 
    				  END, 
    reference.year  = CASE 
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
LOAD CSV WITH HEADERS FROM "file:///semmeddb/semmed_all_associated_with_sentences_1.tsv" AS row FIELDTERMINATOR '\t'
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

CREATE CONSTRAINT ON (evidence:Evidence)     ASSERT evidence.accessionId   IS UNIQUE;

// Perhaps process this complex input in smaller commit blocks
USING PERIODIC COMMIT 1000
LOAD CSV WITH HEADERS FROM "file:///semmeddb/semmed_all_associated_with_sentences_1.tsv" AS row FIELDTERMINATOR '\t'
WITH row
MATCH (subject:Concept     { uri : row.subject_uri })
MATCH (relation:Predicate  { uri : row.property_uri })
MATCH (object:Concept      { uri : row.object_uri })
MATCH (reference:Reference { uri : row.reference_uri })
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
LOAD CSV WITH HEADERS FROM "file:///semmeddb/semmed_all_associated_with_sentences_1.tsv" AS row FIELDTERMINATOR '\t'
WITH row
MATCH (subject:Concept     { uri : row.subject_uri })
MATCH (relation:Predicate  { uri : row.property_uri })
MATCH (object:Concept      { uri : row.object_uri })
WITH subject   AS subject,
	 relation  AS relation,
	 object    AS object,
     substring(subject.accessionId,3) + "." + substring(relation.accessionId,3) + "." + substring(object.accessionId,3) AS stmtId
MATCH (evidence:Evidence:IdentifiedEntity:DatabaseEntity { accessionId : "kbe:"+stmtId })
MERGE (stmt:Statement:IdentifiedEntity:DatabaseEntity  { accessionId : "kbs:"+stmtId })
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
