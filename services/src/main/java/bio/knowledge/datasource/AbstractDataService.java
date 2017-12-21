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

import bio.knowledge.model.ConceptType;

/**
 * @author Richard
 *
 */
public abstract class AbstractDataService
	extends AbstractDescribed implements DataService {
	
	private DataSource dataSource = null ;
	private ConceptType targetSemanticGroup ;
	
	/**
	 * 
	 * @param dataSource
	 * @param serviceId
	 */
	protected AbstractDataService( 
			DataSource dataSource, 
			String serviceId, 
			ConceptType targetSemanticGroup, 
			String name
	) {
		super( serviceId, name ) ;
		this.dataSource = dataSource ;
		this.targetSemanticGroup = targetSemanticGroup ;
	}
	
	/**
	 * 
	 * @param dataSource
	 * @param serviceId
	 */
	protected AbstractDataService(  
			DataSource dataSource, 
			String serviceId, 
			ConceptType targetSemanticGroup
	) {
		this( dataSource, serviceId, targetSemanticGroup, "" ) ;
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.datasource.DataService#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.DataService#getTargetSemanticGroup()
	 */
	@Override
	public ConceptType getTargetSemanticGroup() {
		return targetSemanticGroup;
	}
	
	
}
