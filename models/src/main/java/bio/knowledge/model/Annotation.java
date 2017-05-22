package bio.knowledge.model;

import bio.knowledge.model.Reference;
import bio.knowledge.model.core.IdentifiedEntity;

public interface Annotation extends IdentifiedEntity {

	/**
	 * Type of Annotation
	 */
	public enum Type {
		
		Remark("remark"), 
		Title("ti"), 
		Abstract("ab");
		
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
	
	void setType(Type type);

	/**
	 * @param reference the reference to set
	 */
	void setReference(Reference reference);

	/**
	 * @return the reference
	 */
	Reference getReference();

	void setPublicationDate(String date);

	String getPublicationDate();

	void setSupportingText(String text);

	String getSupportingText();

	/**
	 * 
	 */
	String toString();

	/**
	 * 
	 * @return
	 */
	EvidenceCode getEvidenceCode();

	/**
	 * 
	 * @param evidenceCode
	 */
	void setEvidenceCode(EvidenceCode evidenceCode);

	String getUserId();

	void setUserId(String userId);

	boolean isVisible();

	void setVisible(boolean visible);
	
	public String getUrl();
	public void setUrl(String url);

}