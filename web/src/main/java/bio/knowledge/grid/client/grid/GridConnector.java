package bio.knowledge.grid.client.grid;

import com.vaadin.shared.ui.Connect;

//@Connect(bio.knowledge.renderer.ButtonRenderer.class)
@Connect(bio.knowledge.grid.Grid.class)
public class GridConnector extends com.vaadin.client.connectors.GridConnector {

	private static final long serialVersionUID = 7130352625723507638L;

	@Override
	public GridWidget getWidget() {
		return (GridWidget) super.getWidget();
	}
	
}
