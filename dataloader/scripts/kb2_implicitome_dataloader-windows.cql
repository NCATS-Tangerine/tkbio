// @author Richard Bruskiewich (inspired by SemMedDB CQL script)

// This Cypher Query Language (CQL) Script is for 
// loading the production Knowledge.Bio database
// from pipe delimited text file data dumps from 
// the MySQL version of Implicitome datasets.
//
// The script uses the CSV loader functionality of Neo4j.
//
// See the DATALOADING_R2.3_README.md for 
// more details on how to run the script.
//

//******Implicitome******
MERGE (database:ExternalDatabase:IdentifiedEntity:DatabaseEntity { name : "Implicitome" } )
ON CREATE SET database.name = "Implicitome",
    database.description = "Implicitome Dataset",
    database.url = "",
    database.nameSpacePrefix = "IPCT",
    database.accessionId = "IPCT:DB",
    database.versionDate = timestamp(),
    database.version = TOINT('1')
; 

// ****CUI Resolution via DBLink***
//
// Prerequisite: this loader assumes that 
// SemMedDb is already loaded into the database
//
// I then need to initially parse in the dblink table, 
// to unify the Implicitome (implicitomeConceptId)
// with SemMedDb using dblink CUI candidates. 
// Since many entries have many potential CUI's, we need 
// to iteratively parse the dblinks file in a "priority" order
//
// The Implicitome DBLinks file is not consistent in its treatment of CUI's
// so there is quite a bit of guess work required here!

CREATE CONSTRAINT ON (concept:Concept) ASSERT concept.implicitomeConceptId IS UNIQUE;

// Start with UMLS settings, which are the "gold standard" for alignment to SemMedDb
// The implicitomeConceptId is assumed "NULL" at this point and can be fixed here for many entries, we hope!
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = 'UMLS'
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { cui : row[3] })
ON CREATE SET
    concept.accessionId = "UMLS:"+row[3],
    concept.cui = row[3],
    concept.type = "UMLS",
    concept.name = "",
    concept.description = "",
    concept.ghr = "",
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
ON MATCH SET 
    concept.implicitomeConceptId = row[1],
    concept.dbLink = [],
    concept.term = []
;

// Human Genome Nomenclature Committee CUI is better than OMIM and Entrez, but inferior to UMLS(?)
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = 'HG'
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { implicitomeConceptId : row[1] })
ON CREATE SET
    concept.accessionId = "HGNC:"+row[3],
    concept.cui = row[3],
    concept.type = "GENE",
    concept.name = "",
    concept.description = "",
    concept.ghr = "",
    concept.implicitomeConceptId = row[1],
    concept.dbLink = [],
    concept.term = [],
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
;

// OMIM is better than Entrez, but inferior to HGNC and UMLS(?)
// (not foolproof... OMIM id encountered might not be the only or best one?)
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = 'OM'
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { implicitomeConceptId : row[1] })
ON CREATE SET
    concept.accessionId = "OMIM:"+row[3],
    concept.cui = row[3],
    concept.type = "OMIM",
    concept.name = "",
    concept.description = "",
    concept.ghr = "",
    concept.implicitomeConceptId = row[1],
    concept.dbLink = [],
    concept.term = [],
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
;

// Try for an NCBI Gene ("Entrez") identifier
// (not foolproof... Gene id encountered might not be the only or best one?)
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = 'EG'
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { implicitomeConceptId : row[1] })
ON CREATE SET
    concept.accessionId = "GENE:"+row[3],
    concept.cui = row[3],
    concept.type = "GENE",
    concept.name = "",
    concept.description = "",
    concept.ghr = "",
    concept.implicitomeConceptId = row[1],
    concept.dbLink = [],
    concept.term = [],
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
;

// Try next for an Human Metabolite Database identifier
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = 'HMDB'
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { implicitomeConceptId : row[1] })
ON CREATE SET
    concept.accessionId = "HMDB:"+row[3],
    concept.cui = row[3],
    concept.type = "METABOLITE",
    concept.name = "",
    concept.description = "",
    concept.ghr = "",
    concept.implicitomeConceptId = row[1],
    concept.dbLink = [],
    concept.term = [],
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
;

// Finally, use any PubChem identifier...
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = 'PUBC'
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { implicitomeConceptId : row[1] })
ON CREATE SET
    concept.accessionId = "PUBC:"+row[3],
    concept.cui = row[3],
    concept.type = "CHEMICAL",
    concept.name = "",
    concept.description = "",
    concept.ghr = "",
    concept.implicitomeConceptId = row[1],
    concept.dbLink = [],
    concept.term = [],
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
;

// ... or the Chemical Abstracts identifier
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row
WHERE row[2] = 'CAS'
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity { implicitomeConceptId : row[1] })
ON CREATE SET
    concept.accessionId = "CAS:"+row[3],
    concept.cui = row[3],
    concept.type = "CHEMICAL",
    concept.name = "",
    concept.description = "",
    concept.ghr = "",
    concept.implicitomeConceptId = row[1],
    concept.dbLink = [],
    concept.term = [],
    concept.versionDate = timestamp(),
    concept.version = TOINT('1')
;

// Won't try to find any more CUI at this point...

// Somewhat redundant if SemMedDb has properly created Concepts
// and the cui link to implicitome dblink was resolved above...
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_concept.txt" AS row fieldterminator '|'
WITH row 
MERGE (concept:Concept:AnnotatedEntity:IdentifiedEntity:DatabaseEntity{ implicitomeConceptId : row[0] })
ON MATCH SET
	concept.name = CASE concept.name WHEN "" THEN row[1] ELSE concept.name END,
	concept.description = CASE concept.description WHEN "" THEN row[1] ELSE concept.description END
;

// ****Complete Full DBLink Loading***
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_dblink.txt" AS row fieldterminator '|'
WITH row, row[2] + ":" + row[3] AS identifier
WHERE row[2] <> 'WIKI'
MATCH (concept:Concept { implicitomeConceptId : row[1] })
WHERE NOT identifier IN concept.dbLink
SET concept.dbLink = concept.dbLink + identifier
;

// ****Term Loader***
USING PERIODIC COMMIT 5000
LOAD CSV  FROM "file:/C:/implicitome_term.txt" AS row fieldterminator '|'
WITH row
MATCH (concept:Concept { implicitomeConceptId : row[1] })
WHERE NOT row[3] IN concept.term
SET concept.term = concept.term + row[3]
;
