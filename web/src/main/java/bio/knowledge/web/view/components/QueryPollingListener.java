package bio.knowledge.web.view.components;

import java.util.concurrent.atomic.AtomicBoolean;

public interface QueryPollingListener {
	
	void update();
	boolean isDone();
}
