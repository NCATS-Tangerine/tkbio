package bio.knowledge.model;

import java.util.Set;

import bio.knowledge.model.core.AnnotatedEntity;
import bio.knowledge.model.core.IdentifiedEntity;

public interface Concept extends IdentifiedEntity, AnnotatedEntity {
	
	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;

	/**
	 * 
	 * @param the Concept Semantic Group 
	 * Should generally be set at node creation, but sometimes not?
	 */
	void setSemanticGroup(SemanticGroup semgroup);

	/**
	 * 
	 * @return the Explicit Concept SemanticGroup 
	 */
	SemanticGroup getSemanticGroup();

	/**
	 * @return the usage of the Concept in Statements
	 */
	Long getUsage();

	/**
	 * @param usage to set counting the number of Concept used in Statements
	 */
	void setUsage(Long usage);

	/**
	 * @param increment to add to count of the number of Concept used in Statements
	 */
	void incrementUsage(Long increment);

	/**
	 * @param increment by one the count of the number of Concept used in Statements
	 */
	void incrementUsage();

	/**
	 * 
	 * @param library of archived ConceptMaps associated with the Concept
	 */
	void setLibrary(Library library);

	/**
	 * @return Library of archived ConceptMaps associated with the Concept
	 */
	Library getLibrary();

	/**
	 * @return the Genetic Home References identifier (for genes and disorders)
	 */
	String getGhr();

	/**
	 * @param ghr the Genetic Home References identifier to set
	 */
	void setGhr(String ghr);

	/**
	 * 
	 * @return the Human Metabolome DataBase identifier (as a string)
	 */
	String getHmdbId();

	/**
	 * @param hmdbId the Human Metabolome DataBase identifier (as a string) to set
	 */
	void setHmdbId(String hmdbId);

	/**
	 * @return any associated identifier for Chemical Entities of Biological Interest (ChEBI)
	 */
	String getChebi();

	/**
	 * @param chebi associated identifier for Chemical Entities of Biological Interest (ChEBI)
	 */
	void setChebi(String chebi);

	/**
	 * 
	 * @return
	 */
	Set<String> getCrossReferences();

	/**
	 * 
	 * @return
	 */
	Set<String> getTerms();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	String toString();

	String getName();

}