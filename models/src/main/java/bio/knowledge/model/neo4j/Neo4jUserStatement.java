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

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label="UserStatement")
public class Neo4jUserStatement extends Neo4jAbstractStatement {
	
	String userId;
	
	protected Neo4jUserStatement() {
		super();
	}

	public Neo4jUserStatement(String name, String userId) {
		super(name);
		setUserId(userId);	
	}

	public Neo4jUserStatement(String accessionId, Neo4jPredicate predicate, String userId) {
		super(accessionId, predicate);
		setUserId(userId);	
	}

	public Neo4jUserStatement(String accessionId, Neo4jConcept subject, Neo4jPredicate predicate, Neo4jConcept object, String userId) {
		super(accessionId, subject, predicate, object);
		setUserId(userId);	
	}

	public Neo4jUserStatement(String accessionId, String predicateName, String userId) {
		super(accessionId, predicateName);
		setUserId(userId);	
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
