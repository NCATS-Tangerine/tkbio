/**
 * 
 */
package bio.knowledge.model;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyTerm;

/**
 * @author Richard
 *
 */
public class OntologyTermImpl implements OntologyTerm {

	public OntologyTermImpl() {}
	
	public OntologyTermImpl(String name) {
		this.setName(name);
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#getUri()
	 */
	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#getSynonyms()
	 */
	@Override
	public String getSynonyms() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#setUri(java.lang.String)
	 */
	@Override
	public void setUri(String uri) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.IdentifiedEntity#setSynonyms(java.lang.String)
	 */
	@Override
	public void setSynonyms(String synonyms) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.DatabaseEntity#getDbId()
	 */
	@Override
	public Long getDbId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.DatabaseEntity#setDbId(java.lang.Long)
	 */
	@Override
	public void setDbId(Long id) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.VersionedObject#getVersion()
	 */
	@Override
	public Integer getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.VersionedObject#setVersion(java.lang.Integer)
	 */
	@Override
	public void setVersion(Integer version) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.VersionedObject#getVersionDate()
	 */
	@Override
	public long getVersionDate() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.VersionedObject#setVersionDate(long)
	 */
	@Override
	public void setVersionDate(long versionDate) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(IdentifiedEntity arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getOntologyName()
	 */
	@Override
	public String getOntologyName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getOntology()
	 */
	@Override
	public Ontology getOntology() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#setOntology(bio.knowledge.model.core.Ontology)
	 */
	@Override
	public void setOntology(Ontology ontology) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getIsObsolete()
	 */
	@Override
	public Boolean getIsObsolete() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#setIsObsolete(java.lang.Boolean)
	 */
	@Override
	public void setIsObsolete(Boolean isObsolete) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getIsRelationship()
	 */
	@Override
	public Boolean getIsRelationship() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#setIsRelationship(java.lang.Boolean)
	 */
	@Override
	public void setIsRelationship(Boolean isRelationship) {
		// TODO Auto-generated method stub

	}

}
