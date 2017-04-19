package bio.knowledge.model;

import bio.knowledge.model.neo4j.Neo4jReference;
import bio.knowledge.model.neo4j.Neo4jAnnotation.Type;

public interface Annotation {

	Type getType();

	void setType(Type type);

	/**
	 * @param reference the reference to set
	 */
	void setReference(Neo4jReference reference);

	/**
	 * @return the reference
	 */
	Neo4jReference getReference();

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

}