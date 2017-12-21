package bio.knowledge.web.ui;

import org.vaadin.tokenfield.TokenField;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;

public class ConceptTokenSearch extends TokenField {
	
	public boolean focused = false;

	public ConceptTokenSearch () {
		super();
	}
	
	public ConceptTokenSearch (String caption) {
		super(caption);
	}
	
}
