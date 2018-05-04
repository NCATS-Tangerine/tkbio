package bio.knowledge.web.view;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Colin
 *
 */
public class SearchResultView extends VerticalLayout {

	private static final long serialVersionUID = -5689671079554344415L;

	public SearchResultView() {
		setSpacing(true);
		setMargin(new MarginInfo(false, false, true, false));
	}
	
	public void addResultView(ResultView resultView) {
		this.addComponent(resultView);
	}
}
