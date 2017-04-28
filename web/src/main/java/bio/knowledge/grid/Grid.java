package bio.knowledge.grid;

import java.util.ArrayList;
import java.util.List;

public class Grid extends com.vaadin.ui.Grid {
	private static final long serialVersionUID = 6340543125776370643L;
	
	public Grid() {
		addOnScrollListener(new ScrollListener() {

			@Override
			public void onScroll() {
				System.out.println("SCROLLINGGGG");
			}
			
		});
	}
	
	public interface ScrollListener {
		public void onScroll();
	}
	
	private List<ScrollListener> listeners = new ArrayList<ScrollListener>();
	
	public void addOnScrollListener(ScrollListener listener) {
		listeners.add(listener);
	}
	
}
