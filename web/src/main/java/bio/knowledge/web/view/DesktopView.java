/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.web.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

import bio.knowledge.web.design.DesktopDesign;

/**
 * @author Richard
 * @author Yinglun Colin Qiao
 *
 */
@SpringView(name = DesktopView.NAME)
public class DesktopView extends DesktopDesign implements View {

	private static final long serialVersionUID = -3941787763184092605L;

	public static final String NAME = "main";
	
	private StatementsView statementsView = new StatementsView();

	public DesktopView() {
		viewingConcepts.setSpacing(false);
		
		searchLayout.setSpacing(true);
		searchLayout.setMargin(new MarginInfo(true, false, false, true));
		search.addStyleName("concept-search-field");
		
		historyBtn.setIcon(FontAwesome.HISTORY);
		historyBtn.setStyleName(ValoTheme.BUTTON_HUGE);
		historyBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY, true);
		historyBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS, true);
		
		dataTabSheet.addTab(statementsView, "Statements");
	}

	/**
	 * @return the search
	 */
	public TextField getSearchField() {
		return search;
	}

	/**
	 * @return the searchBtn
	 */
	public Button getSearchBtn() {
		return searchBtn;
	}

	/**
	 * @return the demoMapBtn
	 */
	public Button getSearchMapLibraryBtn() {
		return searchMapLibraryBtn;
	}

	/**
	 * @return the map center button
	 */
	public Button getCenterBtn() {
		return centerMapBtn;
	}

	/**
	 * @return the cmbar
	 */
	public HorizontalLayout getCmbar() {
		return cmbar;
	}

	// /**
	// * @param cmbar the cmbar to set
	// */
	// public void setCmbar(HorizontalLayout cmbar) {
	// this.cmbar = cmbar;
	// }

	/**
	 * @return the saveBtn
	 */
	public Button getSaveBtn() {
		return saveBtn;
	}

	// /**
	// * @param saveBtn the saveBtn to set
	// */
	// public void setSaveBtn(Button saveBtn) {
	// this.saveBtn = saveBtn;
	// }

	/**
	 * @return the loadMap
	 */
	public Upload getLoadMap() {
		return loadMap;
	}

	// /**
	// * @param loadMap the loadMap to set
	// */
	// public void setLoadMap(Upload loadMap) {
	// this.loadMap = loadMap;
	// }

	/**
	 * @return the clearMapBtn
	 */
	public Button getClearMapBtn() {
		return clearMapBtn;
	}

	/**
	 * @param clearMapBtn
	 *            to set
	 */
	// public void setClearMapBtn(Button clearMapBtn) {
	// this.clearMapBtn = clearMapBtn;
	// }

	public TabSheet getTabSheet() {
		return dataTabSheet;
	}

	/**
	 * @return the relationsTab
	 */
//	public VerticalLayout getRelationsTab() {
//		return relationsTab;
//	}

	
	public StatementsView getStatementsView() {
		return statementsView;
	}
	/**
	 * Implicitome disabled for now...
	 * 
	 * @return the implicitRelationsTab
	 * 
	 *         public VerticalLayout getImplicitRelationsTab() { return
	 *         implicitRelationsTab; }
	 */

	/**
	 * April 21, Implicitome removed for now
	 * 
	 * @return the cooccurrencesTab
	 * 
	 *         public VerticalLayout getCooccurrencesTab() { return
	 *         cooccurrencesTab; }
	 */

//	public VerticalLayout getEvidenceTab() {
//		return evidenceTab;
//	}
//
//	public VerticalLayout getReferenceTab() {
//		return referenceTab;
//	}

	public HorizontalLayout getCmPanel() {
		return cmPanel;
	}

	public HorizontalLayout getViewingConcepts() {
		return viewingConcepts;
	}

	public NativeSelect getColorSelect() {
		return colorSelect;
	}

//	public void setReferenceLayout(VerticalLayout referenceLayout) {
//		this.referenceLayout = referenceLayout;
//	}

	public NativeSelect getCmLayoutSelect() {
		return cmLayoutSelect;
	}

//	public void setDesktopSplitPanel(VerticalSplitPanel splitPanel) {
//		this.desktopSplitPanel = splitPanel;
//	}

	public VerticalSplitPanel getDesktopSplitPanel() {
		return desktopSplitPanel;
	}

	public HorizontalLayout getPopUpLayout() {
		return popUpLayout;
	}

	public Slider getZoomSlider() {
		return zoomSlider;
	}

	/**
	 * @return the legend
	 */
	public Button getShowLegendBtn() {
		return showLegendBtn;
	}
	
	public Button getHistoryButton() {
		return historyBtn;
	}

//	/**
//	 * @param
//	 */
//	public void setShowLegendBtn(Button showLegendBtn) {
//		this.showLegendBtn = showLegendBtn;
//	}

	public Label getConceptMapNameLabel() {
		return this.conceptMapNameLabel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("[parameters]: " + event.getParameters());
		String eventParameter = event.getParameters();
		if (!eventParameter.contains("/")) {
			return;
		}
		
		String[] parameters =  event.getParameters().split("/");
		if (parameters.length >= 2) {
			String viewName = parameters[0];
			String parameter = parameters[1];
			
			getUI().getNavigator().navigateTo(viewName + "/" + parameter);
		}

		
		
//		if (UI.getCurrent() instanceof DesktopUI) {
//			DesktopUI ui = (DesktopUI) UI.getCurrent();
//			String state = ui.getNavigator().getState();


			// // To make sure it displays the relations table when
			// // navigated back to this view's URI fragment.
			// if (state.equals(DesktopView.NAME) ||
			// (state.equals(ListView.NAME + "/" + ViewName.RELATIONS_VIEW))) {
			// ui.navigateToRelationsView();
			// }

			// TODO: Rework the above code to fit into the below code.
			// This should allow the user navigate to a view simply by
			// using the URL (maybe they send a URL to a friend).
			// if (state.startsWith(ListView.NAME + "/" + ViewName.RELATIONS_VIEW)) {
			// String conceptId = getStateSegment(state, 2);
			// ui.displayStatements(conceptId);
			// } else if (state.startsWith(ReferenceView.NAME)) {
			// String annotationId = getStateSegment(state, 1);
			// ui.displayReference(annotationId);
			// }
//		}
	}

	private String getStateSegment(String state, int i) {
		String[] parameters = state.split("/");
		if (parameters.length > i) {
			return parameters[i];
		} else {
			return "";
		}
	}

}
