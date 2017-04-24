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
package bio.knowledge.model.core.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.AnnotatedEntity;
import bio.knowledge.model.core.Feature;

/**
 * Should be abstract, since this class is NOT generally directly instantiated, 
 * but Neo4j doesn't really like abstract classes for @NodeEntity tagged classes
 * @author Richard
 *
 */
@NodeEntity(label="AnnotatedEntity")
public class Neo4jAbstractAnnotatedEntity 
	extends Neo4jAbstractIdentifiedEntity implements AnnotatedEntity {

	public Neo4jAbstractAnnotatedEntity() {
        super();
    }
    
    public Neo4jAbstractAnnotatedEntity(String name) {
    	super(name) ;
    }

    public Neo4jAbstractAnnotatedEntity(String name, String description) {
    	super(name,description) ;
    }
	
    public Neo4jAbstractAnnotatedEntity(String accessionId, String name, String description) {
    	super(accessionId,name,description) ;
    }
	
    @Relationship(type="FEATURE")
    private Set<Neo4jAbstractFeature> features = new HashSet<Neo4jAbstractFeature>() ; ;

    /*
     * (non-Javadoc)
     * @see bio.knowledge.model.core.AnnotatedEntity#setFeatures(java.util.Set)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setFeatures( Set<Feature> newFeatures ) {
		features = (Set<Neo4jAbstractFeature>)(Set)newFeatures ;
    }

    /*
     * (non-Javadoc)
     * @see bio.knowledge.model.core.AnnotatedEntity#getFeatures()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<Feature> getFeatures() {
        return (Set<Feature>)(Set)features;
    }
}
