package bio.knowledge.model;

import java.util.List;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Predicate extends IdentifiedEntity {

	public interface PredicateBeacon {
		
		public Integer getBeaconId();
		
		public String getEdgeLabel();
		
		public String getRelation();
		
		public String getDescription();
		
		public Boolean getNegated();
		
	}
	
	/**
	 * 
	 * @param predicates
	 */
	void setPredicatesByBeacons(List<PredicateBeacon> predicates);
	
	/**
	 * 
	 */
	public List<PredicateBeacon> getPredicatesByBeacons() ;

	void addPredicatesByBeacon(PredicateBeacon predicate);

}