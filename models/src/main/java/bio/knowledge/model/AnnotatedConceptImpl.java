package bio.knowledge.model;

import java.util.HashSet;
import java.util.Set;

import bio.knowledge.model.core.Feature;

public class AnnotatedConceptImpl extends IdentifiedConceptImpl implements AnnotatedConcept {

	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;

	private String beaconSource = "";

	private Set<String> dbLinks = new HashSet<String>() ;

	private Set<String> terms = new HashSet<String>() ;

	private String accessionId;

	protected AnnotatedConceptImpl() {
		super() ;
	}
	public AnnotatedConceptImpl( String clique, String id, ConceptType type, String name ) {
		super(clique,name,type,"") ;
		this.setAccessionId(id);
	}

	public AnnotatedConceptImpl( String clique, String id, String type, String name ) {
		super(clique,name,type,"") ;
		this.setAccessionId(id);
	}

	public String getAccessionId() {
		return accessionId;
	}
	public void setAccessionId(String accessionId) {
		this.accessionId = accessionId;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.Concept#setBeaconSource(java.lang.String)
	 */
	@Override
	public void setBeaconSource(String source) {
		this.beaconSource = source;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.BeaconResponse#getBeaconSource()
	 */
	@Override
	public String getBeaconSource() {
		return beaconSource;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getCrossReferences()
	 */
	@Override
	public Set<String> getCrossReferences() {
		return dbLinks ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getTerms()
	 */
	@Override
	public Set<String> getTerms() {
		return terms ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#toString()
	 */
	@Override
	public String toString() {
		return getName() ;
	}

	Set<Feature> features = new HashSet<Feature>();

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.AnnotatedConcept#setFeatures(java.util.Set)
	 */
	public void setFeatures(Set<Feature> features) {
		this.features.addAll(features) ;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.AnnotatedConcept#getFeatures()
	 */
	public Set<Feature> getFeatures() {
		return features;
	}
}
