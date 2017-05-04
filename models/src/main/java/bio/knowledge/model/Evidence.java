package bio.knowledge.model;

import java.util.Set;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Evidence extends IdentifiedEntity {

	/**
	 * @return
	 */
	void setStatement(Statement statement);

	/**
	 * @return
	 */
	Statement getStatement();

	/**
	 * 
	 * @param annotations
	 */
	void setAnnotations(Set<Annotation> annotations);

	/**
	 * 
	 * @param annotation
	 */
	void addAnnotation(Annotation annotation);

	/**
	 * 
	 * @return
	 */
	Set<Annotation> getAnnotations();

	/**
	 * @param count of number of Annotations in Evidence
	 */
	void setCount(Integer count);

	/**
	 * @param increment count of number of Annotations in Evidence
	 */
	void incrementCount();

	/**
	 * @return the 'count' of the Evidence Annotations (i.e. number of independent pieces of Evidence)
	 */
	Integer getCount();

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
	 */
	String toString();

	/**
	 * 
	 */
	int compareTo(IdentifiedEntity other);

}