package bio.knowledge.grid.client.grid;

import java.util.logging.Logger;

import com.vaadin.client.widget.grid.events.ScrollEvent;
import com.vaadin.client.widget.grid.events.ScrollHandler;
import com.vaadin.client.widgets.Escalator;

public class GridWidget extends com.vaadin.client.widgets.Grid {
	
	private Logger _logger = Logger.getLogger(GridWidget.class.toString());
	
	public GridWidget() {
		super();
		
		this.getEscalator().addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent event) {
				Escalator escalator = GridWidget.this.getEscalator();
				int lastIndex = (int) (escalator.getHeightByRows() - 1);
				
				if (escalator.getVisibleRowRange().contains(lastIndex)) {
					GridWidget.this.setTitle("HELLOOO");
				} else {
					GridWidget.this.setTitle("GOODBYE");
				}
			}
			
		});
	}
}
