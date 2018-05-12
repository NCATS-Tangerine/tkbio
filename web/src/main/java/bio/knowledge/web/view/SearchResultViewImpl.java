package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;	
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Colin
 *
 */
public class SearchResultViewImpl extends VerticalLayout implements SearchResultView {

	private static final long serialVersionUID = -5689671079554344415L;
	private List<SearchResultView.Listener> listeners = Collections.synchronizedList(new ArrayList<>());
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	public SearchResultViewImpl() {
		setSpacing(true);
		setMargin(new MarginInfo(false, false, true, false));
		
		addComponentDetachListener(e -> {
			Component component = e.getDetachedComponent();
			removeListener((SearchResultView.Listener) component);
		});
		
		initTask();
	}

	public void addResultView(ResultRowView resultView) {
		addComponentAsFirst(resultView);
		addListener(resultView);
	}

	@Override
	public void addListener(SearchResultView.Listener listener) {
		listeners.add(listener);
	}
	
	private void removeListener(SearchResultView.Listener listener) {
		listeners.remove(listener);
	}
	
	private void update() {
		for (SearchResultView.Listener listener : listeners) {
			listener.update();
		}
	}
	
	private void initTask() {
		Runnable task = () -> {
			update();
		};
		executor.scheduleWithFixedDelay(task, 0, 3, TimeUnit.SECONDS);
	}
}
