package bio.knowledge.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.core.AbstractIdentifiedEntity;

public class ConceptImpl extends AbstractIdentifiedEntity implements Concept {

	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;
	
    private SemanticGroup semanticGroup;

    // Counter for the number of times that this 
    // Concept SemanticGroup is used in Statements.
    // This helps the code filter out unproductive SemMedDb concepts
    // from being listed in the "Concept by Text" search results.
    private Long usage = 0L ;

    // The Library class is an indirect wrapper class for
    // the set of associated ConceptMap's related to the included Concept nodes
    private Library library = new Library();
    
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
    
    protected ConceptImpl() {
    	super() ;
    }
    
    protected ConceptImpl( SemanticGroup semgroup, String name ) {
    	super(name) ;
    	this.semanticGroup = semgroup ;
    }

    public ConceptImpl( String accessionId, SemanticGroup semgroup, String name ) {
    	super(accessionId,name,"") ;
    	this.semanticGroup = semgroup ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setSemanticGroup(bio.knowledge.model.SemanticGroup)
	 */
    @Override
	public void setSemanticGroup(SemanticGroup semgroup) {
    	this.semanticGroup = semgroup ;
    }
    
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getSemanticGroup()
	 */
    @Override
	public SemanticGroup getSemanticGroup() {
    	if(semanticGroup==null) {
    		return SemanticGroup.OBJC;
    	}
    	return semanticGroup ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getUsage()
	 */
	@Override
	public Long getUsage() {
		return usage;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setUsage(java.lang.Long)
	 */
	@Override
	public void setUsage(Long usage) {
		this.usage = usage;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#incrementUsage(java.lang.Long)
	 */
	@Override
	public void incrementUsage(Long increment) {
		this.usage += increment;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#incrementUsage()
	 */
	@Override
	public void incrementUsage() {
		this.usage += 1;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setLibrary(bio.knowledge.model.Library)
	 */
	@Override
	public void setLibrary( Library library ) {
		this.library = library;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getLibrary()
	 */
	@Override
	public Library getLibrary() {
		return library;
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

	@Override
	public void setFeatures(Set<Feature> features) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Feature> getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

}
