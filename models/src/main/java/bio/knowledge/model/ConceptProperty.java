/**
 * 
 */
package bio.knowledge.model;

import bio.knowledge.model.core.AbstractIdentifiedEntity;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyTerm;

/**
 * @author richard
 *
 */
public class ConceptProperty extends AbstractIdentifiedEntity implements OntologyTerm {
	
	/**
	 * 
	 */
	public ConceptProperty() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 */
	public ConceptProperty(String id, String name, String description) {
		super(id, name, description);
	}

	/**
	 * 
	 * @param name
	 * @param description
	 */
	public ConceptProperty(String name, String description) {
		super(name, description);
	}

	/**
	 * 
	 * @param name
	 */
	public ConceptProperty(String name) {
		super(name);
	}

	private Ontology ontology = null;
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getOntologyName()
	 */
	@Override
	public String getOntologyName() {
		if(ontology!=null)
			return ontology.getName();
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#getOntology()
	 */
	@Override
	public Ontology getOntology() {
		return ontology;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.OntologyTerm#setOntology(bio.knowledge.model.core.Ontology)
	 */
	@Override
	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}

	private Boolean isObsolete = false;
	
	@Override
	public Boolean getIsObsolete() {
		return isObsolete;
	}

	@Override
	public void setIsObsolete(Boolean isObsolete) {
		this.isObsolete = isObsolete;
	}

	private Boolean isRelationship = false;
	@Override
	public Boolean getIsRelationship() {
		return isRelationship;
	}

	@Override
	public void setIsRelationship(Boolean isRelationship) {
		this.isRelationship = isRelationship;
	}
}
