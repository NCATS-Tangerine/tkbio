package bio.knowledge.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import bio.knowledge.ontology.BiolinkClass;
import bio.knowledge.ontology.BiolinkModel;
import bio.knowledge.ontology.BiolinkSlot;
import bio.knowledge.ontology.mapping.InheritanceLookup;
import bio.knowledge.ontology.mapping.ModelLookup;

/**
 * A "BiolinkClass" is what we call a category. A "BiolinkSlot" is what we call a predicate.
 */
@Service
public class OntologyService {
	
	BiolinkModel model;
	
	InheritanceLookup<BiolinkClass> classInheritanceLookup;
	ModelLookup<BiolinkClass> classModelLookup;
	
	InheritanceLookup<BiolinkSlot> slotInheritanceLookup;
	ModelLookup<BiolinkSlot> slotModelLookup;
	
	@PostConstruct
	public void init() {
		model = BiolinkModel.get();
		
		classInheritanceLookup = new InheritanceLookup<>(model.getClasses());
		classModelLookup = new ModelLookup<>(model.getClasses(), classInheritanceLookup);
		
		slotInheritanceLookup = new InheritanceLookup<>(model.getSlots());
		slotModelLookup = new ModelLookup<>(model.getSlots(), slotInheritanceLookup);
	}
	
	public BiolinkClass getClassByName(String biolinkClassName) {
		return classModelLookup.getClassByName(biolinkClassName);
	}
	
	public List<BiolinkClass> getClasses() {
		return model.getClasses();
	}

}
