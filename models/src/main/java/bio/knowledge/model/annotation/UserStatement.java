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
package bio.knowledge.model.annotation;

import bio.knowledge.model.Evidence;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.model.neo4j.Neo4jEvidence;
import bio.knowledge.model.neo4j.Neo4jGeneralStatement;
import bio.knowledge.model.neo4j.Neo4jPredicate;

public class UserStatement extends Neo4jGeneralStatement {

	private String description;
	private String uriEvidence;

	public UserStatement(String statementId, Neo4jConcept subject, Neo4jPredicate predicate,
			Neo4jConcept object, String description, String uriEvidence) {
		super(statementId, subject, predicate, object);
		setSubject(subject);
		setObject(object);
		this.description = description;
		this.uriEvidence = uriEvidence;
    	this.evidence = new Neo4jEvidence();
	}
	
//	public UserAnnotation(String predicationLabel, String sourceId, String targetId, String description, String uriEvidence) {
//		super(predicationId, 
//			Type.ANNOTATION, 
//			subject, 
//			predicate, 
//			object);
//		setSubject();
//		setObject();
//		this.description = description;
//		this.uriEvidence = uriEvidence;
//    	this.evidence = new Evidence();
//	}
	
	public String getAnnotationDescription() {
		return this.description;
	}

	public void setAnnotationDescription(String description) {
		this.description = description;
	}
	
	public String getUriEvidence() {
		return this.uriEvidence;
	}
	
	public void setUriEvidence(String uriEvidence) {
		this.uriEvidence = uriEvidence;
	}
	
	public Evidence getEvidence() {
		return this.evidence;
	}
	
	public void setEvidence(Neo4jEvidence evidence) {
		this.evidence = evidence;
	}
	
}
