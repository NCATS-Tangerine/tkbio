package bio.knowledge.model;

import java.util.Set;

import bio.knowledge.model.core.Feature;

public interface AnnotatedConcept extends IdentifiedConcept, BeaconResponse {
	
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

	/**
	 * 
	 * @param features
	 */
	void setFeatures(Set<Feature> features) ;

	/**
	 * 
	 * @return
	 */
	Set<Feature> getFeatures();
}