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

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;

import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyTerm;

@NodeEntity(label="OntologyTerm")
public class Neo4jAbstractOntologyTerm 
	extends Neo4jAbstractIdentifiedEntity implements OntologyTerm {

    /**
     */
	@Relationship( type="ONTOLOGY" )
    private Neo4jAbstractOntology ontology;

    @Transient
    private String ontologyName;

    /* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getOntologyName()
	 */
    @Override
	public String getOntologyName() {
        if (ontologyName == null) 
        	return ""; 
        else 
        	return ontology.getName();
    }

    /**
     */
    @Value("#{ T(java.lang.Boolean).FALSE }")
    private Boolean isObsolete;

    /**
     */
    @Value("#{ T(java.lang.Boolean).FALSE }")
    private Boolean isRelationship;

	public Neo4jAbstractOntologyTerm() {
        super();
    }

	/**
	 * Constructor accepting ontology and name.
	 * Constructs an accessionId from the ontology AccessionId and provided Name
	 * @param ontology
	 * @param name
	 */
    public Neo4jAbstractOntologyTerm( Ontology ontology, String name ) {
        super(name);
        setOntology( ontology );
        String ns = ontology.getId() ;
        if(ns==null) ns = "" ;
        setId(ns+":"+name) ;
    }
    
    /**
     * Constructor as above, except adds a term definition
     * @param ontology
     * @param name
     * @param definition
     */
    public Neo4jAbstractOntologyTerm(  Ontology ontology, String name, String definition ) {
        this(ontology,name);
        setDescription(definition) ;
    }
    
    /**
     * Constructor expecting a user provided accession id.
     * @param ontology
     * @param accessionId
     * @param name
     * @param definition
     */
    public Neo4jAbstractOntologyTerm(  Ontology ontology, String accessionId, String name, String definition ) {
        super(accessionId,name,definition);
        setOntology( ontology );
    }
    
    /* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#toString()
	 */
    @Override
	public String toString() { return this.getName() ; }

	@Override
	public Ontology getOntology() {
        return this.ontology;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#setOntology(bio.knowledge.model.core.JpaOntology)
	 */
	@Override
	public void setOntology(Ontology ontology) {
        // ontology "Ontology" is guaranteed to be a 
        // Neo4jOntology within this Neo4j-based application
        this.ontology = (Neo4jAbstractOntology)ontology;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getIsObsolete()
	 */
	@Override
	public Boolean getIsObsolete() {
        return isObsolete;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#setIsObsolete(java.lang.Boolean)
	 */
	@Override
	public void setIsObsolete(Boolean isObsolete) {
        this.isObsolete = isObsolete;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getIsRelationship()
	 */
	@Override
	public Boolean getIsRelationship() {
        return isRelationship;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#setIsRelationship(java.lang.Boolean)
	 */
	@Override
	public void setIsRelationship(Boolean isRelationship) {
        this.isRelationship = isRelationship;
    }

}
