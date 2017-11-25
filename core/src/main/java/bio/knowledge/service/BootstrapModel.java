/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
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
package bio.knowledge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import bio.knowledge.model.DomainModelException;
import bio.knowledge.model.KBDatabase;
import bio.knowledge.model.KBOntology;
import bio.knowledge.model.core.ExternalDatabase;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyTerm;
import bio.knowledge.service.core.ExternalDatabaseService;
import bio.knowledge.service.core.OntologyService;
import bio.knowledge.service.core.OntologyTermService;

/**
 * Spring creates an instance of this component class.
 * to inject database dependencies. However, execution
 * of the data loading is now deferred to the 
 * initialization context of the DesktopUI.
 * 
 * Initial copy adapted from GTTN timber project...
 * @author Richard
 * 
 *
 */
//@Component
public class BootstrapModel {
	
	private Logger _logger = LoggerFactory.getLogger(BootstrapModel.class);
	
    @Autowired
	private ExternalDatabaseService databaseService ;

    @Autowired
	private OntologyService ontologyService ;
    
    @Autowired
	private OntologyTermService ontologyTermService ;
    
	public void loadModel() throws DomainModelException {
		
		ExternalDatabase kbDb = null ;
		Ontology kb_ontology  = null ;
		
		kbDb = databaseService.getDatabaseByName(KBDatabase.KB_DATABASE) ;
		
		if( kbDb == null ) {

			kbDb = databaseService.addDatabase(
							KBDatabase.KB_DATABASE, 
							"Knowledge.Bio Application",
							"http://knowledge.bio"
					);
		}
			
		kb_ontology = ontologyService.getOntologyByName(KBOntology.KB_GENERAL_ONTOLOGY) ;
		
		if( kb_ontology == null ) {
			
			kb_ontology = ontologyService.addOntology(
					KBDatabase.KB_DATABASE,
					KBOntology.KB_GENERAL_ONTOLOGY,
					"KB General System Ontology"
			) ;				
		}
		
		OntologyTerm bootstrap_complete =
				ontologyTermService.getOntologyTermByName( kb_ontology, KBOntology.BOOTSTRAPPING_COMPLETE );
		
		if( bootstrap_complete != null ) {
			_logger.info("Yay! The KB database appears to be bootstrapped already! Won't do it again...");
			return ;  // if you see this term, then database is assumed to be bootstrapped!
		}
		
		_logger.info("Initiating bootstrapping of KB database");
		
		/*
		 *  Successful insertion of this ontology term at the end 
		 *  of this initialization method signifies that 
		 *  the bootstrapping of the database is likely complete.
		 */
		ontologyTermService.addOntologyTerm(
				kb_ontology,
				"KB:Bootstrap",
				KBOntology.BOOTSTRAPPING_COMPLETE,
				"If this term can be retrieved without addition at the top of this method, "+
				"then the KB database is assumed to have previously been initialized, "+
				"thus the bootstrap process will be skipped in future application runs."
		);
		
		_logger.info("KB Database bootstrapping complete!");

	}
	
}
