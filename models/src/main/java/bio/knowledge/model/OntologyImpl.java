/**
 * 
 */
package bio.knowledge.model;

import java.util.HashSet;
import java.util.Set;

import bio.knowledge.model.core.AbstractIdentifiedEntity;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyContext;
import bio.knowledge.model.core.OntologyTerm;

/**
 * @author richard
 *
 */
public class OntologyImpl extends AbstractIdentifiedEntity implements Ontology {

	private Set<OntologyTerm> terms = new HashSet<OntologyTerm>();
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#getTerms()
	 */
	@Override
	public Set<OntologyTerm> getTerms() {
		return terms ;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#setTerms(java.util.Set)
	 */
	@Override
	public void setTerms(Set<OntologyTerm> terms) {
		this.terms = terms;
	}

	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModel(String model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OntologyContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(OntologyContext context) {
		// TODO Auto-generated method stub
		
	}

}
