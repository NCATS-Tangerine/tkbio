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
package bio.knowledge.web.view.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class PageSizeSelectorBar extends HorizontalLayout {

	private static final long serialVersionUID = -5976734724866862274L;
	
	private final static String PAGE_BUTTON_STYLE = "page-button";
	private final static String PAGE_STATUS_LABEL_STYLE = "page-status-label";
	
	private ListContainer listContainer;
	private PageSelectorBar pageSelectorBar;
	private Label currentStatusLabel = new Label() ;
	

	public PageSizeSelectorBar(ListContainer listContainer, PageSelectorBar pageSelectorBar) {
		this.listContainer = listContainer;
		this.pageSelectorBar = pageSelectorBar;
		
//		this.setSizeFull();
		
		setupLayout();
	}


	private void setupLayout() {
		this.removeAllComponents();
		
		long totalHits = listContainer.getTotalHits();

		HorizontalLayout entriesLayout = new HorizontalLayout();
		entriesLayout.addStyleName("max-width-full");

		Label entriesLabel = new Label("entries per page:");
		entriesLabel.addStyleName("page-entries-label");

		entriesLayout.addComponent(entriesLabel);
		entriesLayout.setComponentAlignment(entriesLabel, Alignment.MIDDLE_LEFT);

		int[] pageSizes = { 5, 10, 25, 50, 100 };
		for (int ps : pageSizes) {
			Button pageButton = new Button(new Integer(ps).toString().trim());

			// pageButton.addClickListener(e -> pageSizeSelector(e));

			pageButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					String value = event.getButton().getCaption();

					int pageSize = 10;
					try {
						pageSize = Integer.parseInt(value);
					} catch (NumberFormatException nfe) {
						pageSize = 10;
					}
					listContainer.setPageSize(pageSize);
					
					pageSelectorBar.goToFirstPage();
					
//					Re-setup everything
					setupLayout();
				}
			});

			if (listContainer.getPageSize() == ps) {
				pageButton.addStyleName("current-page-size");
			} else {
				pageButton.addStyleName(PAGE_BUTTON_STYLE);
			}

			entriesLayout.addComponent(pageButton);
			entriesLayout.setComponentAlignment(pageButton, Alignment.MIDDLE_LEFT);

			// Only give a page size range that is sensible
			// relative to the total number of pages
			if (totalHits < ps)
				break;
		}

		// the page/entry status label
		String[] range = listContainer.getEntryRange();
		String labelContent = "Showing " + range[0] + " to " + range[1] + " of " + totalHits + " entries";

		currentStatusLabel.setValue(labelContent);
		currentStatusLabel.addStyleName(PAGE_STATUS_LABEL_STYLE);

		this.addComponents(currentStatusLabel, entriesLayout);

		this.setComponentAlignment(currentStatusLabel, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(entriesLayout, Alignment.MIDDLE_RIGHT);

		this.setExpandRatio(currentStatusLabel, 4);
		this.setExpandRatio(entriesLayout, 7);
	}

}
