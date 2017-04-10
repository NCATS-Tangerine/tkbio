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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Richard
 *
 */
public enum SemanticGroup {
	ANY(
			"Any Semantic Type",
			new String[]{}
	), 
	ACTI(
			"Activities & Behaviors",
			new String[]{}
	), 
	ANAT(
			"Anatomy",
			new String[]{}
	), 
	CHEM(
			"Chemicals & Drugs",
			new String[]{}
	), 
	CONC(
			"Concepts & Ideas",
			new String[]{}
	), 
	DEVI(
			"Devices",
			new String[]{}
			), 
	DISO(
			"Disorders",
			new String[]{}
	), 
	GENE(
			"Genes & Molecular Sequences",
			new String[]{
					"Q7187",    // gene
					"Q8054",    // protein
					"Q898273",  //protein domain
					"Q20747295" // protein encoding gene
			}
	), 
	GEOG(
			"Geographic Areas",
			new String[]{}
	), 
	LIVB(
			"Living Beings",
			new String[]{}
	), 
	OBJC(
			"Objects",
			new String[]{}
	), 
	OCCU(
			"Occupations",
			new String[]{}
	), 
	ORGA(
			"Organizations",
			new String[]{
					"Q930752" // medical specialty
			}
	), 
	PHEN(
			"Phenomena",
			new String[]{}
	), 
	PHYS(
			"Physiology",
			new String[]{}
	), 
	PROC(
			"Procedures",
			new String[]{}
	)
	;
	
	private static Logger _logger = LoggerFactory.getLogger(SemanticGroup.class);
	
	private String description ;
	
	/*
	 *  wikiClasses is the the set of  WikiItem id's 
	 *  of all the values of subclass_of or instance_of properties
	 *  that map onto the given SemanticGroup
	 */
	private Set<String> wikiClasses ;

	private SemanticGroup(String description, String[] wikiClasses) {
		this.description = description ;
		this.wikiClasses = new HashSet<String>(Arrays.asList(wikiClasses));
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description ;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return description;
	}
	
    public static SemanticGroup lookUpByDescription(String description) {
    	for(SemanticGroup group: SemanticGroup.values()) {
    		if(group.getDescription().equals(description))
    			return group ;
    	}
    	return null;
    }
	
    public static SemanticGroup lookUpByWikiClass(String wikiClass) {
    	_logger.debug("Entering SemanticGroup lookUpByWikiClass("+wikiClass+")");
    	for(SemanticGroup group: SemanticGroup.values()) {
    		if(group.wikiClasses.contains(wikiClass))
    			return group ;
    	}
    	return null;
    }
}
