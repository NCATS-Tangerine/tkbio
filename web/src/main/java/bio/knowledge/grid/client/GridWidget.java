package bio.knowledge.grid.client;

import com.vaadin.client.widget.grid.events.ScrollEvent;
import com.vaadin.client.widget.grid.events.ScrollHandler;
import com.vaadin.client.widgets.Escalator;

//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import com.vaadin.client.widget.grid.events.ScrollEvent;
//import com.vaadin.client.widget.grid.events.ScrollHandler;
//import com.vaadin.client.widgets.Escalator;

public class GridWidget<T> extends com.vaadin.client.widgets.Grid<T> {
	
//	private Logger _logger = Logger.getLogger(GridWidget.class.toString());
	
	public GridWidget() {
		super();
		
		this.addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent event) {
				Escalator escalator = GridWidget.this.getEscalator();
				int lastIndex = (int) (escalator.getHeightByRows() - 1);
				
				if (escalator.getVisibleRowRange().contains(lastIndex)) {
					GridWidget.this.setTitle("UP");
				} else {
					GridWidget.this.setTitle("DOWN");
				}
			}
			
		});
		
	}
	
	@Override
	public Escalator getEscalator() {
		return super.getEscalator();
	}
}
