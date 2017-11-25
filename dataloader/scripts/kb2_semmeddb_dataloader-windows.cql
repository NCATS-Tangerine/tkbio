// @author Chandan Mishra (primary author!)
// @author Richard Bruskiewich (minor modifications)

// This Cypher Query Language (CQL) Script is for 
// loading the production Knowledge.Bio database
// from pipe delimited text file data dumps from 
// the MySQL version of SemMedDB and UMLS datasets.
//
// The script uses the CSV loader functionality of Neo4j.
//
// See the DATALOADING_R2.3_README.md for 
// more details on how to run the script.
//
// NOTE: YOU MAY NEED TO CHANGE THE WINDOWS FILE PATHS 
// BELOW TO POINT TO YOUR DATABASE LOCATION!
//

//******PubMed******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity{name : "PubMed"})
ON CREATE SET database.name = "PubMed",
    database.description = "PubMed",
    database.url = "http://www.ncbi.nlm.nih.gov/pubmed/",
    database.nameSpacePrefix = "PMID",
    database.accessionId = "PMID:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
;

//******NCBI Gene Catalog******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity{name : "NCBI Gene"})
ON CREATE SET database.name = "NCBI Gene",
    database.description = "NCBI Reference Gene Catalog",
    database.url = "http://www.ncbi.nlm.nih.gov/gene/",
    database.nameSpacePrefix = "GENE",
    database.accessionId = "GENE:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
;


//******Online Mendelian Inheritance in Man******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity{name : "OMIM"})
ON CREATE SET database.name = "OMIM",
    database.description = "Online Mendelian Inheritance in Man",
    database.url = "http://www.ncbi.nlm.nih.gov/omim/",
    database.nameSpacePrefix = "OMIM",
    database.accessionId = "OMIM:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
;


//******SNOMED CT - Clinical Terms******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity{name : "SNOMED_CT"})
ON CREATE SET database.name = "SNOMED_CT",
    database.description = "Systematized Nomenclature of Medicine (SNOMED) Clinical Terms",
    database.url = "http://www.snomed.org/doc",
    database.nameSpacePrefix = "SNOMED_CT",
    database.accessionId = "SNOMED_CT:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
;

//******Human Metabolome Database******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity {name : "HMDB"})
ON CREATE SET database.name = "HMDB",
    database.description = "Human Metabolome Database",
    database.url = "http://www.hmdb.ca/",
    database.nameSpacePrefix = "HMDB",
    database.accessionId = "HMDB:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
;

//******Citation loader******
// Uniqueness constraints.
CREATE CONSTRAINT ON (citation:Citation) ASSERT citation.pmid IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_citations.txt" AS row fieldterminator '|'
WITH row, SPLIT(REPLACE(row[3]," ","-"), '-') AS date
MERGE (citation:Citation:IdentifiedEntity:DatabaseEntity {pmid: row[0]})
ON CREATE SET citation.issn = CASE row[1] WHEN 'null' THEN "" ELSE row[1] END,
    citation.year  = TOINT(date[0]),
    citation.month = TOINT(date[1]),
    citation.day   = TOINT(date[2]),
    citation.name  = "",
    citation.description = "",
    citation.accessionId = "PMID:"+row[0],
    citation.versionDate = timestamp(),
    citation.version = TOINT('1')
;

//******SemMedDb******
CREATE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity)
SET database.name = "SemMedDB",
    database.description = "Semantic Medline Database",
    database.url = "https://skr3.nlm.nih.gov/SemMedDB/",
    database.nameSpacePrefix = "SMD",
    database.accessionId = "SMD:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
; 

