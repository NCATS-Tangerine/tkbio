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

import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import bio.knowledge.model.Annotation;
import bio.knowledge.model.Reference;
import bio.knowledge.model.EvidenceCode;
import bio.knowledge.model.core.AbstractIdentifiedEntity;

/**
 * @author Richard
 * 
 * Generalizes the semantics of the SemMedDb SENTENCE_PREDICATION table
 * into Annotation URI + text descriptions
 *
 */
public class AnnotationImpl extends AbstractIdentifiedEntity implements Annotation {
	
	private String url;
	
	// Default Display Label Length
	private static final int DDLL = 20 ;
	
	// User Id, to track the source of the annotation
	private String userId ;
	
	// The annotation is always visible to the public by default.
	private boolean visible = true ;
	
	private Type type = Type.Remark;
	
	private EvidenceCode evidenceCode = EvidenceCode.ND ;
	
	@Relationship(type="REFERENCE")
    private Reference reference ;
    
    public AnnotationImpl() {}
    
    /**
     * 
     * @param uri
     * @param annotation
     */
    public AnnotationImpl( 
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
    public AnnotationImpl( 
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

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#getType()
	 */
	public Type getType() {
		return type;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#setType()
	 */
	@Override
	public void setType(Type type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#getReference()
	 */
	@Override
	public Reference getReference() {
		return this.reference;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#setReference()
	 */
	@Override
	public void setReference(Reference reference) {
		this.reference = reference;
	}
	
	/*
	 *  The Transient publicationDate and supportingText attributes here
	 *  are only loaded when the Annotation POJO is used as a DTO for 
	 *  transferring data to the "Reference Evidence" table.
	 */

	@Transient
	private String publicationDate = "" ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#setPublicationDate(java.lang.String)
	 */
	@Override
	public  void setPublicationDate(String date) {
		this.publicationDate = date ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#getPublicationDate()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#setSupportingText(java.lang.String)
	 */
	@Override
	public  void setSupportingText(String text) {
		this.supportingText = text ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#getSupportingText()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#toString()
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

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#getEvidenceCode()
	 */
	@Override
	public EvidenceCode getEvidenceCode() {
		return evidenceCode;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#setEvidenceCode(bio.knowledge.model.EvidenceCode)
	 */
	@Override
	public void setEvidenceCode( EvidenceCode evidenceCode)  {
		this.evidenceCode = evidenceCode;
	}
 
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#getUserId()
	 */
	@Override
	public String getUserId() {
		return this.userId;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Annotation#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

}