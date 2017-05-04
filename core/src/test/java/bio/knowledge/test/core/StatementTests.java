/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Scripps Institute (USA) - Dr. Benjamin Good
 *                   Delphinai Corporation (Canada) / MedgenInformatics - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.test.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.Concept;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.EvidenceCode;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.neo4j.Neo4jAnnotation;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.model.neo4j.Neo4jEvidence;
import bio.knowledge.model.neo4j.Neo4jGeneralStatement;
import bio.knowledge.model.neo4j.Neo4jPredicate;
import bio.knowledge.model.neo4j.Neo4jReference;
import bio.knowledge.service.AnnotationService;

import bio.knowledge.database.repository.ReferenceRepository;
import bio.knowledge.database.repository.ConceptRepository;
import bio.knowledge.database.repository.EvidenceRepository;
import bio.knowledge.database.repository.PredicateRepository;
import bio.knowledge.database.repository.StatementRepository;
import bio.knowledge.database.repository.AnnotationRepository;

import bio.knowledge.test.core.TestConfiguration;

/**
 * @author Richard
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@Transactional
public class StatementTests {

	@Autowired 
	ConceptRepository conceptRepository;

	@Autowired 
	ReferenceRepository referenceRepository;

	@Autowired 
	EvidenceRepository evidenceRepository;

	@Autowired 
	AnnotationService annotationService;

	@Autowired 
	AnnotationRepository annotationRepository;

	@Autowired 
	PredicateRepository predicateRepository;

	@Autowired 
	StatementRepository statementRepository;
	
	class ReferenceTestData {
		
		/*
		SELECT * FROM CITATIONS WHERE PMID=5905393;
		+---------+-----------+------------+----------+-------+
		| PMID    | ISSN      | DP         | EDAT     | PYEAR |
		+---------+-----------+------------+----------+-------+
		| 5905393 | 0030-6002 | 1966 Feb 6 | 1966-2-6 |  1966 |
		+---------+-----------+------------+----------+-------+
		*/
		public final String PMID5905393 = "5905393" ;
		public final Neo4jReference PMID5905393_REFERENCE = new Neo4jReference("PubMed Citation for PMID "+PMID5905393) ; 
		
		/*
		+-------------+---------+------+--------+--------------------------------------------------------------+
		| SENTENCE_ID | PMID    | TYPE | NUMBER | ANNOTATION                                                     |
		+-------------+---------+------+--------+--------------------------------------------------------------+
		|     3668220 | 5905393 | ti   |      1 | [Glycoprotein level in umbilical arterial and venous blood]. |
		+-------------+---------+------+--------+--------------------------------------------------------------+

		 */
		public final Neo4jAnnotation GLYP_UMBL_ANNOTATION = 
				new Neo4jAnnotation(
						"kba:3668220", 
						"Glycoprotein level in umbilical arterial and venous blood", 
						Neo4jAnnotation.Type.Title,
						EvidenceCode.IC,
						null
				) ;
		
		public ReferenceTestData() {
			PMID5905393_REFERENCE.setPmid(PMID5905393);
			PMID5905393_REFERENCE.setIssn("0030-6002");
			PMID5905393_REFERENCE.setDatePublished( 1966, 2, 6 );
		}
	}
	
	@Before
	public void setUp() {
	}
	
	@Test
	@Transactional
	public void testReferenceModelPersistance() {
		
		ReferenceTestData referenceTestData = new ReferenceTestData() ;
		Neo4jReference guCit = referenceTestData.PMID5905393_REFERENCE ;
		
		// Sanity check
		assertNotNull(guCit.getYearPublished()) ;
		assertNotNull(guCit.getMonthPublished()) ;
		assertNotNull(guCit.getDayPublished()) ;
		
		System.out.println("Reference NodeId (before saving):\t"+guCit.getDbId()) ;
		
		guCit = referenceRepository.save(guCit);
		
		System.out.println("Reference NodeId (after saving):\t"+guCit.getDbId()) ;

		// retrieve reference by PMID
		Neo4jReference c = referenceRepository.findByPmid(  referenceTestData.PMID5905393 ) ;
		
		assertNotNull(c) ;
		
		assertEquals( c.getDbId(), guCit.getDbId() ) ;
		assertEquals( c.getPmid(),   guCit.getPmid() ) ;
		assertEquals( c.getIssn(),   guCit.getIssn() ) ;
		assertNotNull(c.getYearPublished()) ;
		assertNotNull(c.getMonthPublished()) ;
		assertNotNull(c.getDayPublished()) ;
		assertEquals( c.getYearPublished(), guCit.getYearPublished() ) ;
	}

	public class TestData {
		
		/*
		 * N-glycanase 1
		 */
		public Neo4jConcept NGLY1 ;
		
		/*
		 * RAD23B B Concept gene
		 */
		public Neo4jConcept RAD23B ;

		/*
		+------------+----------+------+-------------------------------+------+------+-----------+
		| CONCEPT_ID | CUI      | TYPE | PREFERRED_NAME                | GHR  | OMIM | IS_ORPHAN |
		+------------+----------+------+-------------------------------+------+------+-----------+
		|      22087 | C0041632 | UMLS | Structure of umbilical artery | NULL | NULL |         0 |
		+------------+----------+------+-------------------------------+------+------+-----------+
		 */
		public Neo4jConcept UMBART ;
		
		/*
		+------------+----------+------+----------------+------+------+-----------+
		| CONCEPT_ID | CUI      | TYPE | PREFERRED_NAME | GHR  | OMIM | IS_ORPHAN |
		+------------+----------+------+----------------+------+------+-----------+
		|       9284 | C0017968 | UMLS | Glycoproteins  | NULL | NULL |         0 |
		+------------+----------+------+----------------+------+------+-----------+
		 */
		public Neo4jConcept C0017968 ;
		
		/* Implicitome
		+------------+----------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+
		|        744 | Abetalipoproteinemia | An autosomal recessive disorder of lipid metabolism. It is caused by mutation of the microsomal triglyceride transfer protein that catalyzes the transport of lipids (TRIGLYCERIDES; CHOLESTEROL ESTERS; PHOSPHOLIPIDS) and is required in the secretion of BETA-LIPOPROTEINS (low density lipoproteins or LDL). Features include defective intestinal lipid absorption, very low serum cholesterol level, and near absent LDL. |         0 |
		+------------+----------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+

		+-----------+------------+-------+--------------------------------------+
		| dblink_id | concept_id | db_id | identifier                           |
		+-----------+------------+-------+--------------------------------------+
		|        52 |        744 | OM    | 200100                               |
		|        53 |        744 | UMLS  | C0000744                             |
		|        54 |        744 | WIKI  | a67558f4-5c2c-11df-b0cb-001517ac506c |
		+-----------+------------+-------+--------------------------------------+
		*/
		public Neo4jConcept C0000744 ;
		
		public TestData() {
			NGLY1  = new Neo4jConcept("55768",SemanticGroup.GENE,"NGLY1");
			RAD23B = new Neo4jConcept("5887",SemanticGroup.GENE,"RAD23B");
			
			UMBART = new Neo4jConcept("C0041632",SemanticGroup.ANAT,"Structure of umbilical artery");
			
			C0017968 = new Neo4jConcept( "C0017968",SemanticGroup.CHEM,"Glycoproteins");
			C0017968.setDescription("NCI_NCI-GLOSS,A protein that has sugar molecules attached to it.");
			
			C0000744 = new Neo4jConcept( "C0000744",SemanticGroup.DISO,"Abetalipoproteinemia" );
			C0000744.setDescription( "An autosomal recessive disorder of lipid metabolism. "+
									"It is caused by mutation of the microsomal triglyceride "+
									"transfer protein that catalyzes the transport of lipids "+
									"(TRIGLYCERIDES; CHOLESTEROL ESTERS; PHOSPHOLIPIDS) and "+
									"is required in the secretion of BETA-LIPOPROTEINS "+
									"(low density lipoproteins or LDL). Features include defective "+
									" intestinal lipid absorption, very low serum cholesterol level, and near absent LDL.");
		}
	}
	@Test
	@Transactional
	public void testConceptModelPersistance() {

		/*+--------------------+------------+---------+-------+------+
		  | CONCEPT_SEMTYPE_ID | CONCEPT_ID | SEMTYPE | NOVEL | UMLS |
		  +--------------------+------------+---------+-------+------+
		  |            1555202 |    1347320 | gngm    | Y     | Y    |
		  +--------------------+------------+---------+-------+------+
		 */
		TestData geneTestdata = new TestData() ;
		Neo4jConcept ngly1 = conceptRepository.save(geneTestdata.NGLY1) ;
		Neo4jConcept ngly1_saved = conceptRepository.findByAccessionId(geneTestdata.NGLY1.getAccessionId()) ;
		assertEquals("Finding what I saved:",ngly1.getDbId(),ngly1_saved.getDbId());
	}
	
	@Test
	@Transactional
	public void testAnnotationModelPersistance() {
		
		ReferenceTestData referenceTestData = new ReferenceTestData() ;
		
		// Reference should exist and be added to the Annotation?
		System.out.println("Reference NodeId (before saving):\t"+referenceTestData.PMID5905393_REFERENCE.getDbId()) ;

		Neo4jReference UMBL_REFERENCE =
				referenceRepository.save( referenceTestData.PMID5905393_REFERENCE );
		
		System.out.println("Reference NodeId (after saving):\t"+UMBL_REFERENCE.getDbId()) ;
		
		Neo4jAnnotation glyp2umbl = referenceTestData.GLYP_UMBL_ANNOTATION ;
		glyp2umbl.setReference(UMBL_REFERENCE);
		
		System.out.println("Annotation NodeId (before saving):\t"+glyp2umbl.getDbId()) ;
		
		glyp2umbl = annotationRepository.save(glyp2umbl);
		
		System.out.println("Annotation NodeId (after saving):\t"+glyp2umbl.getDbId()) ;
		
		Neo4jReference reference = referenceRepository.findByPmid( referenceTestData.PMID5905393 ) ;
		
		assertNotNull(reference) ;
		
		Neo4jAnnotation annotation = annotationRepository.findByReference(reference) ;
		
		assertNotNull(annotation) ;
		
		assertEquals( annotation.getDbId(),   glyp2umbl.getDbId() ) ;
		assertEquals( annotation.getType(),     glyp2umbl.getType() ) ;
		// TODO: Ids are a database entity property. do these tests need to be generalized? see Reference interface for the call for this Id.
		assertEquals( annotation.getReference().getDbId(), glyp2umbl.getReference().getDbId() ) ;
		assertEquals( annotation.getReference().getPmid(), referenceTestData.PMID5905393 ) ;
	}
	
	@Test
	@Transactional
	public void testStatementModelPersistance() {
		
		/*
		+--------------------+------------+---------+-------+------+
		| CONCEPT_SEMTYPE_ID | CONCEPT_ID | SEMTYPE | NOVEL | UMLS |
		+--------------------+------------+---------+-------+------+
		|              12636 |       9284 | aapp    | Y     | Y    |
		|              29497 |      22087 | emst    | Y     | Y    |
		+--------------------+------------+---------+-------+------+
		*/
		
		TestData metadata = new TestData() ;
		Neo4jConcept csGProt  = conceptRepository.save(metadata.C0017968) ;
		Neo4jConcept csUmbArt = conceptRepository.save(metadata.UMBART) ;

		System.out.println("Concepts:");
		for (Neo4jConcept cs : new Neo4jConcept[] { csGProt, csUmbArt }) {
			System.out.println(cs);
		}
		
		csGProt  = conceptRepository.save(csGProt);
		csUmbArt = conceptRepository.save(csUmbArt);
		
		/*
		Evidence extracted from the SENTENCE_PREDICATION table of SemMedDb:
		+-------------------------+-------------+----------------+--------------------+--------------------+--------------+-----------------+---------------------+-------------------+---------------+----------------+-----------------------+---------------------+--------------+-------------+----------------+--------------------+------------------+--------------+---------------------+
		| SENTENCE_PREDICATION_ID | SENTENCE_ID | PREDICATION_ID | PREDICATION_NUMBER | SUBJECT_TEXT       | SUBJECT_DIST | SUBJECT_MAXDIST | SUBJECT_START_INDEX | SUBJECT_END_INDEX | SUBJECT_SCORE | INDICATOR_TYPE | PREDICATE_START_INDEX | PREDICATE_END_INDEX | OBJECT_TEXT  | OBJECT_DIST | OBJECT_MAXDIST | OBJECT_START_INDEX | OBJECT_END_INDEX | OBJECT_SCORE | CURR_TIMESTAMP      |
		+-------------------------+-------------+----------------+--------------------+--------------------+--------------+-----------------+---------------------+-------------------+---------------+----------------+-----------------------+---------------------+--------------+-------------+----------------+--------------------+------------------+--------------+---------------------+
		|                 1257783 |     3668220 |         540408 |                  1 | umbilical arterial |            1 |               2 |                  24 |                41 |           964 | PREP           |                    21 |                  22 | Glycoprotein |           1 |              1 |                  2 |               13 |          694 | 2014-02-11 11:55:19 |
		+-------------------------+-------------+----------------+--------------------+--------------------+--------------+-----------------+---------------------+-------------------+---------------+----------------+-----------------------+---------------------+--------------+-------------+----------------+--------------------+------------------+--------------+---------------------+
		 */
		
		ReferenceTestData referenceTestData = new ReferenceTestData() ;
		
		// Reference should exist and be added to the Annotation?
		System.out.println("Reference NodeId (before saving):\t"+referenceTestData.PMID5905393_REFERENCE.getDbId()) ;

		Neo4jReference UMBL_REFERENCE =
				referenceRepository.save( referenceTestData.PMID5905393_REFERENCE );
		
		System.out.println("Reference NodeId (after saving):\t"+UMBL_REFERENCE.getDbId()) ;

		// The Reference is added to the Annotation that should should exist before Evidence link is created?
		referenceTestData.GLYP_UMBL_ANNOTATION.setReference(UMBL_REFERENCE);
		
		System.out.println("Annotation NodeId (before saving):\t"+referenceTestData.GLYP_UMBL_ANNOTATION.getDbId()) ;

		Neo4jAnnotation GLYP_UMBL_SENTENCE = 
				annotationRepository.save( referenceTestData.GLYP_UMBL_ANNOTATION ) ; 

		System.out.println("Annotation NodeId (after saving):\t"+GLYP_UMBL_SENTENCE.getDbId()) ;

		// Annotation should exist before Evidence link is created?
		Neo4jEvidence GLYP_UMBL_EVIDENCE = new Neo4jEvidence() ;
		GLYP_UMBL_EVIDENCE.addAnnotation(GLYP_UMBL_SENTENCE) ;

		System.out.println("Evidence NodeId (before saving):\t"+GLYP_UMBL_EVIDENCE.getDbId()) ;

		GLYP_UMBL_EVIDENCE = evidenceRepository.save(GLYP_UMBL_EVIDENCE) ;

		System.out.println("Evidence NodeId (before saving):\t"+GLYP_UMBL_EVIDENCE.getDbId()) ;

		/*
			+----------------+-------------+--------+
			| PREDICATION_ID | PREDICATE   | TYPE   |
			+----------------+-------------+--------+
			|         540408 | LOCATION_OF | semrep |
			+----------------+-------------+--------+

			+-------------------------+----------------+--------------------+------+
			| PREDICATION_ARGUMENT_ID | PREDICATION_ID | CONCEPT_SEMTYPE_ID | TYPE |
			+-------------------------+----------------+--------------------+------+
			|                 1086216 |         540408 |              12636 | O    |
			|                 1086210 |         540408 |              29497 | S    |
			+-------------------------+----------------+--------------------+------+
		 */
		
		System.out.println("Statement:");
		Neo4jPredicate predicate = predicateRepository.findPredicateByName("LOCATION_OF");
		if( predicate == null ) {
			// should not be run twice?
			predicate = new Neo4jPredicate("LOCATION_OF") ;
			predicate = predicateRepository.save(predicate) ;
		}
		
		Neo4jGeneralStatement UMBL_LOCATION_OF_GLYP = new Neo4jGeneralStatement( "540408", csUmbArt, predicate, csGProt ) ;
		UMBL_LOCATION_OF_GLYP.setEvidence(GLYP_UMBL_EVIDENCE);
		
		System.out.println("NodeId (before saving):\t"+UMBL_LOCATION_OF_GLYP.getDbId()) ;
		
		UMBL_LOCATION_OF_GLYP = statementRepository.save(UMBL_LOCATION_OF_GLYP);
		
		System.out.println("NodeId (after saving):\t"+UMBL_LOCATION_OF_GLYP.getDbId()) ;
		
		Neo4jGeneralStatement p = statementRepository.findOne(UMBL_LOCATION_OF_GLYP.getDbId()) ;
		
		assertNotNull(p) ;
		
		System.out.println( "\nTesting findOne() of UMBL_LOCATION_OF_GLYP:");
		assertEquals( p.getDbId(), UMBL_LOCATION_OF_GLYP.getDbId() ) ;
		
		List<Concept> subjects = p.getSubjects() ;
		assertNotNull(subjects) ;
		assertTrue("Statement has subjects?",!subjects.isEmpty()) ;
		Concept subject = subjects.get(0) ;
		Concept origSubject = UMBL_LOCATION_OF_GLYP.getSubjects().get(0);
		assertEquals( subject.getDbId(), origSubject.getDbId() ) ;
		assertEquals( subject.getAccessionId(), origSubject.getAccessionId() ) ;
		System.out.println( "Subject accession id: "+subject.getAccessionId());
		
		assertEquals( p.getRelation(), UMBL_LOCATION_OF_GLYP.getRelation() ) ;
		System.out.println( "Relation Name: "+UMBL_LOCATION_OF_GLYP.getRelation().getName() );
		
		List<Concept> objects = p.getObjects() ;
		assertNotNull(objects) ;
		assertTrue("Statement has objects?",!objects.isEmpty()) ;
		Concept object = objects.get(0) ;
		Concept origObject = UMBL_LOCATION_OF_GLYP.getObjects().get(0);
		assertEquals( object.getDbId(),  origObject.getDbId() ) ;
		assertEquals( object.getAccessionId(),  origObject.getAccessionId() ) ;
		System.out.println( "Object accession id: "+object.getAccessionId());
		
		Evidence evidence = p.getEvidence() ;
		assertNotNull(evidence) ;
		System.out.println("Evidence id:\t"+evidence.getDbId()) ;
		
		assertTrue("Statement has some evidence?",!evidence.getAnnotations().isEmpty()) ;
		
		for( Annotation annotation : evidence.getAnnotations() ) {
			System.out.println("Evidence Annotation found:\t"+annotation.getAccessionId()) ;
			assertEquals( annotation.getDbId(), GLYP_UMBL_SENTENCE.getDbId() ) ;
			
			Neo4jReference reference = (Neo4jReference) annotation.getReference();
			assertEquals( reference.getDbId(), UMBL_REFERENCE.getDbId() ) ;
			System.out.println("Annotation Reference found:\t"+reference.getAccessionId()) ;
			
			break ;
		}
		
		System.out.println("\nDirect dump of current statements:\n");
		for(Neo4jGeneralStatement s : statementRepository.getStatements()) {
			System.out.println("Statement: "+s.getName());
			
			subjects = p.getSubjects() ;
			assertNotNull(subjects) ;
			assertTrue("Statement has subjects?",!subjects.isEmpty()) ;
			subject = subjects.get(0) ;
			System.out.println("Subject accessionId: "+subject.getAccessionId());
			
			Predicate relation = p.getRelation();
			System.out.println("Relation: "+relation.getName());
			
			objects = p.getObjects() ;
			assertNotNull(objects) ;
			assertTrue("Statement has objects?",!objects.isEmpty()) ;
			object = objects.get(0) ;
			System.out.println("Object accessionId: "+object.getAccessionId());
		}
		
		System.out.println(
				"\nTesting findBySourceAndTargetAccessionId()"
				+ " retrieval of ("+subject.getAccessionId()+")-[:"
				+ UMBL_LOCATION_OF_GLYP.getRelation().getName()+"]->("
				+object.getAccessionId()+")"
		);

		List<Map<String, Object>> stmtList = 
				statementRepository.findBySourceTargetAndRelation( 
					subject.getAccessionId(), 
					object.getAccessionId(), 
					UMBL_LOCATION_OF_GLYP.getRelation().getName()
			) ;
		
		assertTrue("Have statements?",!stmtList.isEmpty()) ;
		
		Map<String, Object> stmtMap = stmtList.get(0);
		assertNotNull("Have a non-null Statement Map?",stmtMap) ;
		assertTrue("Have a non-empty Statement Map?",!stmtMap.isEmpty()) ;
		System.out.println("\n\nStatement contents successfully retrieved\nby findBySourceAndTargetAccessionId():\n");
		for( Entry<String,Object> item : stmtMap.entrySet()) {
			System.out.println(item.getKey()+" = "+item.getValue().toString());
		}
		
		System.out.println("\n*****");
	}
}
