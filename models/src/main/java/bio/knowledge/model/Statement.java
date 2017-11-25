package bio.knowledge.model;

import java.util.List;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Statement extends IdentifiedEntity, DisplayableStatement, BeaconResponse {
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
	 * @param predicate the predicate to set
	 */
	void setRelation(Predicate relation);

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
	 * @param evidence to be associated with the Statement
	 */
	void setEvidence(Evidence evidence);

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
	 */
	String toString();

	String getName();

}