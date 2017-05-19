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
package bio.knowledge.service.core.neo4j;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.model.core.ModelException;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyTerm;
import bio.knowledge.model.core.neo4j.Neo4jAbstractOntology;
import bio.knowledge.model.core.neo4j.Neo4jAbstractOntologyTerm;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.core.OntologyTermService;

@Service("OntologyTermService")
public class Neo4jOntologyTermService  
	extends IdentifiedEntityServiceImpl<OntologyTerm>
	implements OntologyTermService {

    @Autowired
	private Neo4jOntologyService ontologyService ;

    private static Map<String, Map<String,OntologyTerm>> ontologyMap = 
    								new HashMap<String,Map<String,OntologyTerm>>() ;

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	public OntologyTerm createInstance(Object... args) {
		
		if(args.length==2)
			return new Neo4jAbstractOntologyTerm(
							(Ontology)args[0], 
							(String)args[1] // Name
						);
		
		else if(args.length==3)
			return new Neo4jAbstractOntologyTerm(
							(Ontology)args[0],
							(String)args[1], // Name
							(String)args[2]	 // Description
						);
		else if(args.length==4)
			return new Neo4jAbstractOntologyTerm(
							(Ontology)args[0],
							(String)args[1], // Accession Id
							(String)args[2], // Name
							(String)args[3]	 // Description
						);
		else
			throw new RuntimeException("Invalid OntologyTermService.createInstance() arguments?") ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.OntologyTermService#getOntologyTermByAccessionId(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTermByAccessionId( String accessionId ) throws ModelException {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.OntologyTermService#getOntologyTermByName(bio.knowledge.model.core.Ontology, java.lang.String)
	 */
    @Override
	public OntologyTerm getOntologyTermByName( Ontology ontology, String termName ) throws ModelException {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.OntologyTermService#getOntologyTermByName(java.lang.String, java.lang.String)
	 */
    @Override
	public OntologyTerm getOntologyTermByName( String ontologyName, String termName) throws ModelException {
    	Ontology ontology = ontologyService.getOntologyByName(ontologyName) ;
    	return getOntologyTermByName( ontology, termName ) ;
    }

    // This variant of the ontology term search assumes globally unique term names
    // for example, unique Feature tag names, scientific data set types, etc.
    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.OntologyTermService#getOntologyTermByName(java.lang.String)
	 */
    @Override
	public OntologyTerm getOntologyTermByName( String termName ) throws ModelException {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.OntologyTermService#addOntologyTerm(bio.knowledge.model.core.Ontology, java.lang.String, java.lang.String)
	 */
    @Override
	public OntologyTerm addOntologyTerm(
    		Ontology ontology,
    		String accessionId, // obligatory field for unique indexing of ontology terms now!
    		String termName, 
    		String definition
    ) throws ModelException {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#findAll(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<OntologyTerm> findByNameLike( String filter, Pageable pageable ){
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#findAll(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<OntologyTerm> findAll(Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#countEntries()
	 */
	@Override
	public long countEntries() {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#countHits(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

    private List<OntologyTerm> terms = new ArrayList<OntologyTerm>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
    private Stream<OntologyTerm> getTermStream() {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
    
    public List<OntologyTerm> getOntologyTerms() {
    	if(terms.isEmpty()) {
    		terms = getTermStream().sorted().collect(toList()) ;
    	}
    	return terms ;
    }
    
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<OntologyTerm> getIdentifiers() {
		return getOntologyTerms() ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<OntologyTerm> getIdentifiers(Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

}
