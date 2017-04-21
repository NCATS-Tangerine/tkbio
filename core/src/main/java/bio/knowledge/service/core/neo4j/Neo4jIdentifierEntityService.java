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
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.core.Neo4jIdentifierEntityRepository;
import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.core.neo4j.Neo4jAbstractExternalDatabase;
import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;
import bio.knowledge.service.core.IdentifiedEntityService;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;

/**
 * @author Richard
 *
 */
@Service("IdentifierEntityService")
public class Neo4jIdentifierEntityService
	extends IdentifiedEntityServiceImpl<IdentifiedEntity>
	implements IdentifiedEntityService<IdentifiedEntity> {
	
    @Autowired
    Neo4jIdentifierEntityRepository identifierRepository;

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	public IdentifiedEntity createInstance( Object... args ) {
		// Use the Neo4jExternalDatabase class as a 
		// surrogate to create an instance of IdentifiedEntity
		return new Neo4jAbstractExternalDatabase((String)args[0], (String)args[1]);
	}
    
    public List<IdentifiedEntity> testData() {
        // save a couple of identified entities
    	identifierRepository.deleteAll();
    	List<IdentifiedEntity> ids = new ArrayList<IdentifiedEntity> () ;
    	
    	// Use the Neo4jExternalDatabase class as a surrogate to generate IdentifiedEntity
        ids.add( identifierRepository.save( (Neo4jAbstractIdentifiedEntity)createInstance("Johnny Appleseed", "Disseminator of Apples")) );
        ids.add( identifierRepository.save( (Neo4jAbstractIdentifiedEntity)createInstance("Monty Python","Lumberjack")) );
        ids.add( identifierRepository.save( (Neo4jAbstractIdentifiedEntity)createInstance("Amazonia","Queen of the Amazon")) );
        return ids ; 
    }
    
    public void dumpAll() {
        // fetch all identifiers
        System.err.println("\nAll Identified Entities retrieved:");
        System.err.println("-------------------------------");
        identifierRepository.getIdentifiedEntities().forEach(System.err::println) ;
    }
    
    public void findOne(Long id) {
        // fetch an individual identifier by ID
        IdentifiedEntity identifier = identifierRepository.findOne(id);
        System.err.println("\nIdentifier found with findOne("+id.toString()+"):");
        System.err.println("--------------------------------");
        System.err.println(identifier);
        System.err.println();
    }
    
    public void findByName(String name) {
        // fetch identifier by last name
        System.err.println("\nIdentifier found with findByName("+name+"):");
        System.err.println("--------------------------------------------");
        identifierRepository.findByName(name).forEach(System.err::println) ;
    }
    
    private List<IdentifiedEntity> identifiedEntities = new ArrayList<IdentifiedEntity>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
    private Stream<IdentifiedEntity> getIdentifierStream() {
    	List<IdentifiedEntity> identifiedEntities = new ArrayList<IdentifiedEntity>() ;
    	// 
    	// SDN 4.0 Doesn't appear to instantiate a GraphDatabaseService instance?
    	//
    	// Accessing a collection from the repository 
    	// needs to be explicitly wrapped in a Neo4j Transaction?
    	// see http://stackoverflow.com/questions/11485090/org-neo4j-graphdb-notintransactionexception
    	//try (Transaction tx = graphDb.beginTx()) {
	    	for(IdentifiedEntity ie : identifierRepository.getIdentifiedEntities()) {
	    		identifiedEntities.add(ie) ;
	    	}
	    //	tx.success() ;
    	//}
    	return identifiedEntities.stream() ;
    }
    
	public List<IdentifiedEntity> getidentifiedEntities() {
    	if(identifiedEntities.isEmpty()) {
    		identifiedEntities = getIdentifierStream().sorted().collect(toList()) ;
    	}
    	return identifiedEntities ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<IdentifiedEntity> getIdentifiers() {
		return getidentifiedEntities();
	}
    
    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifierService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<IdentifiedEntity> getIdentifiers(Pageable pageable) {
    	return (Page<IdentifiedEntity>)(Page)identifierRepository.findAll(pageable) ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countEntries()
	 */
	@Override
	public long countEntries() {
		return identifierRepository.count();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countHitsByNameLike(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		return identifierRepository.countByNameLikeIgnoreCase(filter);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<IdentifiedEntity> findAll(Pageable pageable) {
		return (Page<IdentifiedEntity>)(Page)identifierRepository.findAll(pageable);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<IdentifiedEntity> findByNameLike(String filter, Pageable pageable) {
		return (Page<IdentifiedEntity>)(Page)new PageImpl( identifierRepository.findByNameLikeIgnoreCase( filter, pageable ));
	}

}
