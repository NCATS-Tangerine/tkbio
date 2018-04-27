package bio.knowledge.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bio.knowledge.model.AnnotatedConcept;
import bio.knowledge.model.core.Feature;

public class AnnotatedConceptImpl extends IdentifiedConceptImpl implements AnnotatedConcept {

	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;

	private String beaconSource = "";

	private Set<String> dbLinks = new HashSet<String>() ;

	protected AnnotatedConceptImpl() { }
	
	public AnnotatedConceptImpl( String clique, String name, ConceptType type, String taxon ) {
		super( clique, name, type,taxon ) ;
	}

	public AnnotatedConceptImpl( String clique, String name, String type, String taxon ) {
		super( clique, name, type, taxon ) ;
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
	public Set<String> getAliases() {
		return dbLinks ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.neo4j.Concept#toString()
	 */
	@Override
	public String toString() {
		return getName() ;
	}

	public class ConceptBeaconEntry implements BeaconEntry {
		
		private final Integer beaconId;
		private final String id;
		private Set<String> synonyms = new HashSet<String>();
		private String definition="";
		private Set<Feature> details = new HashSet<Feature>();

		public ConceptBeaconEntry(Integer beacon, String id) {
			this.beaconId = beacon;
			this.id = id;
		}

		@Override
		public Integer getBeacon() {
			return beaconId;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public Set<String> getSynonyms() {
			return synonyms;
		}

		@Override
		public void setDefinition(String definition) {
			this.definition = definition;
		}

		@Override
		public String getDefinition() {
			return definition;
		}

		@Override
		public Set<Feature> getDetails() {
			return details;
		}
		
	}
	
	
	private List<BeaconEntry> entries = new ArrayList<BeaconEntry>();
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.AnnotatedConcept#getEntries()
	 */
	@Override
	public List<BeaconEntry> getEntries() {
		return entries;
	}
}
