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

import java.util.Optional;
import java.util.Set;

import bio.knowledge.model.SemanticGroup;

/**
 * @author Richard
 * @version 0.1.0
 */
public interface DataSource extends Described {
	
	/**
	 * @return Set of target concept types against which a query may be applied to the DataSource
	 */
	Set<SemanticGroup> getTargetSemanticGroups() ;
	
	/**
	 * 
	 * @return Set of all available DataServices from this DataSource
	 */
	Set<DataService> getAllServices() ;
	
	/**
	 * 
	 * @param type SemanticGroup for which DataServices are of interest
	 * @return Set of all Data Services exposed by the Data Source for a given target concept type (may be empty)
	 */
	Set<DataService> getServicesBySemanticGroup( SemanticGroup type ) ;

	/**
	 * 
	 * @param identifier of service
	 * @return Optional<DataSource> with given identifier (may be empty if unknown)
	 */
	Optional<DataService> getServiceByIdentifier( String identifier ) ;
	
}
