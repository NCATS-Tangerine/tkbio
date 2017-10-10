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

import java.util.List;
import java.util.Map;

import bio.knowledge.model.core.IdentifiedEntity;

/**
 * @author Chandan Mishra (cmishra@sfu.ca)
 * @author Richard Bruskiewich (Nov. 2016 cache elaboration)
 */
public interface Cache {
	
	/**
	 * Method to access query count cache
	 */
	@Deprecated
	public Map<String, Long> getCountCache() ;
	
	/**
	 * Method to access query single IdentifiedEntity object cache
	 */
	@Deprecated
	public Map<String, IdentifiedEntity> getEntityCache() ;
	
	/**
	 * Method to access query result cache
	 */
	@Deprecated
	public Map<String, List<? extends IdentifiedEntity>> getResultSetCache() ;
	
	/**
	 * This will reset cache for this session.
	 * 
	 */
	public void resetCache();

	/**
	 * This will invalidate entry for given key.
	 * @param key
	 */
	public void invalidate(String key);
	
	/*
	 * RMB: Nov 28, 2016 Cache redesign: to facilitate invalidation 
	 * of cache contents based on partitioning of the space of 
	 * results by name space and subspace indexing, 
	 * we are encapsulating the cache key computation and 
	 * cache access inside a new set of methods.
	 */
	
	/**
	 * This method searches the ResultSet cache for a specific indexed location 
	 * partitioned under the specified 'primary' key, with a cache key 
	 * computed from a specified String array of keys provided
	 * 
	 * @param nameSpace String identifier of currently active cache name space
	 * @param subspace  String identifier of currently active name subspace within currently active cache name space
	 * @param keys String array used to generate the unique cache key
	 */
	public CacheLocation searchForResultSet( String nameSpace, String subSpace, String[] keys );
	
	/**
	 * This method searches the Identified Entity object cache for a specific indexed location 
	 * partitioned under the specified 'primary' key, with a cache key 
	 * computed from a specified String array of keys provided
	 * 
	 * @param nameSpace String identifier of currently active cache name space
	 * @param subspace  String identifier of currently active name subspace within currently active cache name space
	 * @param keys String array used to generate the unique cache key
	 */
	public CacheLocation searchForEntity( String nameSpace, String subSpace, String[] keys );

	
	/**
	 * This method searches the numeric counter cache for a specific indexed location 
	 * partitioned under the specified 'primary' key, with a cache key 
	 * computed from a specified String array of keys provided
	 * 
	 * @param nameSpace String identifier of currently active cache name space
	 * @param subspace  String identifier of currently active name subspace within currently active cache name space
	 * @param keys String array used to generate the unique cache key
	 */
	public CacheLocation searchForCounter( String nameSpace, String subSpace, String[] keys );
	
	/**
	 * This will invalidates all entries in all cache partitions 
	 * associated with the specified name space and subspace
	 * 
	 * @param nameSpace String identifier of the cache name space targeted for invalidation
	 * @param subspace  String identifier of the subspace targeted for invalidation within the name space
	 */
	public void invalidate( String nameSpace, String subSpace );
	
	/**
	 * 
	 * @author Richard
	 *
	 */
	public interface CacheLocation {
		
		/**
		 * 
		 * @return Current Cache Partition
		 */
		public String getCurrentPartition() ;
		
		/**
		 * 
		 * @return Current Cache Key Location 
		 */
		public String getCurrentKey() ;

		/**
		 * After calling 'searchForResult()', this method may be used to cache 
		 * a List of results in the currently identified cache location
		 * 
		 * @return 
		 */
		public void setResultSet( List<? extends IdentifiedEntity> resultSet );
		
		/**
		 * After calling 'searchForResult()', this method may be used to access any cached List of results
		 * @return List of data objects in the cache, in the current 'searchCache' indexed location
		 */
		public List<? extends IdentifiedEntity> getResultSet();
		
		/**
		 * After calling 'searchForEntity()', this method may be used to cache 
		 * a List of results in the currently identified cache location
		 * 
		 * @return 
		 */
		public void setEntity( IdentifiedEntity entity );
		
		/**
		 * After calling 'searchForEntity()', this method may be used to access any cached List of results
		 * @return List of data objects in the cache, in the current 'searchCache' indexed location
		 */
		public IdentifiedEntity getEntity();
		
		/**
		 * After calling 'searchForCounter()', this method may be used to access a specific cached counter value
		 * @return Long counter value, in the current 'searchCache' indexed location
		 */
		public Long getCounter();
		
		/**
		 * After calling 'searchForCounter()', this method may be used to cache 
		 * a Long counter value in the currently identified cache location
		 * 
		 * @return 
		 */
		public void setCounter( Long count );
	}

}
