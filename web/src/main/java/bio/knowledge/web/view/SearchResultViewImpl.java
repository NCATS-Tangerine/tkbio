package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Colin
 *
 */
public class SearchResultViewImpl extends VerticalLayout implements SearchResultView {

	private static final long serialVersionUID = -5689671079554344415L;
	private List<SearchResultView.Listener> listeners = new ArrayList<>();

	public SearchResultViewImpl() {
		setSpacing(true);
		setMargin(new MarginInfo(false, false, true, false));
	}

	public void addResultView(ResultRowView resultView) {
		addComponent(resultView, 0);
		addListener(resultView);
	}

	@Override
	public void addListener(SearchResultView.Listener listener) {
		listeners.add(listener);
		updateAll();
	}

	public void updateAll() {
		for (SearchResultView.Listener listener : listeners) {
			listener.update();
		}
	}
}
