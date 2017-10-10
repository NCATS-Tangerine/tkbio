package bio.knowledge.model;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Predicate extends IdentifiedEntity {

	/**
	 * 
	 * @param beaconNameFromId
	 */
	void setBeaconSource(String beaconSource);
	
	/**
	 * 
	 * @return
	 */
	public String getBeaconSource() ;

}