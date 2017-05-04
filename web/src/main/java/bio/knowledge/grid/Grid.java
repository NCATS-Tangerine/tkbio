package bio.knowledge.grid;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.GridConstants.Section;
import com.vaadin.ui.Notification;

import bio.knowledge.grid.client.GridServerRpc;

public class Grid extends com.vaadin.ui.Grid {
	private static final long serialVersionUID = 6340543125776370643L;
	
	private final ScrollListener scrollListener;
	
	public interface ScrollListener {
		public void scrolledToBottom();
	}
	
	public Grid(ScrollListener scrollListener) {
		registerRpc(rpc);
		this.scrollListener = scrollListener;
	}
	
	private GridServerRpc rpc = new GridServerRpc() {

		@Override
		public void scrolledToBottom() {
			scrollListener.scrolledToBottom();
		}
		
	};
}