//******Sentence loader******
CREATE CONSTRAINT ON (s:Sentence) ASSERT s.sentenceId IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_sentence.txt" AS row fieldterminator '|'
WITH row
MATCH (citation:Citation{pmid : row[1]})
MERGE (s:Sentence:IdentifiedEntity:DatabaseEntity {sentenceId : row[0]})
ON CREATE SET 
    s.name = "",
    s.description = "",
    s.accessionId = "SMD.S:"+row[0],
    s.versionDate = timestamp(),
    s.version = TOINT('1'),
    s.type = CASE row[2] WHEN 'ti' THEN 'Title' WHEN 'ab' THEN 'Abstract' END,
    s.sentence = row[4],
    s.number = TOINT(row[3])
MERGE (s)-[:PMID]->(citation)
;

// Here, I am creating Predicate, Predication and PREDICATE relation in three different pass of same file 
// "semmeddb_predication.txt" and reason for that is as on merge of three query in single query we are 
// introducing "eagerness" in query plan which is throwing GC Overhead error.
// Link : http://www.markhneedham.com/blog/2014/10/23/neo4j-cypher-avoiding-the-eager/

//******Predicate Loader*****
CREATE CONSTRAINT ON (predicate:Predicate) ASSERT predicate.name IS UNIQUE;

// ... First, I check for a local predicate definitions file...
LOAD CSV  FROM "file:/C:/umls_predicate.txt" AS row fieldterminator '|'
WITH row
MERGE (predicate:Predicate:IdentifiedEntity:DatabaseEntity {name : row[0]})
ON CREATE SET 
    predicate.name = row[0],
    predicate.description = row[1],
    predicate.accessionId = "SMD.OP:"+row[0],
    predicate.versionDate = timestamp(),
    predicate.version = TOINT('1')
;

//... then, capture additional Predicate names from Predication file...
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_predication.txt" AS row fieldterminator '|'
WITH row
WHERE (row[1] <> 'PROCESS_OF' AND row[1] <> 'NEG_PROCESS_OF')
MERGE (predicate:Predicate:IdentifiedEntity:DatabaseEntity {name : row[1]})
ON CREATE SET 
    predicate.name = row[1],
    predicate.description = "",
    predicate.accessionId = "SMD.OP:"+row[1],
    predicate.versionDate = timestamp(),
    predicate.version = TOINT('1')
;

//******Predication loader******
CREATE CONSTRAINT ON (pred:Predication) ASSERT pred.predicationId IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_predication.txt" AS row fieldterminator '|'
WITH row
WHERE (row[1] <> 'PROCESS_OF' AND row[1] <> 'NEG_PROCESS_OF')
MERGE (pred:Predication:Association:IdentifiedEntity:DatabaseEntity {predicationId : row[0]})-[:EVIDENCE]->(evidence:CitationEvidence:Evidence:IdentifiedEntity:DatabaseEntity { score : "0" })
ON CREATE SET 
    pred.name = row[1],
    pred.description = "",
    pred.accessionId = "SMD.P:"+row[0],
    pred.versionDate = timestamp(),
    pred.version = TOINT('1'),
    pred.type = UPPER(row[2]),
    evidence.name = "",
    evidence.description = "",
    evidence.accessionId = "SMD.E:"+row[0],
    evidence.versionDate = timestamp(),
    evidence.version = TOINT('1')   
;

//******PREDICATE RELATION*****
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_predication.txt" AS row fieldterminator '|'
WITH row
WHERE (row[1] <> 'PROCESS_OF' AND row[1] <> 'NEG_PROCESS_OF')
MATCH (predicate:Predicate {name : row[1]})
MATCH (pred:Predication {predicationId : row[0]})
MERGE (pred)-[:PREDICATE]->(predicate)
;

//******Sentence Prediction loader******
CREATE CONSTRAINT ON (sp:SentencePredication) ASSERT sp.sentencePredicationId IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_sentence_predication.txt" AS row fieldterminator '|'
WITH row
MATCH (s:Sentence {sentenceId : (row[1])})
MERGE (sp:SentencePredication:IdentifiedEntity:DatabaseEntity {sentencePredicationId : row[0]})
ON CREATE SET 
    sp.name = "",
    sp.description = "",
    sp.accessionId = "SMD.SP:"+row[0],
    sp.versionDate = timestamp(),
    sp.version = TOINT('1')
