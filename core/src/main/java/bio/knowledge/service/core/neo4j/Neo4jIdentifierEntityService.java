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

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.core.neo4j.Neo4jAbstractExternalDatabase;
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
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	public IdentifiedEntity createInstance( Object... args ) {
		// Use the Neo4jExternalDatabase class as a 
		// surrogate to create an instance of IdentifiedEntity
		return new Neo4jAbstractExternalDatabase((String)args[0], (String)args[1]);
	}
    
    public List<IdentifiedEntity> testData() {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
    
    public void dumpAll() {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
    
    public void findOne(Long id) {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
    
    public void findByName(String name) {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
    
    private List<IdentifiedEntity> identifiedEntities = new ArrayList<IdentifiedEntity>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
    private Stream<IdentifiedEntity> getIdentifierStream() {
    	throw new NotImplementedException("Removed all reference to neo4j");
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
    	throw new NotImplementedException("Removed all reference to neo4j");
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countEntries()
	 */
	@Override
	public long countEntries() {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countHitsByNameLike(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<IdentifiedEntity> findAll(Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<IdentifiedEntity> findByNameLike(String filter, Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

}
