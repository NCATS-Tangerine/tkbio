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

package bio.knowledge.model.datasource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Richard
 *
 */
public class SimpleResultSet extends ArrayList<Result> implements ResultSet {

	private static final long serialVersionUID = -368830976735888356L;
	
	/*
	 *  We dynamically poll the Result hits in
	 *  the ResultSet for their Map keySets.
	 *  We cache this key set for performance sake, 
	 *  assuming that no additional Results with 
	 *  new key values are added to the ResultSet
	 *  after the 'keySet' method (below) is called
	 *  the first time...
	 */
	private Set<String> keys = new HashSet<String>() ;

	/* (non-Javadoc)
	 * @see bio.knowledge.model.datasource.ResultSet#keySet()
	 */
	@Override
	public Set<String> keySet() {
		if(keys.isEmpty())
			for(Result rs : this) {
				keys.addAll(rs.keySet()) ;
			}
		return new HashSet<String>(keys);
	}

}
