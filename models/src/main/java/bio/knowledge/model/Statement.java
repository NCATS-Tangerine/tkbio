/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-17 Scripps Institute (USA) - Dr. Benjamin Good
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

import java.util.List;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Statement extends IdentifiedEntity, DisplayableStatement, BeaconResponse {
	/**
	 * 
	 * @param subject to be added to the Statement
	 */
	void addSubject(IdentifiedConcept subject);

	/**
	 * @param subjects set to be added with the Statement
	 */
	void setSubjects(List<IdentifiedConcept> subjects);

	/**
	 * @return subjects associated with the Statement
	 */
	List<IdentifiedConcept> getSubjects();

	/**
	 * 
	 * @param subject
	 */
	void setSubject(IdentifiedConcept subject);


	/**
	 * @param predicate the predicate to set
	 */
	void setRelation(Predicate relation);

	/**
	 * 
	 * @param subject to be added to the Statement
	 */
	void addObject(IdentifiedConcept object);

	/**
	 * @param objects set to be added with the Statement
	 */
	void setObjects(List<IdentifiedConcept> objects);

	/**
	 * @return objects associated with the Statement
	 */
	List<IdentifiedConcept> getObjects();

	/**
	 * 
	 * @param object
	 */
	void setObject(IdentifiedConcept object);

	/**
	 * 
	 * @param evidence to be associated with the Statement
	 */
	void setEvidence(Evidence evidence);

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
	 */
	String toString();

	String getName();

}