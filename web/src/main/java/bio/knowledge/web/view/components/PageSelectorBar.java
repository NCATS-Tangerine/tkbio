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

import java.util.Iterator;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

public class PageSelectorBar extends HorizontalLayout {
	private ListContainer listContainer;

	public PageSelectorBar(ListContainer listContainer) {
		this.listContainer = listContainer;
//		this.setSizeFull();
		createPageControls();
	}
	
	public void goToFirstPage() {
		this.gotoPageIndex(0);
	}

	private void gotoPageIndex(int pageIndex) {
		listContainer.setCurrentPageIndex(pageIndex);
		listContainer.refresh();
		
		this.removeAllComponents();
		// refresh page controls
		createPageControls();
	}

	private void createPageControls() {
		int totalPages = listContainer.getPageCount();

		if (totalPages == 0) {
			return;
		}

		int offset = ListContainer.PAGE_WINDOW_OFFSET;

		int currentPageIndex = listContainer.getCurrentPageIndex();
		int finalPageIndex = totalPages - 1;

		int windowStartIndex = currentPageIndex - offset;
		int windowEndIndex = currentPageIndex + offset;

		// clip the window
		if (windowStartIndex < 0) {
			windowStartIndex = 0;
			windowEndIndex = ListContainer.PAGE_WINDOW_SIZE - 1;
		} else if (windowEndIndex >= totalPages) {
			windowStartIndex = totalPages - ListContainer.PAGE_WINDOW_SIZE;
			windowEndIndex = totalPages - 1;
		}

		// display one more button if the window is at one of the end
		if (windowStartIndex == 0) {
			windowEndIndex++;
		} else if (windowEndIndex == finalPageIndex) {
			windowStartIndex--;
		}

		// update window end index if there are few pages
		if (totalPages <= ListContainer.PAGE_WINDOW_SIZE) {
			windowStartIndex = 0;
			windowEndIndex = finalPageIndex;
		}

		// set up the Previous button
		Button previousBtn = new Button("previous");

		previousBtn.setEnabled(false);
		previousBtn.addStyleName("pagecontrol-button");

		previousBtn.addClickListener(e -> {
			int pageIndex = listContainer.getCurrentPageIndex() - 1;
			gotoPageIndex(pageIndex);
		});

		// set up the Next button
		Button nextBtn = new Button("next");

		nextBtn.setEnabled(false);
		nextBtn.addStyleName("pagecontrol-button");

		nextBtn.addClickListener(e -> {
			int pageIndex = listContainer.getCurrentPageIndex() + 1;
			gotoPageIndex(pageIndex);
		});

		if (currentPageIndex > 0) {
			previousBtn.setEnabled(true);
		}

		if (currentPageIndex < (totalPages - 1)) {
			nextBtn.setEnabled(true);
		}

		this.addComponent(previousBtn);

		// create the first page button, and ellipsis if applicable (before the
		// window)
		if (windowStartIndex >= 1) {
			Button firstPageBtn = new Button("1");
			firstPageBtn.addStyleName("page-button");
			firstPageBtn.addClickListener(e -> {
				gotoPageIndex(0);
			});

			this.addComponent(firstPageBtn);

			if (windowStartIndex >= 2) {
				Label ellipsisLabel = new Label("...");
				ellipsisLabel.addStyleName("page-ellipsis-label");

				this.addComponent(ellipsisLabel);
				this.setComponentAlignment(ellipsisLabel, Alignment.MIDDLE_CENTER);
			}
		}

		// create the buttons within the window's range
		for (int i = windowStartIndex; i <= windowEndIndex; i++) {
			Button pageBtn = new Button(Integer.toString(i + 1));
			pageBtn.addStyleName("page-button");
			pageBtn.addClickListener(e -> {
				int pageIndex = Integer.parseInt(e.getButton().getCaption()) - 1;
				gotoPageIndex(pageIndex);
			});

			this.addComponent(pageBtn);
		}

		// create the last page button, and ellipsis if applicable (after the
		// window)

		if (windowEndIndex <= (finalPageIndex - 1)) {
			Button finalPageBtn = new Button(Integer.toString(finalPageIndex + 1));
			finalPageBtn.addStyleName("page-button");
			finalPageBtn.addClickListener(e -> {
				gotoPageIndex(finalPageIndex);
			});

			if (windowEndIndex <= (finalPageIndex - 2)) {
				Label ellipsisLabel = new Label("...");
				ellipsisLabel.addStyleName("page-ellipsis-label");

				this.addComponent(ellipsisLabel);
				this.setComponentAlignment(ellipsisLabel, Alignment.MIDDLE_CENTER);
			}

			this.addComponent(finalPageBtn);
		}

		// set current button style
		Iterator<Component> iterator = this.iterator();

		while (iterator.hasNext()) {
			Component component = iterator.next();
			if (component.getClass().equals(Button.class)) {
				Button button = (Button) component;
				String caption = button.getCaption();

				if (caption.equals(Integer.toString(currentPageIndex + 1))) {
					button.addStyleName("current-page-button");
				}
			}
		}

		this.addComponent(nextBtn);
	}
}
