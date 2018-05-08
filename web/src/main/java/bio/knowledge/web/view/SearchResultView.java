package bio.knowledge.web.view;

public interface SearchResultView {
	interface Listener {
		void update();
	}
	void addListener(Listener listener);
}
