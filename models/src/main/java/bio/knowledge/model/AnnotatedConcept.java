package bio.knowledge.model;

import java.util.Set;

import bio.knowledge.model.core.Feature;

public interface AnnotatedConcept extends IdentifiedConcept, BeaconResponse {

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