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
package bio.knowledge.test.database;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import bio.knowledge.database.repository.ConceptRepository;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.test.database.TestConfiguration;

/**
 * @author Richard
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@Transactional
public class ConceptTests {
	
	@Autowired 
	ConceptRepository conceptRepository;

	@Test
	@Transactional
	public void testConceptType() {

		Neo4jConcept brca1     = new Neo4jConcept("1",SemanticGroup.GENE,"Brca1");
		Neo4jConcept brca2     = new Neo4jConcept("2",SemanticGroup.GENE,"Brca2");
		Neo4jConcept wrn       = new Neo4jConcept("3",SemanticGroup.GENE,"Wrn");
		Neo4jConcept diabetes  = new Neo4jConcept("4",SemanticGroup.DISO,"Diabetes");
		Neo4jConcept psp       = new Neo4jConcept("5",SemanticGroup.DISO,"PSP");
		Neo4jConcept metformin = new Neo4jConcept("6",SemanticGroup.CHEM,"Metformin");

		System.out.println("Before linking up with Neo4j...");
		for (Neo4jConcept item : new Neo4jConcept[] { brca1, brca2, wrn, diabetes, psp, metformin }) {
			System.out.println(item);
		}
		
		conceptRepository.save(brca1);
		conceptRepository.save(brca2);
		conceptRepository.save(wrn);
		conceptRepository.save(diabetes);
		conceptRepository.save(psp);
		conceptRepository.save(metformin);
		
		System.out.println("Getting Concept types with frequencies...");

		List<Map<String,Object>> result = 
				conceptRepository.countAllGroupBySemanticGroup();
		
		System.out.println("... dumped here...");
		for(Map<String,Object> entry : result ) {
			System.out.println(
					"Type: "+entry.getOrDefault("type", "Unknown").toString()+
				  ", Count: "+entry.getOrDefault("frequency", "Unknown").toString());
		}
	}
}
