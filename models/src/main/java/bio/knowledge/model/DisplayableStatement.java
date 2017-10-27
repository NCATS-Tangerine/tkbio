package bio.knowledge.model;

public interface DisplayableStatement {
	
	/**
	 * 
	 * @return
	 */
	Concept getSubject();

	/**
	 * @return the predicate
	 */
	Predicate getRelation();

	/**
	 * 
	 * @return
	 */
	Concept getObject();
	
	/**
	 * @return associated Evidence (e.g. References) supporting the Statement
	 */
	Evidence getEvidence();

}
