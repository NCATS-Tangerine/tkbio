package bio.knowledge.model;

import java.util.List;
import java.util.Set;

import bio.knowledge.model.core.Feature;

public interface AnnotatedConcept extends IdentifiedConcept, BeaconResponse {

	/**
	 * 
	 * @return
	 */
	Set<String> getAliases();

	interface BeaconEntry {
		
		/**
		 * 
		 * @return
		 */
		String getBeacon() ;
		
		/**
		 * 
		 * @return
		 */
		String getId();
		
		/**
		 * 
		 * @return
		 */
		Set<String> getSynonyms();
		
		/**
		 * 
		 * @param definition
		 */
		void setDefinition(String definition);
		
		/**
		 * 
		 * @return
		 */
		String getDefinition();

		/**
		 * 
		 * @return
		 */
		Set<Feature> getDetails();
	}
	
	List<BeaconEntry> getEntries();
}