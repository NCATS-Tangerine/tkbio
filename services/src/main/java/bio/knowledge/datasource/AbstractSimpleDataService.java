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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import bio.knowledge.model.SemanticGroup;

/**
 * @author Richard
 *
 */
public abstract class AbstractSimpleDataService<T>
	extends AbstractDataService implements SimpleDataService<T> {

	/**
	 * @param dataSource
	 */
	protected AbstractSimpleDataService( 
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
	protected AbstractSimpleDataService( 
			DataSource dataSource, 
			String serviceId,
			SemanticGroup targetConceptType
	) {
		this( dataSource, serviceId, targetConceptType, "" );
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.SimpleDataService#getInputType()
	 */
	@Override
	public Class<T> getInputType() {
		Class<?> clazz = this.getClass() ;
		Type genericSuperclazz = clazz.getGenericSuperclass() ;
		ParameterizedType pType = (ParameterizedType)genericSuperclazz ;
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>)pType.getActualTypeArguments()[0];
		return type ;
	}
}
