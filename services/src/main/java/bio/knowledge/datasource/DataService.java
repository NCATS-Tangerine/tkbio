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

import java.util.concurrent.TimeUnit;

import bio.knowledge.model.SemanticGroup;

/**
 * @author Richard
 * @version 0.2.0
 */
public interface DataService extends Described {
	
	// time out variables
	public final long TIMEOUT_DURATION = 1;
	public final TimeUnit TIMEOUT_UNIT = TimeUnit.MINUTES;

	/**
	 * 
	 * @return DataSource to which the DataService belongs
	 */
	DataSource getDataSource() ;
	
	/**
	 * 
	 * @return boolean flag: true, if simple data source, false if complex
	 */
	Boolean isSimple() ;
	
	/**
	 * @return the target SemanticGroup against which a query may be applied to this Data Service
	 */
	SemanticGroup getTargetSemanticGroup() ;
}
