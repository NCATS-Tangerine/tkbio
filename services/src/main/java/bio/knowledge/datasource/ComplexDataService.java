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

package bio.knowledge.datasource;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import bio.knowledge.model.datasource.ResultSet;

/**
 * @author Richard
 * @version 0.1.0
 */
public interface ComplexDataService extends DataService {
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.datasource.DataService#isSimple()
	 */
	@Override
	default Boolean isSimple() { return false ; }

	/**
	 * Method to document the mandatory input parameters for this complex data service.
	 * 
	 * @return Map of Class-specified expected input parameters data types, keyed by parameter name 
	 */
	Map<String,Class<?>> parameters() ;
	
	/**
	 * Method to document optional input qualifier parameters for this complex data service.
	 * 
	 * @return Map of Class-specified optional input parameters, keyed by qualifier name 
	 */
	Map<String,Class<?>> qualifiers() ;
	
	/**
	 * This method executes a "complex" data service query with map of variable inputs mapped onto a ResultSet output. 
	 * 
	 * Underlying implementations should be thread-safe within the DataService, 
	 * in that there may only be one DataService class instance within a DataSource,
	 * accessed by all system clients, but each user independently run their own 
	 * queries against the DataService, without cross-interference between queries.	 * @param parameters Map of all mandatory and optional named input parameters to the service
	 * @return a CompleteableFuture deferred ResultSet output from a query
	 * @throws DataSourceException if invalid parameters or qualifiers are provided
	 */
	CompletableFuture< ResultSet > query( Map<String,Object> parameters ) throws IllegalArgumentException ;
	
}
