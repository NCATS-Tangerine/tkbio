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

import bio.knowledge.model.core.AnnotatedEntity;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.OntologyTerm;

/**
 * @author Richard
 *
 * This class records instances of tag = value annotations of 
 * complex domain model actor entities in the system.
 */
@NodeEntity(label="Feature")
public class Neo4jAbstractFeature 
	extends Neo4jAbstractIdentifiedEntity implements Feature {

	/**
	 * 
	 */
	public Neo4jAbstractFeature() {
        super();
    }

	/**
	 * 
	 * @param owner
	 * @param tag
	 */
    public Neo4jAbstractFeature( AnnotatedEntity owner, OntologyTerm tag ) {
    	this(owner, "", tag, "") ;
    }
    
    /**
     * 
     * @param owner
     * @param tag
     * @param value
     */
    public Neo4jAbstractFeature( AnnotatedEntity owner, OntologyTerm tag, String value ) {
    	this(owner, "", tag, value) ;
    }
    
	/**
	 * @param owner2
	 * @param accessionId
	 * @param tag2
	 */
	public Neo4jAbstractFeature( AnnotatedEntity owner, String accessionId, OntologyTerm tag ) {
        this(owner, accessionId, tag, "") ;
	}

    /**
     * Note: this method does NOT automatically add the Feature to the owner. 
     * The feature should be added to the owner after it is first saved.
	 * @param owner
	 * @param accessionId
	 * @param tag OntologyTerm
	 * @param value
	 */
	public Neo4jAbstractFeature( AnnotatedEntity owner, String accessionId, OntologyTerm tag, String value ) {
        super( accessionId, tag.getName(), value );
        setOwner( owner );
        setTag( tag );
    }

	/**
     */
    @Relationship( type="OWNER" )
	private Neo4jAbstractAnnotatedEntity owner;

    /**
     */
    @Relationship( type="TAG" )
	private Neo4jAbstractOntologyTerm tag;

    /**
     */
    @Relationship( type="EVIDENCE_CODE" )  // Do I need another EVIDENCE class here?
	private Neo4jAbstractOntologyTerm evidenceCode;

    /* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#toString()
	 */
    @Override
	public String toString() { return tag.toString()+" = "+getDescription() ; }


    /* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setOwner(bio.knowledge.model.core.AnnotatedEntity)
	 */
    @Override
	public void setOwner(AnnotatedEntity owner) {
        // owner "AnnotatedEntity" is guaranteed to be a 
        // JpaLocation within this JPA-based application
        this.owner = (Neo4jAbstractAnnotatedEntity)owner;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getOwner()
	 */
	@Override
	public AnnotatedEntity getOwner() {
        return this.owner;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getTag()
	 */
	@Override
	public OntologyTerm getTag() {
        return this.tag;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setTag(bio.knowledge.model.core.OntologyTerm)
	 */
	@Override
	public void setTag(OntologyTerm tag) {
        // tag "OntologyTerm" is guaranteed to be a 
        // JpaOntologyTerm within this JPA-based application
        this.tag = (Neo4jAbstractOntologyTerm)tag;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getValue()
	 */
	@Override
	public String getValue() {
        return getDescription() ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
        setDescription(value);
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getEvidenceCode()
	 */
	@Override
	public OntologyTerm getEvidenceCode() {
        return this.evidenceCode;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setEvidenceCode(bio.knowledge.model.core.OntologyTerm)
	 */
	@Override
	public void setEvidenceCode( OntologyTerm evidenceCode ) {
        // evidenceCode "OntologyTerm" is guaranteed to be a 
        // JpaOntologyTerm within this JPA-based application
        this.evidenceCode = (Neo4jAbstractOntologyTerm)evidenceCode;
    }
}
