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
	void addSubject(Concept subject);

	/**
	 * @param subjects set to be added with the Statement
	 */
	void setSubjects(List<Concept> subjects);

	/**
	 * @return subjects associated with the Statement
	 */
	List<Concept> getSubjects();

	/**
	 * 
	 * @param subject
	 */
	void setSubject(Concept subject);

	/**
	 * 
	 * @return
	 */
	Concept getSubject();

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
	void addObject(Concept object);

	/**
	 * @param objects set to be added with the Statement
	 */
	void setObjects(List<Concept> objects);

	/**
	 * @return objects associated with the Statement
	 */
	List<Concept> getObjects();

	/**
	 * 
	 * @param object
	 */
	void setObject(Concept object);

	/**
	 * 
	 * @return
	 */
	Concept getObject();

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