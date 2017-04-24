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

import java.util.HashMap;
import java.util.Map;

import bio.knowledge.model.SemanticGroup;

/**
 * @author Richard
 *
 */
public abstract class AbstractComplexDataService 
	extends AbstractDataService implements ComplexDataService {

	/**
	 * @param dataSource
	 */
	protected AbstractComplexDataService( 
			DataSource dataSource, 
			String serviceId,
			SemanticGroup targetConceptType, 
			String name 
	) {
		super( dataSource, serviceId, targetConceptType, name );
	}

	/**
	 * @param dataSource
	 */
	protected AbstractComplexDataService( 
			DataSource dataSource, 
			String serviceId,
			SemanticGroup targetConceptType 
	) {
		this( dataSource, serviceId, targetConceptType, "");
	}

	private Map<String, Class<?>> parameters = new HashMap<String, Class<?>>() ;
	private Map<String, Class<?>> qualifiers = new HashMap<String, Class<?>>() ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.ComplexDataService#parameters()
	 */
	@Override
	public Map<String, Class<?>> parameters() {
		return new HashMap<String, Class<?>>(parameters) ;
	}
	
	/**
	 * 
	 * @param key
	 * @param type
	 */
	protected void addParameter( String key, Class<?> type ) {
		parameters.put(key, type) ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.ComplexDataService#qualifiers()
	 */
	@Override
	public Map<String, Class<?>> qualifiers() {
		return new HashMap<String, Class<?>>(qualifiers) ;
	}
	
	/**
	 * 
	 * @param key
	 * @param type
	 */
	protected void addQualifier( String key, Class<?> type ) {
		qualifiers.put(key, type) ;
	}

}
