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

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import bio.knowledge.model.core.IdentifiedEntity;

/**
 * @author Chandan Mishra (cmishra@sfu.ca)
 * @author Richard Bruskiewich (Nov. 2016 cache elaboration - direct cache access is now deprecated!)
 * 
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CacheImpl implements Cache {
	
	private final String DEFAULT_PARTITION = "*Default Cache Partition*";

	private Map<String, Map<String,Long>> cacheForDBEntriesCount = 
			new ConcurrentHashMap<String, Map<String,Long>>();

	private Map<String, Map<String,IdentifiedEntity>> cacheForDBEntities = 
			new ConcurrentHashMap<String, Map<String,IdentifiedEntity>>();

	private Map<String, Map<String,List<? extends IdentifiedEntity>>> cacheForDBResults = 
			new ConcurrentHashMap<String,Map<String,List<? extends IdentifiedEntity>>>();

	/*
	 * Constructor called implicitly by Spring in session scope...
	 */
	CacheImpl() {
		// Have to initialize the default partitions covering the legacy API
		Map<String,List<? extends IdentifiedEntity>> defaultResultPartition = 
				new ConcurrentHashMap<String,List<? extends IdentifiedEntity>>();
		cacheForDBResults.put( DEFAULT_PARTITION, defaultResultPartition ) ;
		Map<String,IdentifiedEntity> defaultEntityPartition = 
				new ConcurrentHashMap<String,IdentifiedEntity>();
		cacheForDBEntities.put( DEFAULT_PARTITION, defaultEntityPartition ) ;
		Map<String,Long> defaultCounterPartition = 
				new ConcurrentHashMap<String,Long>();
		cacheForDBEntriesCount.put( DEFAULT_PARTITION, defaultCounterPartition ) ;
	}
	
	/**
	 *  get method to access count cache.
	 */
	public Map<String, Long> getCountCache() {
		return cacheForDBEntriesCount.get(DEFAULT_PARTITION);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.Cache#getEntityCache()
	 */
	@Override
	public Map<String, IdentifiedEntity> getEntityCache() {
		return cacheForDBEntities.get(DEFAULT_PARTITION);
	}
	
	/**
	 *  Method to access database cache.
	 */
	public Map<String, List<? extends IdentifiedEntity>> getResultSetCache() {
		return cacheForDBResults.get(DEFAULT_PARTITION);
	}

	@Override
	public void resetCache() {
		cacheForDBEntriesCount.clear();
		cacheForDBEntities.clear();
		cacheForDBResults.clear();
	}

	// targets default cache only...
	@Override
	public void invalidate(String key) {
		if(cacheForDBEntriesCount.get(DEFAULT_PARTITION).containsKey(key))
			cacheForDBEntriesCount.get(DEFAULT_PARTITION).remove(key);
		if(cacheForDBEntities.get(DEFAULT_PARTITION).containsKey(key))
			cacheForDBEntities.get(DEFAULT_PARTITION).remove(key);
		if(cacheForDBResults.get(DEFAULT_PARTITION).containsKey(key))
			cacheForDBResults.get(DEFAULT_PARTITION).remove(key);
	}

	/*
	 * RMB November 2016 elaboration of the Cache to facilitate management of complex invalidation needs
	 */
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.Cache#searchForCounter(java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public CacheLocation searchForCounter( String nameSpace, String subSpace, String[] keys ) {
		CacheLocation cacheLocation = new CacheLocationImpl( nameSpace, subSpace, keys );
		if(!cacheForDBEntriesCount.containsKey(cacheLocation.getCurrentPartition())) {
			Map<String,Long> newPartition = 
					new ConcurrentHashMap<String,Long>();
			cacheForDBEntriesCount.put( cacheLocation.getCurrentPartition(), newPartition ) ;
		}
		return cacheLocation;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.Cache#searchForEntity(java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public CacheLocation searchForEntity( String nameSpace, String subSpace, String[] keys ) {
		CacheLocation cacheLocation = new CacheLocationImpl( nameSpace, subSpace, keys );
		if(!cacheForDBEntities.containsKey(cacheLocation.getCurrentPartition())) {
			Map<String,IdentifiedEntity> newPartition = 
					new ConcurrentHashMap<String,IdentifiedEntity>();
			cacheForDBEntities.put( cacheLocation.getCurrentPartition(), newPartition ) ;
		}
		return cacheLocation;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.Cache#searchForResultSet(java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public CacheLocation searchForResultSet( String nameSpace, String subSpace, String[] keys) {
		CacheLocation cacheLocation = new CacheLocationImpl( nameSpace, subSpace, keys );
		if(!cacheForDBResults.containsKey(cacheLocation.getCurrentPartition())) {
			Map<String,List<? extends IdentifiedEntity>> newPartition = 
					new ConcurrentHashMap<String,List<? extends IdentifiedEntity>>();
			cacheForDBResults.put( cacheLocation.getCurrentPartition(), newPartition ) ;
		}
		return cacheLocation;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.Cache#invalidate(java.lang.String, java.lang.String)
	 */
	@Override
	public void invalidate(String nameSpace, String subSpace) {
		String partition = nameSpace+"/"+subSpace;
		if(cacheForDBEntities.containsKey(partition))
			cacheForDBEntities.remove(partition);
		if(cacheForDBEntriesCount.containsKey(partition))
			cacheForDBEntriesCount.remove(partition);
		if(cacheForDBResults.containsKey(partition))
			cacheForDBResults.remove(partition);
	}
	
	class CacheLocationImpl implements CacheLocation {
		
		private String currentCachePartition = DEFAULT_PARTITION ;
		private String currentCacheKey = "" ;
		
		/*
		 * Constructor to set current cache location parameters
		 */
		CacheLocationImpl( String nameSpace, String subSpace, String[] keys ) {
			
			String cacheKey = nameSpace+"#";
			for(String key:keys) {
				cacheKey += key;
			}
			currentCachePartition = nameSpace+"/"+subSpace;
			currentCacheKey = Base64.getEncoder().encodeToString(cacheKey.getBytes());
		}
		
		public String getCurrentPartition() { return currentCachePartition; }
		
		public String getCurrentKey() { return currentCachePartition; }

		/* (non-Javadoc)
		 * @see bio.knowledge.service.Cache#setResultSet(java.util.List)
		 */
		@Override
		public void setResultSet( List<? extends IdentifiedEntity> resultSet ) {
			cacheForDBResults.get(currentCachePartition).put(currentCacheKey, resultSet);
		}
		
		/* (non-Javadoc)
		 * @see bio.knowledge.service.Cache#getResultSet()
		 */
		@Override
		public List<? extends IdentifiedEntity> getResultSet() {
			return cacheForDBResults.get(currentCachePartition).get(currentCacheKey);
		}
		
		/* (non-Javadoc)
		 * @see bio.knowledge.service.Cache#getEntity()
		 */
		@Override
		public IdentifiedEntity getEntity() {
			return cacheForDBEntities.get(currentCachePartition).get(currentCacheKey);
		}		
		
		/* (non-Javadoc)
		 * @see bio.knowledge.service.Cache#setEntity(java.util.List)
		 */
		@Override
		public void setEntity( IdentifiedEntity entity ) {
			cacheForDBEntities.get(currentCachePartition).put(currentCacheKey, entity);
		}
		

		/* (non-Javadoc)
		 * @see bio.knowledge.service.Cache#getCount()
		 */
		@Override
		public Long getCounter() {
			return cacheForDBEntriesCount.get(currentCachePartition).get(currentCacheKey);
		}

		/* (non-Javadoc)
		 * @see bio.knowledge.service.Cache#setCount(java.lang.Long)
		 */
		@Override
		public void setCounter(Long count) {
			cacheForDBEntriesCount.get(currentCachePartition).put(currentCacheKey, count);
		}
	}

}
