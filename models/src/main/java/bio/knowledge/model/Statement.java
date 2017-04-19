package bio.knowledge.model;

import java.util.List;

import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.model.neo4j.Neo4jEvidence;
import bio.knowledge.model.neo4j.Neo4jPredicate;

public interface Statement {

	/**
	 * 
	 * @param subject to be added to the Statement
	 */
	void addSubject(Neo4jConcept subject);

	/**
	 * @param subjects set to be added with the Statement
	 */
	void setSubjects(List<Neo4jConcept> subjects);

	/**
	 * @return subjects associated with the Statement
	 */
	List<Neo4jConcept> getSubjects();

	/**
	 * 
	 * @param subject
	 */
	void setSubject(Neo4jConcept subject);

	/**
	 * 
	 * @return
	 */
	Neo4jConcept getSubject();

	/**
	 * @param predicate the predicate to set
	 */
	void setRelation(Neo4jPredicate relation);

	/**
	 * @return the predicate
	 */
	Neo4jPredicate getRelation();

	/**
	 * 
	 * @param subject to be added to the Statement
	 */
	void addObject(Neo4jConcept object);

	/**
	 * @param objects set to be added with the Statement
	 */
	void setObjects(List<Neo4jConcept> objects);

	/**
	 * @return objects associated with the Statement
	 */
	List<Neo4jConcept> getObjects();

	/**
	 * 
	 * @param object
	 */
	void setObject(Neo4jConcept object);

	/**
	 * 
	 * @return
	 */
	Neo4jConcept getObject();

	/**
	 * 
	 * @param evidence to be associated with the Statement
	 */
	void setEvidence(Neo4jEvidence evidence);

	/**
	 * @return associated Evidence (e.g. References) supporting the Statement
	 */
	Neo4jEvidence getEvidence();

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
	 */
	String toString();

}