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
	
	public Grid() {
		registerRpc(rpc);
		Notification.show("CONSTRUCTOR CALL");
	}
	
	private GridServerRpc rpc = new GridServerRpc() {

		@Override
		public void sort(String[] columnIds, SortDirection[] directions, boolean userOriginated) {
			System.out.println("sort");
			
		}

		@Override
		public void itemClick(String rowKey, String columnId, MouseEventDetails details) {
			System.out.println("itemClick");
			
		}

		@Override
		public void contextClick(int rowIndex, String rowKey, String columnId, Section section,
				MouseEventDetails details) {
			System.out.println("contextClick");
			
		}

		@Override
		public void columnsReordered(List<String> newColumnOrder, List<String> oldColumnOrder) {
			System.out.println("columnsReordered");
			
		}

		@Override
		public void columnVisibilityChanged(String id, boolean hidden, boolean userOriginated) {
			System.out.println("columnVisibilityChanged");
			
		}

		@Override
		public void columnResized(String id, double pixels) {
			System.out.println("COLUMN RESIZED");
		}

		@Override
		public void scrolledToBottom() {
			System.out.println("SCROLLED TO BOTTOM");
			Notification.show("HELLOO");
		}
		
	};
}
