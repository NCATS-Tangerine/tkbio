package bio.knowledge.model;

import java.util.ArrayList;
import java.util.List;

import bio.knowledge.model.core.AbstractIdentifiedEntity;

public class IdentifiedConceptImpl 
extends AbstractIdentifiedEntity 
implements IdentifiedConcept {

	private String clique = "";

	private List<String> categories = new ArrayList<>();

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
	 * @param categories
	 * @param taxon
	 */
	public IdentifiedConceptImpl(  String clique, String name, List<String> categories ) {
		super( "", name, name );
		this.clique = clique ;
		this.categories = categories;
	}

	/**
	 * 
	 * @param clique
	 * @param id
	 * @param name
	 * @param type
	 * @param taxon
	 */
	public IdentifiedConceptImpl( String clique, String id, String name, List<String> categories ) {
		super( id, name, name );

		this.clique = clique ;

		if(categories !=null) {
			this.categories = categories;
		}
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#setClique(String cliqueId)
	 */
	@Override
	public void setCliqueId(String cliqueId) {
		this.clique = cliqueId;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#getClique()
	 */
	@Override
	public String getCliqueId() {
		if(clique==null||clique.isEmpty())
			throw new RuntimeException("TRAPPING Empty concept clique identifier!");
		return clique;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#setType(bio.knowledge.model.ConceptType)
	 */
	@Override
	public void setCategories(List<String> categories) {
		this.categories = categories ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.IdentifiedConcept#getType()
	 */
	@Override
	public List<String> getCategories() {
		return categories ;
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
