package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.web.view.components.QueryPollingListener;

/**
 * 
 * @author Colin
 *
 */
public class SearchHistoryViewImpl extends VerticalLayout {

	private static final long serialVersionUID = -5689671079554344415L;
	private List<QueryPollingListener> listeners = Collections.synchronizedList(new ArrayList<>());
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> job;
	private boolean jobStarted = false;

	public SearchHistoryViewImpl() {
		setSpacing(true);
		setMargin(new MarginInfo(false, false, true, false));
		
		addComponentDetachListener(e -> {
			Component component = e.getDetachedComponent();
			removeListener((SingleSearchHistoryView) component);
		});
	}

	public void addResultView(SingleSearchHistoryView resultView) {
		addComponentAsFirst(resultView);
		addListener(resultView.listener());
		if (!jobStarted) {
			jobStarted = true;
			initPolling();
		}
	}
	
	public void startPolling() {
		if (!jobStarted && !listeners.isEmpty()) {
			jobStarted = true;
			initPolling();
		}
	}
	
	/**
	 * Method to explicitly stop polling - to be called when the window is closed. 
	 * If this method is not called, jobStarted will continue to be true, but the job will not
	 * be running properly when the window is closed
	 */
	public void stopPolling() {
		if (!job.isCancelled()) {
			job.cancel(false);
			jobStarted = false;
		}
	}

	public void addListener(QueryPollingListener listener) {
		listeners.add(listener);
	}
	
	private void removeListener(SingleSearchHistoryView component) {
		listeners.remove(component.listener());
		if (listeners.isEmpty()) {
			if (!job.isCancelled()) {
				job.cancel(false);
				jobStarted = false;
			}
		}
	}
	
	private void update() {
		synchronized (listeners) {
			for (QueryPollingListener listener : listeners) {
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
