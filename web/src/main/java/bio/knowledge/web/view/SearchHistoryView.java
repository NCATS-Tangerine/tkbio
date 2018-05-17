package bio.knowledge.web.view;

public interface SearchHistoryView {
	interface Listener {
		void update();
	}
	void addListener(Listener listener);
}
