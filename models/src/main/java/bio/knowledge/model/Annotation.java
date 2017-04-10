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

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity;

/**
 * @author Richard
 * 
 * Generalizes the semantics of the SemMedDb SENTENCE_PREDICATION table
 * into Annotation URI + text descriptions
 *
 */
@NodeEntity(label="Annotation")
public class Annotation extends Neo4jIdentifiedEntity {
	
	// Default Display Label Length
	private static final int DDLL = 20 ;
	
	// User Id, to track the source of the annotation
	private String userId ;
	
	// The annotation is always visible to the public by default.
	private boolean visible = true ;
	

	/**
	 * Type of Annotation
	 */
	public enum Type {
		
		Remark("remark"), 
		Title("ti"), 
		Abstract("ab")
		;
		
		private String abbreviation ;
		
		Type(String abbreviation){
			this.abbreviation = abbreviation ;
		}
		
	    public static Type lookUp(String abbreviation) {
	    	for(Type type: Type.values()) {
	    		if(type.abbreviation.toLowerCase().equals(abbreviation))
	    			return type ;
	    	}
	    	throw new DomainModelException("Invalid Sentence type abbreviation: "+abbreviation) ;
	    }
	    
	    public String toString() { return name() ; }
		
	}
	
	private Type type = Type.Remark;
	
	private EvidenceCode evidenceCode = EvidenceCode.ND ;
	
	@Relationship(type="REFERENCE")
    private Reference reference ;
    
    Annotation() {}
    
    /**
     * 
     * @param uri
     * @param annotation
     */
    public Annotation( 
    		String accessionId, 
    		String annotation,
    		Reference reference 
    ) {
    	super( accessionId, annotation, Type.Remark.name()+":"+EvidenceCode.ND.getLabel() );
    	setType(type);
    	setEvidenceCode(evidenceCode);
    	setReference(reference) ;
    }
    
    /**
     * 
     * @param uri
     * @param annotation
     * @param type could be 'title' or 'ab
     */
    public Annotation( 
    		String accessionId, 
    		String annotation, 
    		Type type, 
    		EvidenceCode code, 
    		Reference reference 
    ) {
    	super( accessionId, annotation, type.name()+":"+code.getLabel() );
    	setType(type);
    	setEvidenceCode(code);
    	setReference(reference) ;
    }

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(Reference reference) {
		this.reference = reference;
	}

	/**
	 * @return the reference
	 */
	public Reference getReference() {
		return reference;
	}
	
	/*
	 *  The Transient publicationDate and supportingText attributes here
	 *  are only loaded when the Annotation POJO is used as a DTO for 
	 *  transferring data to the "Reference Evidence" table.
	 */

	@Transient
	private String publicationDate = "" ;
	
	public  void setPublicationDate(String date) {
		this.publicationDate = date ;
	}
	
	public String getPublicationDate() {
		if( publicationDate == null || publicationDate.isEmpty() ) {
			if(reference!=null) {
				return reference.getPublicationDate();
			} else {
				return "Unspecified";
			}
		} 
		return publicationDate ;
	}
	
	@Transient
	private String supportingText = "" ;
	
	public  void setSupportingText(String text) {
		this.supportingText = text ;
	}
	
	public String getSupportingText() {
		if( supportingText == null || supportingText.isEmpty() ) {
			String text = getName() ;
			if(text!=null) {
				return text;
			} else {
				return "Unspecified";
			}
		} 
		return publicationDate ;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String text = super.getName();
		if(text!=null && !text.isEmpty()) {
			int slen = text.length() ;
			return text.substring(0, slen > DDLL ? DDLL : slen ) ;
		}
		return "" ;
	}

	/**
	 * 
	 * @return
	 */
	public EvidenceCode getEvidenceCode() {
		return evidenceCode;
	}

	/**
	 * 
	 * @param evidenceCode
	 */
	public void setEvidenceCode( EvidenceCode evidenceCode)  {
		this.evidenceCode = evidenceCode;
	}
 
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}