MERGE (sp)-[:SENTENCE]->(s)
;

//****SUPPORTED_BY Relation****
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_sentence_predication.txt" AS row fieldterminator '|'
WITH row
MATCH (pred:Predication {predicationId : (row[2])})-[:EVIDENCE]->(evidence:CitationEvidence)
MATCH (sp:SentencePredication{sentencePredicationId : row[0]})
MERGE (evidence)-[:SUPPORTED_BY]->(sp)
ON CREATE SET evidence.score = toString( toInt(evidence.score) + 1 )
ON MATCH  SET evidence.score = toString( toInt(evidence.score) + 1 )
;

// ******ExplicitConcept Loader******
CREATE CONSTRAINT ON (concept:Concept) ASSERT concept.semMedDbConceptId IS UNIQUE;
CREATE CONSTRAINT ON (concept:Concept) ASSERT concept.cui IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept.txt" AS row fieldterminator '|'
WITH row 
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity{semMedDbConceptId : row[0]})
ON CREATE SET
    concept.cui = row[1],
    concept.type = CASE row[2] WHEN "ENTREZ" THEN "GENE" WHEN "META" THEN "UMLS" WHEN "CT" THEN "CLINICAL_TERM" WHEN "HMDB" THEN "METABOLITE" END,
    concept.name = row[3],
    concept.description = "",
    concept.versionDate = timestamp(),
    concept.version = TOINT('1'),
    concept.ghr = CASE row[4] WHEN "\\N" THEN "" ELSE row[4] end,
    concept.accessionId = CASE row[2] WHEN "ENTREZ" THEN "GENE:"+row[1] WHEN "META" THEN "UMLS:"+row[1] WHEN "CT" THEN "SNOMED_CT:"+row[1] WHEN "HMDB" THEN "HMDB:"+row[1] END
ON MATCH SET
    concept.ghr = CASE row[4] WHEN "\\N" THEN "" ELSE row[4] end,
    concept.accessionId = CASE row[2] WHEN "ENTREZ" THEN "GENE:"+row[1] WHEN "META" THEN "UMLS:"+row[1] WHEN "CT" THEN "SNOMED_CT:"+row[1] WHEN "HMDB" THEN "HMDB:"+row[1] END
;

// ****Assign Gene label***
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = "ENTREZ"
MATCH (concept:Concept{semMedDbConceptId : row[0]})
SET concept : Gene
;

// ****Assign UMLS label to META Concepts***
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept.txt" AS row fieldterminator '|'
WITH row 
WHERE row[2] = "META"
MATCH (concept:Concept{semMedDbConceptId : row[0]})
SET concept : UMLS
;

// ****Assign ClinicalTerm label to CT Concepts***
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept.txt" AS row fieldterminator '|'
WITH row 
WHERE row[2] ="CT"
MATCH (concept:Concept{semMedDbConceptId : row[0]})
SET concept : ClinicalTerm
;

// ****Assign Metabolite label***
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = "HMDB"
MATCH (concept:Concept{semMedDbConceptId : row[0]})
SET concept : Metabolite
;

// **** OMIM *****
CREATE CONSTRAINT ON (omim:OMIM) ASSERT omim.cui IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept.txt" AS row fieldterminator '|'
WITH row 
WHERE row[5] <> "\\N"
MATCH (concept:Concept{semMedDbConceptId : row[0]})
WITH split(row[5],':') as ids, concept
UNWIND ids as id
MERGE(omim:OMIM {cui: id})
ON CREATE SET
        omim.type = "OMIM",
        omim.name = "",
        omim.description = "",
        omim.accessionId = "OMIM:"+id,
        omim.versionDate = timestamp(),
        omim.version = TOINT('1')
MERGE (concept)-[:OMIM]->(omim)
;

