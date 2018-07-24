package bio.knowledge.web.view;

public interface SearchHistoryView {
	//TODO: remove
	interface Listener {
		void update();
	}
	void addListener(Listener listener);
}
