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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.core.Neo4jOntologyTermRepository;
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
    
    @Autowired
	private Neo4jOntologyTermRepository ontologyTermRepository ;

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
		
    	if(accessionId==null || accessionId.isEmpty())
    		throw new ModelException("Null or empty accessionId argument given to getOntologyTermByAccessionId()?!") ;
		
		return (OntologyTerm)ontologyTermRepository.findByAccessionIdEquals( accessionId );
	}

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.OntologyTermService#getOntologyTermByName(bio.knowledge.model.core.Ontology, java.lang.String)
	 */
    @Override
	public OntologyTerm getOntologyTermByName( Ontology ontology, String termName ) throws ModelException {
    	
    	if(ontology==null)
    		throw new ModelException("Null ontology argument given to getOntologyTermByName() - don't know which ontology to search!") ;
    	
    	String ontologyName = ontology.getName() ;
    	
		OntologyTerm term = null ;
		
		try {
		
			Map<String,OntologyTerm> termCatalog = ontologyMap.get(ontologyName) ;
			
			if( termCatalog != null && termCatalog.containsKey(termName) )
				
				term = termCatalog.get(termName) ;
				
			else {
				// Ontology ontology is assumed in 
				// this Neo4j application to be a Neo4jOntology
				term = ontologyTermRepository.
						   findByOntologyAndNameEquals( (Neo4jAbstractOntology)ontology, termName ) ;
				
				// Cache the term if found...
				if( term != null ) {
					if( termCatalog == null ) {
						termCatalog = new HashMap<String,OntologyTerm>() ;
						ontologyMap.put( ontologyName, termCatalog ) ; 
					}
					termCatalog.put(termName,term) ;
				}
			}

		} catch ( RuntimeException e) {
			e.printStackTrace();
			throw new ModelException("Severe error encountered while retrieving term with name '"+termName+"' from ontology '"+ontologyName+"'?") ;
		}
    	return term ; // may be null if not found
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
    	OntologyTerm term = null ;
    	try {
    		term = ontologyTermRepository.findOntologyTermByNameEquals( termName ) ;
		} catch ( IncorrectResultSizeDataAccessException irsdae) {
			irsdae.printStackTrace();
			throw new ModelException("Term name '"+termName+"' not globally unique?") ;
		} catch ( RuntimeException rte ){
			rte.printStackTrace();
			throw new ModelException("Severe error encountered while retrieving term with presumed globally unique name '"+termName+"'?") ;
		}
    	return term ;
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
    	String ontologyName = ontology.getName() ;
    	
    	// Cache the ontology terms in a local catalog
    	Map<String,OntologyTerm> termCatalog = null ;
		if( ontologyMap.containsKey(ontologyName) ) {
			termCatalog = ontologyMap.get(ontologyName) ;
		} else {
			termCatalog = new HashMap<String,OntologyTerm>() ;
			ontologyMap.put( ontologyName, termCatalog ) ; 
		}
		
		// Retrieve existing external ontology term 
		// (by name) if it exists, otherwise, create and cache it
		OntologyTerm ontologyTerm = getOntologyTermByAccessionId( accessionId ) ;
		if( ontologyTerm == null ) {
			ontologyTerm = new Neo4jAbstractOntologyTerm( ontology, termName, definition ) ;
			ontologyTerm = ontologyTermRepository.save( (Neo4jAbstractOntologyTerm)ontologyTerm ) ;
			termCatalog.put(termName, ontologyTerm) ;
		}
		return ontologyTerm ;
    }
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#findAll(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<OntologyTerm> findByNameLike( String filter, Pageable pageable ){
		return (Page<OntologyTerm>)(Page)new PageImpl( ontologyTermRepository.findByNameLikeIgnoreCase( filter, pageable )) ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#findAll(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<OntologyTerm> findAll(Pageable pageable) {
		return (Page<OntologyTerm>)(Page)ontologyTermRepository.findAll(pageable);
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#countEntries()
	 */
	@Override
	public long countEntries() {
		return ontologyTermRepository.count() ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#countHits(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		return ontologyTermRepository.countByNameLikeIgnoreCase(filter);
	}

    private List<OntologyTerm> terms = new ArrayList<OntologyTerm>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
    private Stream<OntologyTerm> getTermStream() {
    	List<OntologyTerm> terms = new ArrayList<OntologyTerm>() ;
    	// 
    	// SDN 4.0 Doesn't appear to instantiate a GraphDatabaseService instance?
    	//
    	// Accessing a collection from the repository 
    	// needs to be explicitly wrapped in a Neo4j Transaction?
    	// see http://stackoverflow.com/questions/11485090/org-neo4j-graphdb-notintransactionexception
    	//try (Transaction tx = graphDb.beginTx()) {
	    	for(OntologyTerm c : ontologyTermRepository.getOntologyTerms()) {
	    		terms.add(c) ;
	    	}
	    //	tx.success() ;
    	//}
    	return terms.stream() ;
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
		return (Page<OntologyTerm>)(Page)ontologyTermRepository.findAll( pageable ) ;
	}

}
