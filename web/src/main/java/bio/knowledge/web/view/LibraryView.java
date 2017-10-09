package bio.knowledge.web.view;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = LibraryView.NAME)
public class LibraryView extends BaseView {

	public static final String NAME = "concept_map_library";
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
