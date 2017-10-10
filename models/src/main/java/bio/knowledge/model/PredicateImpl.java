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
package bio.knowledge.model;

import bio.knowledge.model.Predicate;
import bio.knowledge.model.core.AbstractIdentifiedEntity;

/**
 * @author Richard
 *
 */
public class PredicateImpl extends AbstractIdentifiedEntity implements Predicate {
	
	public PredicateImpl() { }
	
	public PredicateImpl( String name ) {
		super(name) ;
	}
	
	public PredicateImpl( String name, String description ) {
		super(name,description) ;
	}
	
	public PredicateImpl( String predicateId, String name, String description ) {
		super( predicateId, name, description ) ;
	}
	
	
	private String beaconSource = "";

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.Predicate#setBeaconSource(java.lang.String)
	 */
	@Override
	public void setBeaconSource(String beaconSource) {
		this.beaconSource = beaconSource;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.Predicate#getBeaconSource()
	 */
	@Override
	public String getBeaconSource() {
		return beaconSource;
	}

}
