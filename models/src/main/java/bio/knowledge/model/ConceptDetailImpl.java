/**
 * 
 */
package bio.knowledge.model;

import bio.knowledge.model.core.AbstractIdentifiedEntity;
import bio.knowledge.model.core.AnnotatedEntity;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.OntologyTerm;

/**
 * @author richard
 *
 */
public class ConceptDetailImpl extends AbstractIdentifiedEntity implements Feature {

	private OntologyTerm tag = null;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getTag()
	 */
	@Override
	public OntologyTerm getTag() {
		return tag;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setTag(bio.knowledge.model.core.OntologyTerm)
	 */
	@Override
	public void setTag(OntologyTerm tag) {
		this.tag = tag;
	}
	
	private String value = "";

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}

	private OntologyTerm evidenceCode = null;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getEvidenceCode()
	 */
	@Override
	public OntologyTerm getEvidenceCode() {
		return evidenceCode;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setEvidenceCode(bio.knowledge.model.core.OntologyTerm)
	 */
	@Override
	public void setEvidenceCode(OntologyTerm evidenceCode) {
		this.evidenceCode = evidenceCode;
	}


	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#setOwner(bio.knowledge.model.core.AnnotatedEntity)
	 */
	@Override
	public void setOwner(AnnotatedEntity owner) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Feature#getOwner()
	 */
	@Override
	public AnnotatedEntity getOwner() {
		// TODO Auto-generated method stub
		return null;
	}


}
