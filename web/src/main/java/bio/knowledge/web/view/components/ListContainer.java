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

import java.io.Serializable;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.UI;

import bio.knowledge.authentication.AuthenticationManager;
import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.user.User;
import bio.knowledge.service.AuthenticationState;
import bio.knowledge.service.ConceptMapArchiveService;
import bio.knowledge.service.core.ListTableEntryCounter;
import bio.knowledge.service.core.ListTableFilteredHitCounter;
import bio.knowledge.service.core.ListTablePageCounter;
import bio.knowledge.service.core.ListTablePager;
import bio.knowledge.service.core.TableSorter;
import bio.knowledge.web.ui.DesktopUI;

class ListContainer implements Serializable {

	private static final long serialVersionUID = -1922666185642169173L;

	public final static int PAGE_WINDOW_SIZE = 5;

	public static final int PAGE_WINDOW_OFFSET = PAGE_WINDOW_SIZE / 2;
	
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_CURRENT_PAGE_INDEX = 0;
	private static final boolean DEFAULT_IS_ASCENDING = false;

	private BeanItemContainer<IdentifiedEntity> container = null ;
	
	private int currentPageIndex = DEFAULT_CURRENT_PAGE_INDEX;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private int totalPages = 0;
	
	private String simpleTextFilter = "";
	
	// By default, it will be false to list predication in descending order of evidence count
	private boolean isAscending = DEFAULT_IS_ASCENDING;
	private TableSorter sorter = TableSorter.DEFAULT;

	/**
	 * Creates a new ListContainer.
	 * <b>userProfile</b> is the user profile to search concept maps by.
	 * @param userProfile
	 */
	protected ListContainer(User user) {
	}
	
	public Boolean isEmpty() {
		if (container  == null || container.size() == 0)
			return Boolean.TRUE ;
		else
			return Boolean.FALSE ;
	}
	
	@SuppressWarnings({ "unchecked"})
	public void setContainer( BeanItemContainer<? extends IdentifiedEntity> container ) {
		// Not sure how to best deal with this issue...
		this.container = (BeanItemContainer<IdentifiedEntity>)container ;
	}
	
	public Container.Indexed getContainer() {
		// May get a fresh page...
		refresh() ;
		return container ;
	}

	/**
	 * @param pager
	 */
	private ListTablePager<? extends IdentifiedEntity> pager = null ;
	public void setPager( ListTablePager<? extends IdentifiedEntity> pager ) {
		this.pager = pager ;
	}
	
	/**
	 */
	private ListTableEntryCounter entryCounter = null ;
	public void setEntryCounter( ListTableEntryCounter entryCounter ) {
		this.entryCounter = entryCounter ;
	}
	
	/**
	 */
	private ListTableFilteredHitCounter hitCounter = null ;
	public void setHitCounter( ListTableFilteredHitCounter hitCounter ) {
		this.hitCounter = hitCounter ;
	}
	
	/**
	 * @param pager
	 */
	private ListTablePageCounter pageCounter = null ;
	public void setPageCounter( ListTablePageCounter pageCounter ) {
		this.pageCounter = pageCounter ;
	}
	
	/**
	 * @param pageNo
	 * @param pageSize
	 */
	public void setCurrentPageIndex(int pageIndex) {
		this.currentPageIndex = pageIndex;
	}
	
	/**
	 * Sets the page index to be the first page.
	 */
	public void setFirstPage() {
		setCurrentPageIndex(0);
	}
	
	/**
	 * Initialize fields, such as page index and size, to be the default. 
	 */
	public void initializeFields() {
		isAscending = DEFAULT_IS_ASCENDING;

		pageSize = DEFAULT_PAGE_SIZE;
		currentPageIndex = DEFAULT_CURRENT_PAGE_INDEX;
	}
	
	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize ;
	}
	
	public void setSimpleTextFilter ( String textfilter ){
		this.simpleTextFilter = textfilter;
	}
	
	public void setDirection ( boolean isAscending ){
		this.isAscending = isAscending;
	}
	
//	public void refresh() {
//		if( pager != null ) {
//			if( container.removeAllItems() )
//				container.addAll(
//						pager.getDataPage(currentPageIndex, pageSize, simpleTextFilter, sorter,
//						isAscending, false, authenticationManager.getCurrentAccountsEmail() )  );
//		}
//	}
	
	public void refresh(ConceptMapArchiveService.SearchMode searchMode) {
		DesktopUI ui = (DesktopUI) UI.getCurrent();
		ui.getConceptMapArchiveService().setSearchMode(searchMode);
		refresh();
	}

	public void refresh() {		
		if (pager != null) {
			if (container.removeAllItems()) {
				DesktopUI ui = (DesktopUI) UI.getCurrent();
				AuthenticationManager authenticationManager = ui.getAuthenticationManager();
				AuthenticationState authenticationState = ui.getAuthenticationState();
				
				if (authenticationManager.isUserAuthenticated()) {
					User user = authenticationManager.getCurrentUser();
					authenticationState.setState(user.getId(), user.getIdsOfGroupsBelongedTo());
				} else {
					authenticationState.setState(null, null);
				}

				container.addAll(pager.getDataPage(currentPageIndex, pageSize, simpleTextFilter, sorter, isAscending));
			}
		}
	}

	/**
	 * 
	 * @return total number of data entries in the current list view data table
	 */
	public long getTotalEntries() {
		return entryCounter!=null ? entryCounter.countEntries() : 0 ;
	}
	
	/**
	 * 
	 * @return total number of data entries in the current list view data table
	 */
	public long getTotalHits() {
		return hitCounter!=null ? hitCounter.countHits(simpleTextFilter) : 0 ;
	}

	/**
	 * 
	 * @return total number of data pages from current list view data table
	 */
	public int getPageCount() {
		if (pageCounter == null) {
			totalPages = 0;
		} else {
			totalPages = pageCounter.countPages(simpleTextFilter, pageSize);
		}
		return totalPages;
	}

	/**
	 * @return
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public String getSimpleTextFilter() {
		return simpleTextFilter;
	}
	
	public boolean getDirection() {
		return isAscending;
	}
	
	public String[] getEntryRange() {
		long start = currentPageIndex * pageSize + 1;
		long end   = (currentPageIndex + 1) * pageSize;
		Long totalHits = getTotalHits();
		end = end < totalHits ? end : totalHits ;
		
		if(start > end) start = end ; // end is probably zero?
		
		String[] range = new String[2] ;
		range[0] = new Long(start).toString() ;
		range[1] = new Long(end).toString() ;
		return range ;
	}
	
	

	/**
	 * @return integer size of page
	 */
	public int getPageSize() {
		return this.pageSize;
	}
	
	public void setSorter(TableSorter sorter) {
		this.sorter = sorter;
	}
}