package bio.knowledge.model;

public interface DisplayableStatement {
	
	/**
	 * 
	 * @return
	 */
	IdentifiedConcept getSubject();

	/**
	 * @return the predicate
	 */
	Predicate getRelation();

	/**
	 * 
	 * @return
	 */
	IdentifiedConcept getObject();
	
	/**
	 * @return associated Evidence (e.g. References) supporting the Statement
	 */
	Evidence getEvidence();

}
