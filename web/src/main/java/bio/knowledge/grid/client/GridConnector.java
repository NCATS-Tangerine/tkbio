package bio.knowledge.grid.client;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.widget.grid.events.ScrollEvent;
import com.vaadin.client.widget.grid.events.ScrollHandler;
import com.vaadin.client.widgets.Escalator;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

//@Connect(bio.knowledge.renderer.ButtonRenderer.class)
@Connect(bio.knowledge.grid.Grid.class)
public class GridConnector extends com.vaadin.client.connectors.GridConnector {

	private static final long serialVersionUID = 7130352625723507638L;

	@Override
	public GridWidget<JsonObject> getWidget() {
		return (GridWidget<JsonObject>) super.getWidget();
	}
	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		
		getWidget().addScrollHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent event) {
				Escalator escalator = getWidget().getEscalator();
				int lastIndex = (int) (escalator.getHeightByRows() - 1);
				
				if (escalator.getVisibleRowRange().contains(lastIndex)) {
					getState().scrollListener.onScroll();
				} else {
					
				}
			}
			
		});
	}
	
	@Override
	public GridState getState() {
		return (GridState) super.getState();
	}
	
}
