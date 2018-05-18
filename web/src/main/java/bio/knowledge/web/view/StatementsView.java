package bio.knowledge.web.view;

import java.util.Set;

public interface StatementsView {
	interface Listener {
		void buttonClick(Set<Object> selected);
	}
	public void addListener(Listener listener);
}