// ******ConceptSemanticType Loader******
CREATE CONSTRAINT ON (cst:ConceptSemanticType) ASSERT cst.conceptSemanticTypeId IS UNIQUE;
// Create Index for making initial search faster (need to review)
CREATE INDEX ON : ConceptSemanticType(name);
CREATE INDEX ON : ConceptSemanticType(usage);
CREATE INDEX ON : ConceptSemanticType(type);

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept_semantic_type.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] <> 'invt'
MATCH (concept:Concept {semMedDbConceptId:row[1]})
MERGE (cst:ConceptSemanticType:AnnotatedEntity:IdentifiedEntity:DatabaseEntity {conceptSemanticTypeId : row[0]})
ON CREATE SET 
    cst.name  = CASE concept WHEN NULL THEN "Unknown "+row[2]  ELSE concept.name END,
    cst.usage = TOINT('0'),
    cst.novel = CASE row[3] WHEN "Y" THEN true ELSE false END,
    cst.umls  = CASE row[4] WHEN "Y" THEN true ELSE false END,
    cst.type  = row[2],
    cst.description = "",
    cst.accessionId = CASE concept 
    					WHEN NULL THEN "SMD.CST:"+row[0]+"."+row[2]
    					ELSE "SMD.CST:"+concept.type+"."+concept.cui+"."+row[2] 
    				  END,
    cst.versionDate = timestamp(),
    cst.version = TOINT('1')
MERGE (cst)-[:ROOT_CONCEPT]->(concept)
MERGE (concept)-[:EXPLICIT_CONCEPT]->(cst)
;

//******PredicationArgument Subject Loader******
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_predication_argument.txt" AS row fieldterminator '|'
WITH row
WHERE row[3] = "S"
MATCH (pred:Predication {predicationId: row[1]} )
MATCH (cst:ConceptSemanticType {conceptSemanticTypeId:row[2]} )
MERGE (pred)-[:SUBJECT]->(cst)
ON CREATE SET cst.usage = cst.usage + 1 
ON MATCH SET cst.usage = cst.usage + 1 
;
//******PredicationArgument Object Loader******
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_predication_argument.txt" AS row fieldterminator '|'
WITH row
WHERE row[3] = "O"
MATCH (pred:Predication {predicationId: row[1]} )
MATCH (cst:ConceptSemanticType {conceptSemanticTypeId:row[2]} )
MERGE (pred)-[:OBJECT]->(cst)
ON CREATE SET cst.usage = cst.usage + 1 
ON MATCH SET cst.usage = cst.usage + 1
;

//******UMLS Definitions Loader******

//******UMLS Database******
CREATE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity)
SET database.name = "UMLS",
    database.description = "Unified Medical Language System",
    database.url = "https://www.nlm.nih.gov/research/umls/",
    database.nameSpacePrefix = "UMLS",
    database.accessionId = "UMLS:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1') 
;
//******Ontology*******
CREATE CONSTRAINT ON (ontology:Ontology) ASSERT ontology.name IS UNIQUE;

CREATE (ontology:Ontology:IdentifiedEntity:DatabaseEntity)
SET ontology.name = "UMLS/SAB",
    ontology.description = "UMLS Source Abbreviations",
    ontology.context = "ANY_CONTEXT",
    ontology.accessionId = "UMLS.SAB",
    ontology.versionDate = timestamp(),
    ontology.version = TOINT('1') 
;


//****Ontology Term******
CREATE CONSTRAINT ON (ontologyTerm:OntologyTerm) ASSERT ontologyTerm.accessionId IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/umls_definition.txt" AS row fieldterminator '|'
WITH row
MATCH (ontology:Ontology{accessionId:"UMLS.SAB"})
MERGE (term:OntologyTerm:IdentifiedEntity:DatabaseEntity{name:row[5]})
ON CREATE SET 
	term.description = row[5]+" Definition",
    term.accessionId = "UMLS.SAB:"+row[5],
    term.versionDate = timestamp(),
    term.version = TOINT('1') 
MERGE (term)-[:ONTOLOGY]->(ontology)
;

