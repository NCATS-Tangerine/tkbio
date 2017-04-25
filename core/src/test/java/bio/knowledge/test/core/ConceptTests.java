/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Scripps Institute (USA) - Dr. Benjamin Good
 *                   Delphinai Corporation (Canada) / MedgenInformatics - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any concept obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit concepts to whom the Software is
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import bio.knowledge.database.repository.ConceptRepository;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyTerm;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.core.ExternalDatabaseService;
import bio.knowledge.service.core.OntologyService;
import bio.knowledge.service.core.OntologyTermService;
import bio.knowledge.test.core.TestConfiguration;

/**
 * @author Richard
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@Transactional
public class ConceptTests {

	private static final String UMLS = "UMLS" ;
	private static final String UMLS_SAB_NAMESPACE = "UMLS.SAB" ;
	private static final String NCI_SAB_TAG_NAME = "NCI" ;
	
	public OntologyTerm NCI_SAB_TAG_TERM ; 

	@Autowired 
	ExternalDatabaseService dbService;

	@Autowired 
	OntologyService ontologyService;

	@Autowired 
	OntologyTermService termService;

	@Autowired 
	ConceptRepository conceptRepository;

	@Autowired 
	ConceptService conceptService;
	
	@Test
	@Transactional
	public void testAddingOfUMLSDefinitionsToConcept() {
		
		Ontology sabOntology  = ontologyService.addOntology(UMLS, UMLS_SAB_NAMESPACE, "") ;
		String UMLS_SAB_ACCID = UMLS_SAB_NAMESPACE+":"+NCI_SAB_TAG_NAME ;
		NCI_SAB_TAG_TERM      = termService.addOntologyTerm( sabOntology, UMLS_SAB_ACCID, NCI_SAB_TAG_NAME, "National Cancer Institute Thesaurus") ;
		
		String TESTCUI  = "smd:C0158981" ;
		String TESTNAME = "Neonatal diabetes mellitus" ;
		
		String TESTAUI  = "00001" ;
		String TESTDEF  = "Hyperglycemia in the newborn due to a defect "
				        + "in the secretion or function of insulin.(NICHD)" ;
		
		Neo4jConcept concept = conceptRepository.findByAccessionId(TESTCUI) ;
		if(concept==null) {
			concept = new Neo4jConcept( TESTCUI, SemanticGroup.PHEN, TESTNAME );
			concept = conceptRepository.save(concept) ;
		}
		
		conceptService.addDefinition( concept, NCI_SAB_TAG_NAME, TESTAUI, TESTDEF );
		
		concept = conceptRepository.findByAccessionId(TESTCUI) ;
		assertNotNull("Concept associated with Concept '"+TESTCUI+"' should not be null!",concept) ;
		assertEquals("Retrieval of 'name' associated with Concept '"+TESTCUI+"'?", concept.getName(), TESTNAME ) ;
		
		Feature definition = conceptService.getDefinition( concept, NCI_SAB_TAG_NAME ) ;
		assertNotNull("Definition associated with '"+NCI_SAB_TAG_NAME+"' should not be null!",definition) ;
		assertEquals("Retrieval of definition associated with '"+NCI_SAB_TAG_NAME+"'?",TESTDEF,definition.getDescription()) ;
		
		/* TODO: new version of Canonical... need to fix this test?
		String canonical = conceptService.getCanonicalDescription( concept ) ;
		assertNotNull("Default description should not be null!",definition) ;
		assertEquals("Retrieval of default description?",TESTDEF,definition.getDescription()) ;
		*/
	}
	
	@Test
	@Transactional
	public void testAddingOfEvidenceToConcept() {
		
		String TESTCUI  = "smd:C0158981" ;
		String TESTNAME = "Neonatal diabetes mellitus" ;
		
		String TESTAUI  = "00001" ;
		String TESTDEF  = "Hyperglycemia in the newborn due to a defect "
				        + "in the secretion or function of insulin.(NICHD)" ;
		
		Neo4jConcept concept = conceptRepository.findByAccessionId(TESTCUI) ;
		if(concept==null) {
			concept = new Neo4jConcept( TESTCUI, SemanticGroup.PHEN, TESTNAME );
			concept = conceptRepository.save(concept) ;
		}
		
		
		
	}		

}
