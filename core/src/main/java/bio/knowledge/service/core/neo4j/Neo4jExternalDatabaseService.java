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

import bio.knowledge.database.repository.core.Neo4jExternalDatabaseRepository;
import bio.knowledge.model.core.ExternalDatabase;
import bio.knowledge.model.core.ModelException;
import bio.knowledge.model.core.neo4j.Neo4jAbstractExternalDatabase;
import bio.knowledge.service.core.ExternalDatabaseService;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;

@Service("ExternalDatabaseService")
public class Neo4jExternalDatabaseService
	extends IdentifiedEntityServiceImpl<ExternalDatabase> 
	implements ExternalDatabaseService {
	
    @Autowired
	private Neo4jExternalDatabaseRepository databaseRepository ;

    private static Map<String,ExternalDatabase> databaseCatalog = new HashMap<String,ExternalDatabase>() ;

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	public ExternalDatabase createInstance(Object... args) {
		
		if(args.length==1)
			return new Neo4jAbstractExternalDatabase((String)args[0]); // Name
		
		else if(args.length==2)
			return new Neo4jAbstractExternalDatabase(
							(String)args[0], // Name
							(String)args[1]  // Description
						);
		
		else if(args.length==3)
			return new Neo4jAbstractExternalDatabase(
							(String)args[0], // Name
							(String)args[1], // Description
							(String)args[2]	 // URL
						);
		else
			throw new RuntimeException("Invalid ExternalDatabaseService.createInstance() arguments?") ;
	}
	
    /*
     * (non-Javadoc)
     * @see bio.knowledge.service.core.ExternalDatabaseService#getDatabaseByName(java.lang.String)
     */
    @Override
	public ExternalDatabase getDatabaseByName(String databaseName) throws ModelException {
    	ExternalDatabase database = null ;
		try {
			if(databaseCatalog.containsKey(databaseName)) {
				database = databaseCatalog.get(databaseName) ;
			} else {
				database = databaseRepository.findUniqueByNameEquals(databaseName) ;
			}
		} catch ( RuntimeException e) {
			throw new ModelException("Error retrieving database with name '"+databaseName+"': "+e.getMessage()) ;
		}
		if( database!=null && !databaseCatalog.containsKey(databaseName) ) {
			databaseCatalog.put(databaseName,database) ;
		}
    	return database ;
    }
    
    /*
     * (non-Javadoc)
     * @see bio.knowledge.service.core.ExternalDatabaseService#addDatabase(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
	public ExternalDatabase addDatabase(
    		String name, 
    		String description, 
    		String url
    ) throws ModelException {
		// Retrieve existing External Database (by name) if it exists, otherwise, create it
    	// Fields: source(ExternalDatabase), name, description, url, version
    	ExternalDatabase database = getDatabaseByName( name ) ;
		if( database == null ) {
			database = new Neo4jAbstractExternalDatabase( name, description, url ) ;
			database = databaseRepository.save( (Neo4jAbstractExternalDatabase)database ) ;
		}
		if(!databaseCatalog.containsKey( name )) {
			databaseCatalog.put( name , database ) ;
		}
		return database ;
    }

    private List<ExternalDatabase> databases = new ArrayList<ExternalDatabase>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
    private Stream<ExternalDatabase> getDatabaseStream() {
    	List<ExternalDatabase> databases = new ArrayList<ExternalDatabase>() ;
    	// 
    	// SDN 4.0 Doesn't appear to instantiate a GraphDatabaseService instance?
    	//
    	// Accessing a collection from the repository 
    	// needs to be explicitly wrapped in a Neo4j Transaction?
    	// see http://stackoverflow.com/questions/11485090/org-neo4j-graphdb-notintransactionexception
    	//try (Transaction tx = graphDb.beginTx()) {
	    	for(ExternalDatabase d : databaseRepository.getExternalDatabases()) {
	    		databases.add(d) ;
	    	}
	    //	tx.success() ;
    	//}
    	return databases.stream() ;
    }
    
    public List<ExternalDatabase> getExternalDatabases() {
    	if(databases.isEmpty()) {
    		databases = getDatabaseStream().sorted().collect(toList()) ;
    	}
    	return databases ;
    }
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<ExternalDatabase> getIdentifiers() {
		return getExternalDatabases();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<ExternalDatabase> getIdentifiers(Pageable pageable) {
		return (Page<ExternalDatabase>)(Page)databaseRepository.findAll(pageable);
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countEntries()
	 */
	@Override
	public long countEntries() {
		return databaseRepository.count();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countHitsByNameLike(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		return databaseRepository.countByNameLikeIgnoreCase(filter);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<ExternalDatabase> findAll(Pageable pageable) {
		return (Page<ExternalDatabase>)(Page)databaseRepository.findAll(pageable);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<ExternalDatabase> findByNameLike(String filter, Pageable pageable) {
		return (Page<ExternalDatabase>)(Page)new PageImpl( databaseRepository.findByNameLikeIgnoreCase( filter, pageable ) );
	}

}
