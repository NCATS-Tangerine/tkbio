package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

public class StatementsViewImpl extends VerticalLayout implements StatementsView, ClickListener {
	
	private static final long serialVersionUID = 1012771871917385704L;
	
	private Button addToGraphBtn = new Button();
	private Set<Object> selected;
	private List<StatementsView.Listener> listeners = new ArrayList<>();
	
	public StatementsViewImpl(Grid grid) {
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addSelectionListener(e -> {
			selected = e.getSelected();
			if (selected.isEmpty()) {
				addToGraphBtn.setEnabled(false);
			} else {
				addToGraphBtn.setEnabled(true);				
			}
		});
		
		addToGraphBtn.setDescription("Add to Graph");
		addToGraphBtn.setEnabled(false);
		addToGraphBtn.setWidth(100, Unit.PERCENTAGE);
		
		setSpacing(true);
		setMargin(new MarginInfo(false, false, true, false));
		addComponents(grid, addToGraphBtn);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		for (StatementsView.Listener listener : listeners) {
			listener.buttonClick(selected);
		}
	}

	@Override
	public void addListener(StatementsView.Listener listener) {
		listeners.add(listener);
	}

}
