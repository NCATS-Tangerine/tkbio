package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;	
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Colin
 *
 */
public class SearchHistoryViewImpl extends VerticalLayout implements SearchHistoryView {

	private static final long serialVersionUID = -5689671079554344415L;
	private List<SearchHistoryView.Listener> listeners = Collections.synchronizedList(new ArrayList<>());
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> job;
	private boolean jobStarted = false;

	public SearchHistoryViewImpl() {
		setSpacing(true);
		setMargin(new MarginInfo(false, false, true, false));
		
		addComponentDetachListener(e -> {
			Component component = e.getDetachedComponent();
			removeListener((SearchHistoryView.Listener) component);
		});
	}

	public void addResultView(SingleSearchHistoryView resultView) {
		addComponentAsFirst(resultView);
		addListener(resultView);
		if (!jobStarted) {
			jobStarted = true;
			initPolling();
		}
	}

	@Override
	public void addListener(SearchHistoryView.Listener listener) {
		listeners.add(listener);
	}
	
	private void removeListener(SearchHistoryView.Listener listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			if (!job.isCancelled()) {
				job.cancel(true);
				jobStarted = false;
			}
		}
	}
	
	private void update() {
		System.out.println("[updating all listeners]");
		synchronized (listeners) {
			for (SearchHistoryView.Listener listener : listeners) {
				listener.update();
			}
		}
	}
	
	private void initPolling() {
		Runnable task = () -> {
			getUI().access(() -> {		
				update();
			});
		};
		job = executor.scheduleWithFixedDelay(task, 0, 3, TimeUnit.SECONDS);
	}
	
	public void stopService() {
		if (job == null) {
			job.cancel(true);			
		}
		executor.shutdown();
	}
}
