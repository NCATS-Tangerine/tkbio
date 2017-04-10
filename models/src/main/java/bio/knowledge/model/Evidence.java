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

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity;

/**
 * @author Richard
 *
 * September 16, 2016 revision:
 * 
 * The former SemMedDb "Evidence" class was generalized to track 
 * a more diverse range of 'Annotation'
 *
 */
@NodeEntity(label="Evidence")
public class Evidence extends Neo4jIdentifiedEntity {
	
	@Relationship( type="EVIDENCE", direction=Relationship.INCOMING )
	private Statement statement ;
	
	@Relationship( type="ANNOTATION" )
    private Set<Annotation> annotations = new HashSet<Annotation>() ;

	private Integer count = 0;

	public Evidence() {}

	/**
	 * @return
	 */
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	
	/**
	 * @return
	 */
	public Statement getStatement() {
		return statement;
	}
	
	/**
	 * 
	 * @param annotations
	 */
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations.addAll(annotations);
	}

	/**
	 * 
	 * @param annotation
	 */
	public void addAnnotation(Annotation annotation) {
		this.annotations.add(annotation);
		incrementCount();
	}

	/**
	 * 
	 * @return
	 */
	public Set<Annotation> getAnnotations() {
		return annotations;
	}
	
	/**
	 * @param count of number of Annotations in Evidence
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @param increment count of number of Annotations in Evidence
	 */
	public void incrementCount() {
		this.count += 1;
	}

	/**
	 * @return the 'count' of the Evidence Annotations (i.e. number of independent pieces of Evidence)
	 */
	public Integer getCount() {
		if(count==null)
			count = annotations.size() ;
		return count;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
	 */
	@Override
	public String toString() {
		/*
		 *  The String representation of Evidence is simply the count of Annotations
		 */
		return getCount().toString() ;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(IdentifiedEntity other) {
		return getCount().compareTo(((Evidence)other).getCount());
	}
	
}
