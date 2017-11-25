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

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyContext;
import bio.knowledge.model.core.OntologyTerm;

@NodeEntity(label="Ontology")
public class Neo4jAbstractOntology 
	extends Neo4jAbstractIdentifiedEntity implements Ontology {
	
	public Neo4jAbstractOntology() {
        super();
    }

    public Neo4jAbstractOntology( String source, String name, String description ) {
		super( source, name, description );
        this.context = OntologyContext.ANY_CONTEXT ;
	}

    /**
     */
    private String model = "*";
    

    /**
     */
    private OntologyContext context;
    
    /**
     */
    @Relationship( type="TERM", direction=Relationship.INCOMING )
	private Set<Neo4jAbstractOntologyTerm> terms ;

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#getModel()
	 */
	@Override
	public String getModel() {
        return this.model;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#setModel(java.lang.String)
	 */
	@Override
	public void setModel(String model) {
        this.model = model;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#getContext()
	 */
	@Override
	public OntologyContext getContext() {
        return this.context;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#setContext(bio.knowledge.model.core.OntologyContext)
	 */
	@Override
	public void setContext(OntologyContext context) {
        this.context = context;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#getTerms()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<OntologyTerm> getTerms() {
        return (Set<OntologyTerm>)(Set)terms ;
        
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#setTerms(java.util.Set)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setTerms(Set<OntologyTerm> newTerms) {
		terms = (Set<Neo4jAbstractOntologyTerm>)(Set)newTerms ;
    }
}
