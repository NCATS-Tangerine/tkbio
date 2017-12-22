package bio.knowledge.model;

import bio.knowledge.model.core.AbstractIdentifiedEntity;

public class IdentifiedConceptImpl 
	extends AbstractIdentifiedEntity 
	implements IdentifiedConcept {

	private String clique = "";
	
	private String name = "" ;
	
    private ConceptType conceptType = null;

	private String taxon = "";

    // Counter for the number of times that this 
    // Concept SemanticGroup is used in Statements.
    // This helps the code filter out unproductive SemMedDb concepts
    // from being listed in the "Concept by Text" search results.
    private Long usage = 0L ;

    // The Library class is an indirect wrapper class for
    // the set of associated ConceptMap's related to the included Concept nodes
    private Library library = new Library();

    protected IdentifiedConceptImpl() { }
    
    /**
     * 
     * @param clique
     * @param name
     * @param type
     * @param taxon
     */
    public IdentifiedConceptImpl(  String clique, String name, ConceptType type, String taxon ) {
    	this.clique = clique ;
    	this.name = name ;
    	this.conceptType = type;
    	this.taxon = taxon;
    }
    
    /**
     * 
     * @param clique
     * @param name
     * @param type
     * @param taxon
     */
    public IdentifiedConceptImpl(  String clique, String name, String type, String taxon ) {
    	
    	this.clique = clique ;
    	this.name = name ;
    	if( type==null || type.isEmpty() )
    		this.conceptType = ConceptType.OBJC;
    	else
    		this.conceptType = ConceptType.valueOf(type,ConceptType.OBJC) ;
    	this.taxon = taxon;
    	
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setClique(String cliqueId)
	 */
    @Override
	public void setClique(String cliqueId) {
		this.clique = cliqueId;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getClique()
	 */
	@Override
	public String getClique() {
		if(clique==null||clique.isEmpty())
			throw new RuntimeException("TRAPPING Empty concept clique identifier!");
		return clique;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#setType(bio.knowledge.model.ConceptType)
	 */
    @Override
	public void setType(ConceptType type) {
    	this.conceptType = type ;
    }
    
	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#getType()
	 */
    @Override
	public ConceptType getType() {
    	if(conceptType==null) {
    		return ConceptType.OBJC;
    	}
    	return conceptType ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#setTaxon(String)
	 */
	@Override
	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#getTaxon()
	 */
	@Override
	public String getTaxon() {
		return taxon;
	}

	/* 
	 * These attributes are not directly associated with the concepts identified
	 * by beacons but are more to do with the Knowledge Graph "blackboard" contents
	 */

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
	
	@Override
    public String toString() {
    	return getName() ;
    }
}
