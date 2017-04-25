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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.core.Neo4jOntologyRepository;
import bio.knowledge.model.core.ModelException;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.neo4j.Neo4jAbstractOntology;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.core.OntologyService;

@Service("OntologyService")
public class Neo4jOntologyService  
	extends IdentifiedEntityServiceImpl<Ontology> 
	implements OntologyService {

    @Autowired
	private Neo4jOntologyRepository ontologyRepository ;

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.String)
	 */
	public Ontology createInstance(Object... args) {
		
		if(args.length==3)
			return new Neo4jAbstractOntology(
							(String)args[0], // Source 'accessionId'
							(String)args[1], // Name
							(String)args[2]	 // Description
						);
		else
			throw new RuntimeException("Invalid OntologyService.createInstance() arguments?") ;
	}
	
    private static Map<String,Ontology> ontologyCatalog = new HashMap<String,Ontology>() ;

    public Ontology getOntologyByName(String ontologyName) throws ModelException {
		Ontology ontology = null ;
		try {
			if(ontologyCatalog.containsKey(ontologyName)) {
				ontology = ontologyCatalog.get(ontologyName) ;
			} else {
				ontology = ontologyRepository.findUniqueByNameEquals(ontologyName) ;
			}
		} catch ( RuntimeException e) {
			e.printStackTrace();
			throw new ModelException("Error retrieving Ontology with name '"+ontologyName+"'?") ;
		}
		
		if( ontology != null && ! ontologyCatalog.containsKey(ontologyName)) {
			ontologyCatalog.put(ontologyName,ontology) ;
		}
    	return ontology ;
    }
    
    public Ontology addOntology(
    		String source, // Ontology Source database stored in the AccessionId field
    		String name, 
    		String description
    ) throws ModelException {
		// Retrieve existing external database record (by name) if it exists, otherwise, create it
    	// Fields: source(ExternalDatabase), name, description, url, version
		Ontology ontology = getOntologyByName( name ) ;
		if( ontology == null ) {
			ontology = new Neo4jAbstractOntology( source, name, description ) ;
			ontology = ontologyRepository.save( (Neo4jAbstractOntology)ontology ) ;
		}
		return ontology ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Ontology> findByNameLike(String filter, Pageable pageable) {
		return (Page<Ontology>)(Page)new PageImpl( ontologyRepository.findByNameLikeIgnoreCase( filter, pageable ));
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Ontology> findAll(Pageable pageable) {
		return (Page<Ontology>)(Page)ontologyRepository.findAll(pageable);
	}

    private List<Ontology> ontology = new ArrayList<Ontology>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
    private Stream<Ontology> getOntologyStream() {
    	List<Ontology> ontology = new ArrayList<Ontology>() ;
    	// 
    	// SDN 4.0 Doesn't appear to instantiate a GraphDatabaseService instance?
    	//
    	// Accessing a collection from the repository 
    	// needs to be explicitly wrapped in a Neo4j Transaction?
    	// see http://stackoverflow.com/questions/11485090/org-neo4j-graphdb-notintransactionexception
    	//try (Transaction tx = graphDb.beginTx()) {
	    	for(Ontology c : ontologyRepository.getOntology()) {
	    		ontology.add(c) ;
	    	}
	    //	tx.success() ;
    	//}
    	return ontology.stream() ;
    }
    
    public List<Ontology> getOntology() {
    	if(ontology.isEmpty()) {
    		ontology = getOntologyStream().sorted().collect(toList()) ;
    	}
    	return ontology ;
    }
    
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<Ontology> getIdentifiers() {
		return getOntology();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Ontology> getIdentifiers(Pageable pageable) {
		return (Page<Ontology>)(Page)ontologyRepository.findAll(pageable);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countEntries()
	 */
	@Override
	public long countEntries() {
		return ontologyRepository.count() ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countHitsByNameLike(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		return ontologyRepository.countByNameLikeIgnoreCase(filter);
	}


}
