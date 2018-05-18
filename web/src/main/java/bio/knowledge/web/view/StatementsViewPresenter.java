package bio.knowledge.web.view;

import java.util.Set;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = StatementsView.NAME)
public class StatementsViewPresenter implements View, StatementsView.Listener {

	private static final long serialVersionUID = -6383744406771442514L;

	private StatementsView statementsView = new StatementsViewImpl();
	
	public StatementsViewPresenter() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void buttonClick(Set<Object> selected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
