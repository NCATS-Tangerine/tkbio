package bio.knowledge.model;

import java.util.HashSet;
import java.util.Set;

import bio.knowledge.model.core.Feature;

public class ConceptImpl extends IdentifiedConceptImpl implements AnnotatedConcept {

	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;
	
	private String beaconSource = "";

    // Cross-reference to the 
    // Genetic Home References identifier 
    // for genes and disorders
    private String ghr ;
    
	// Human Metabolome Database identifier
	// i.e. to use to access record at
	// http://www.hmdb.ca/metabolites/HMDB06408
	private String hmdbId ; 
	
    // Cross-reference to the 
    // Chemical Entities of Biological Interest (ChEBI)
    private String chebi ;
    
    private Set<String> dbLinks = new HashSet<String>() ;
    
    private Set<String> terms = new HashSet<String>() ;
    
	private String accessionId;
    
    protected ConceptImpl() {
    	super() ;
    }
    public ConceptImpl( String clique, String id, ConceptType type, String name ) {
    	super(clique,name,type,"") ;
    	this.setAccessionId(id);
    }
    
    public ConceptImpl( String clique, String id, String type, String name ) {
    	super(clique,name,type,"") ;
    	this.setAccessionId(id);
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
	 * @see bio.knowledge.model.neo4j.Concept#getGhr()
	 */
	@Override
	public String getGhr() {
		return ghr;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setGhr(java.lang.String)
	 */
	@Override
	public void setGhr(String ghr) {
		this.ghr = ghr;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getHmdbId()
	 */
	@Override
	public String getHmdbId() {
		return hmdbId ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setHmdbId(java.lang.String)
	 */
	@Override
	public void setHmdbId(String hmdbId) {
		this.hmdbId = hmdbId;
	}	

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getChebi()
	 */
	@Override
	public String getChebi() {
		return chebi;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setChebi(java.lang.String)
	 */
	@Override
	public void setChebi(String chebi) {
		this.chebi = chebi;
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
	public String getAccessionId() {
		return accessionId;
	}
	public void setAccessionId(String accessionId) {
		this.accessionId = accessionId;
	}
}
