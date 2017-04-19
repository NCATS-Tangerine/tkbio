package bio.knowledge.model;

import java.util.Set;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.neo4j.Neo4jAnnotation;
import bio.knowledge.model.neo4j.Neo4jGeneralStatement;

public interface Evidence {

	/**
	 * @return
	 */
	void setStatement(Neo4jGeneralStatement statement);

	/**
	 * @return
	 */
	Neo4jGeneralStatement getStatement();

	/**
	 * 
	 * @param annotations
	 */
	void setAnnotations(Set<Neo4jAnnotation> annotations);

	/**
	 * 
	 * @param annotation
	 */
	void addAnnotation(Neo4jAnnotation annotation);

	/**
	 * 
	 * @return
	 */
	Set<Neo4jAnnotation> getAnnotations();

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