//****Feature node and TAG relation*****
CREATE CONSTRAINT ON (feature:Feature) ASSERT feature.accessionId IS UNIQUE;

USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/umls_definition.txt" AS row fieldterminator '|'
WITH row
MATCH (ontologyTerm:OntologyTerm{name:row[5]})
MERGE (feature:Feature:IdentifiedEntity:DatabaseEntity{accessionId:"UMLS.DEF:"+row[1]+"."+row[2]+"."+row[3]})
ON CREATE SET 
    feature.name = row[5],
    feature.description = row[6],
    feature.versionDate = timestamp(),
    feature.version = TOINT('1')
CREATE (feature)-[:TAG]->(ontologyTerm)
;

//***** OWNER and FEATURE relation *****
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/umls_definition.txt" AS row fieldterminator '|'
WITH row
MATCH (feature:Feature{accessionId:"UMLS.DEF:"+row[1]+"."+row[2]+"."+row[3]})
MATCH (concept:Concept{cui:row[1]})
MERGE (feature)-[:OWNER]->(concept)    
MERGE (concept)-[:FEATURE]->(feature)
;

//******SOURCE Relation for External Database******

// Index on accessionId used in SOURCE creation across all external databases
CREATE INDEX ON:IdentifiedEntity(accessionId);

//****** PMID SOURCE RELATION ******
MATCH (pmid:ExternalDatabase{accessionId:"PMID:DB"})
MATCH (identified:IdentifiedEntity) WHERE identified.accessionId =~ 'PMID.*'
MERGE (identified)-[:SOURCE]->(pmid)
;

//****** SemMedDb SOURCE RELATION ******
MATCH (smd:ExternalDatabase{accessionId:"SMD:DB"})
MATCH (identified:IdentifiedEntity) WHERE identified.accessionId =~ 'SMD.*'
CREATE (identified)-[:SOURCE]->(smd)
;

//****** GENE SOURCE RELATION ******
MATCH (gene:ExternalDatabase{accessionId:"GENE:DB"})
MATCH (identified:IdentifiedEntity) WHERE identified.accessionId =~ 'GENE.*'
MERGE (identified)-[:SOURCE]->(gene)
;

//****** OMIM SOURCE RELATION ******
MATCH (omim:ExternalDatabase{accessionId:"OMIM:DB"})
MATCH (identified:IdentifiedEntity) WHERE identified.accessionId =~ 'OMIM.*'
MERGE (identified)-[:SOURCE]->(omim)
;

//****** CT SOURCE RELATION ******
MATCH (ct:ExternalDatabase{accessionId:"SNOMED_CT:DB"})
MATCH (identified:IdentifiedEntity) WHERE identified.accessionId =~ 'SNOMED_CT.*'
MERGE (identified)-[:SOURCE]->(ct)
;

//****** HMDB SOURCE RELATION ******
MATCH (hmdb:ExternalDatabase{accessionId:"HMDB:DB"})
MATCH (identified:IdentifiedEntity) WHERE identified.accessionId =~ 'HMDB.*'
MERGE (identified)-[:SOURCE]->(hmdb)
;

//****** UMLS SOURCE RELATION ******
MATCH (umls:ExternalDatabase{accessionId:"UMLS:DB"})
MATCH (identified:IdentifiedEntity) WHERE identified.accessionId =~ 'UMLS.*'
MERGE (identified)-[:SOURCE]->(umls)
;
//***** LIBRARY*****
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/semmeddb_concept_semantic_type.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] <> 'invt'
MATCH (cst:ConceptSemanticType {conceptSemanticTypeId : row[0]})
MERGE (cst)-[:LIBRARY]->(library:Library)
ON CREATE SET library.numberOfArchivedMaps = TOINT('0'),
    library.name = cst.name +"_library",
    library.description = "Library Of "+ cst.description,
    library.accessionId = cst.accessionId,
    library.versionDate = timestamp(),
    library.version = TOINT('1')
;
