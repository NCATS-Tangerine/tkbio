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

import bio.knowledge.model.Concept;
import bio.knowledge.model.Predicate;

/**
 * @author Richard
 * 
 * Reified node for statements of conceptual relations, which are now inspired
 * by the notion of WikiData RDF statement triples, embellished with associated Evidence
 *
 */
@NodeEntity(label="Statement")
public class Neo4jGeneralStatement extends Neo4jAbstractStatement {
	
	protected Neo4jGeneralStatement() {
		super();
	}

	public Neo4jGeneralStatement(String name){
		super(name);
	}
	
	public Neo4jGeneralStatement(
    		String accessionId,
    		Predicate predicate
    ) {
    	super(accessionId, predicate);
    }
	   
	public Neo4jGeneralStatement(
    		String accessionId,
    		Concept subject,
    		Predicate predicate,
    		Concept object
    ) {
    	super(accessionId, subject, predicate, object) ;
    }

	public Neo4jGeneralStatement(
    		String accessionId,
    		String predicateName
    ) {
    	super(accessionId,predicateName) ;
    }
	
}
