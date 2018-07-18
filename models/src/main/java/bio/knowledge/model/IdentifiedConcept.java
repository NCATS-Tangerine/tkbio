package bio.knowledge.model;

import java.util.List;

import bio.knowledge.model.core.IdentifiedEntity;

public interface IdentifiedConcept extends IdentifiedEntity {

	/**
	 * @param the Identifier Equivalent Concept Clique of the Concept
	 * Should generally be set at node creation, but sometimes not?
	 */
	void setCliqueId(String cliqueId);

	/**
	 * 
	 * @return the Identifier Equivalent Concept Clique of the Concept
	 */
	String getCliqueId();
	
	/**
	 * 
	 * @param name is a human readable String name or title of the Concept
	 */
	void setName(String name);
	
	/**
	 * @return a human readable name or title of the Concept
	 */
	String getName();

	/**
	 * 
	 * @param the Concept Semantic Type 
	 * Should generally be set at node creation, but sometimes not?
	 */
	void setCategories(List<String> type);

	/**
	 * 
	 * @return the Concept Semantic Type 
	 */
	List<String> getCategories();
	
	/**
	 * 
	 * @param taxon is the NCBI Taxon identifier associated with the Concept (optional)
	 */
	void setTaxon(String taxon);
	
	/**
	 * 
	 * @return taxon is the NCBI Taxon identifier associated with the Concept (may be empty)
	 */
	String getTaxon();

	/* 
	 * These attributes are not directly associated with the concepts identified
	 * by beacons but are more to do with the Knowledge Graph "blackboard" contents
	 */
	
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


}