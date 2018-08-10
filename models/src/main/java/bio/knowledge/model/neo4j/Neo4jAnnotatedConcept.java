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
package bio.knowledge.model.neo4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;

import bio.knowledge.model.AnnotatedConcept;
import bio.knowledge.model.ConceptType;

/**
 * @author Richard Bruskiewich
 * 
 * Concept is a fundamental currency in Knowledge.Bio, 
 * representing the unit of semantic representation of globally distinct ideas.
 * 
 */
@NodeEntity(label="AnnotatedConcept")
public class Neo4jAnnotatedConcept extends Neo4jIdentifiedConceptImpl implements AnnotatedConcept {

	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;

	private String beaconSource = "";

	private Set<String> dbLinks = new HashSet<String>() ;

	protected Neo4jAnnotatedConcept() {	super() ; }

	/**
	 * 
	 * @param clique
	 * @param name
	 * @param type
	 * @param taxon
	 */
	public Neo4jAnnotatedConcept(  String clique, String name, List<String> categories, String taxon ) {
		super(clique, name, categories, taxon);
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.Concept#setBeaconSource(java.lang.String)
	 */
	@Override
	public void setBeaconSource(String source) {
		this.beaconSource = source;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.BeaconResponse#getBeaconSource()
	 */
	@Override
	public String getBeaconSource() {
		return beaconSource;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getCrossReferences()
	 */
	@Override
	public Set<String> getAliases() {
		return dbLinks ;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#toString()
	 */
	@Override
	public String toString() {
		return getName() ;
	}

	private String taxon = "";

	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#setTaxon(String)
	 */
	@Override
	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#getTaxon()
	 */
	@Override
	public String getTaxon() {
		return taxon;
	}

	private List<BeaconEntry> entries = new ArrayList<BeaconEntry>();
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.AnnotatedConcept#getEntries()
	 */
	@Override
	public List<BeaconEntry> getEntries() {
		return entries;
	}
}
