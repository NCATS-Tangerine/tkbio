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
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

import bio.knowledge.web.design.DesktopDesign;
import bio.knowledge.web.ui.DesktopUI;

/**
 * @author Richard
 * @author Yinglun Colin Qiao
 *
 */
@SpringView(name = DesktopView.NAME)
public class DesktopView extends DesktopDesign implements View {

	private static final long serialVersionUID = -3941787763184092605L;

	public static final String NAME = "desktop";
	
	public DesktopView() { }
	

	/**
	 * @return the search
	 */
	public TextField getSearch() {
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(TextField search) {
		this.search = search;
	}

	/**
	 * @return the searchBtn
	 */
	public Button getSearchBtn() {
		return searchBtn;
	}

	/**
	 * @param searchBtn the searchBtn to set
	 */
	public void setSearchBtn(Button searchBtn) {
		this.searchBtn = searchBtn;
	}

	/**
	 * @return the demoMapBtn
	 */
	public Button getSearchMapLibraryBtn() {
		return searchMapLibraryBtn;
	}

	/**
	 * @param demoMapBtn the demoMapBtn to set
	 */
	public void setSearchMapLibraryBtn(Button searchMapLibraryBtn) {
		this.searchMapLibraryBtn = searchMapLibraryBtn;
	}

	/**
	 * @return the map center button
	 */
	public Button getCenterBtn() {
		return centerMapBtn;
	}

//	/**
//	 * @param viewingConcepts the viewingConcepts to set
//	 */
//	public void setViewingConceptsLabel(Label viewingConceptsLabel) {
//		this.viewingConceptsLabel = viewingConceptsLabel;
//	}


	/**
	 * @return the cmbar
	 */
	public HorizontalLayout getCmbar() {
		return cmbar;
	}

	/**
	 * @param cmbar the cmbar to set
	 */
	public void setCmbar(HorizontalLayout cmbar) {
		this.cmbar = cmbar;
	}

	/**
	 * @return the saveBtn
	 */
	public Button getSaveBtn() {
		return saveBtn;
	}

	/**
	 * @param saveBtn the saveBtn to set
	 */
	public void setSaveBtn(Button saveBtn) {
		this.saveBtn = saveBtn;
	}

	/**
	 * @return the loadMap
	 */
	public Upload getLoadMap() {
		return loadMap;
	}

	/**
	 * @param loadMap the loadMap to set
	 */
	public void setLoadMap(Upload loadMap) {
		this.loadMap = loadMap;
	}

	/**
	 * @return the clearMapBtn
	 */
	public Button getClearMapBtn() {
		return clearMapBtn;
	}

	/**
	 * @param clearMapBtn to set
	 */
	public void setClearMapBtn(Button clearMapBtn) {
		this.clearMapBtn = clearMapBtn;
	}
	
	public TabSheet getDataTabSheet() {
		return dataTabSheet ;
	}
	
	/**
	 * @return the relationsTab
	 */
	public VerticalLayout getRelationsTab() {
		return relationsTab;
	}

	/**
	 * Implicitome disabled for now...
	 * 
	 * @return the implicitRelationsTab

	public VerticalLayout getImplicitRelationsTab() {
		return implicitRelationsTab;
	}
	 */
	
	/**
	 * April 21, Implicitome removed for now
	 * 
	 * @return the cooccurrencesTab

	public VerticalLayout getCooccurrencesTab() {
		return cooccurrencesTab;
	}
	*/

	public VerticalLayout getEvidenceTab() {
		return evidenceTab ;
	}
	
	public VerticalLayout getReferenceTab() {
		return referenceTab ;
	}
	
	
	public HorizontalLayout getCmPanel(){
		return cmPanel;
	}
	
	public HorizontalLayout getViewingConcepts() {
		return viewingConcepts;
	}
	
	public NativeSelect getColorSelect() {
		return colorSelect;
	}
	
	public VerticalLayout getReferenceLayout() {
		return referenceLayout;
	}

	public void setReferenceLayout(VerticalLayout referenceLayout) {
		this.referenceLayout = referenceLayout;
	}
	
	public NativeSelect getCmLayoutSelect() {
		return cmLayoutSelect;
	}
	
	public void setDesktopSplitPanel(VerticalSplitPanel splitPanel) {
		this.desktopSplitPanel = splitPanel ;
	}
	
	public VerticalSplitPanel getDesktopSplitPanel() {
		return desktopSplitPanel ;
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

	/**
	 * @param 
	 */
	public void setShowLegendBtn(Button showLegendBtn) {
		this.showLegendBtn = showLegendBtn;
	}
	
	public Label getConceptMapNameLabel() {
		return this.conceptMapNameLabel;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		if (UI.getCurrent() instanceof DesktopUI) {
			DesktopUI ui = (DesktopUI) UI.getCurrent();
			
			String state = ui.getNavigator().getState();
			
			System.out.println("state is: " + state);
			
			// To make sure it displays the relations table when 
			// navigated back to this view's URI fragment.
			if (state.equals(DesktopView.NAME) || 
					(state.equals(ListView.NAME + "/" + ViewName.RELATIONS_VIEW))) {				
				ui.gotoStatementsTable();
			}
			
			// TODO: Rework the above code to fit into the below code.
			// This should allow the user navigate to a view simply by
			// using the URL (maybe they send a URL to a friend).
			if (state.startsWith(ListView.NAME + "/" + ViewName.RELATIONS_VIEW)) {
				String conceptId = getStateSegment(state, 2);
				ui.displayStatements(conceptId);
			} else if (state.startsWith(ReferenceView.NAME)) {
				String annotationId = getStateSegment(state, 1);
				ui.displayReference(annotationId);
			}
		}
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
