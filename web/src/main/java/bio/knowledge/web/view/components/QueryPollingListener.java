package bio.knowledge.web.view.components;

import java.util.concurrent.atomic.AtomicBoolean;

public interface QueryPollingListener {

	public AtomicBoolean isInProgress = new AtomicBoolean(false);
	public AtomicBoolean isDone = new AtomicBoolean(false);
	
	void update();
	boolean isDone();
}
