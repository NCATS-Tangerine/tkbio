package bio.knowledge.model;

import java.util.List;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Predicate extends IdentifiedEntity {

	public interface PredicateBeacon {
		
		public Integer getBeacon();
		
		public String getId();
		
		public String getDefinition();
		
	}
	
	/**
	 * 
	 * @param beaconNameFromId
	 */
	void setBeacons(List<PredicateBeacon> beacons);
	
	/**
	 * 
	 */
	public List<PredicateBeacon> getBeacons() ;

	void addBeacon(PredicateBeacon beacon);

}