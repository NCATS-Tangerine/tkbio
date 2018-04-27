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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.apache.jena.riot.adapters.AdapterRDFWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.ItemSorter;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;

import bio.knowledge.authentication.AuthenticationManager;
import bio.knowledge.model.AnnotatedConcept;
import bio.knowledge.model.AnnotatedConceptImpl;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.BeaconResponse;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.ConceptType;
import bio.knowledge.model.DisplayableStatement;
import bio.knowledge.model.DomainModelException;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.IdentifiedConceptImpl;
import bio.knowledge.model.Library;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.Statement;
import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.core.OntologyTerm;
import bio.knowledge.model.organization.ContactForm;
import bio.knowledge.model.umls.SemanticType;
import bio.knowledge.model.user.User;
import bio.knowledge.model.util.Util;
import bio.knowledge.service.ConceptMapArchiveService;
import bio.knowledge.service.ConceptMapArchiveService.SearchMode;
import bio.knowledge.service.DataServiceException;
import bio.knowledge.service.KBQuery.LibrarySearchMode;
import bio.knowledge.service.KBQuery.RelationSearchMode;
import bio.knowledge.service.PredicateService;
import bio.knowledge.service.beacon.KnowledgeBeaconRegistry;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.service.core.ListTableEntryCounter;
import bio.knowledge.service.core.ListTableFilteredHitCounter;
import bio.knowledge.service.core.ListTablePageCounter;
import bio.knowledge.service.core.ListTablePager;
import bio.knowledge.service.core.TableSorter;
import bio.knowledge.web.ui.ConceptDetailsHandler;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.ui.PopupWindow;

/**
 * @author Richard
 * @author Yinglun(Colin) Qiao
 *
 */
@SpringView(name = ListView.NAME)
public class ListView extends BaseView implements Util {
	
	public static final String NAME = "list";

	private static final long serialVersionUID = 4845614961187807437L ;
	
	private Logger _logger = LoggerFactory.getLogger(ListView.class);

	// column ids (property ids) for data table
	private static final String COL_ID_SUPPORTING_TEXT = "supportingText";
	private static final String COL_ID_EVIDENCE  = "evidence";
	private static final String COL_ID_RELATION  = "relation";
	private static final String COL_ID_OBJECT    = "object";
	private static final String COL_ID_SUBJECT   = "subject";
	private static final String COL_ID_DETAILS  = "details";
	private static final String COL_ID_REFERENCE = "reference";
	private static final String COL_ID_PUBLICATION_DATE = "publicationDate";

	private static final int ELLIPSIS_INDEX_OFFSET = 2;
	// style names
	private static final String PAGE_STATUS_LABEL_STYLE = "page-status-label";
	private static final String CURRENT_PAGE_BUTTON_STYLE = "current-page-button";
	private static final String CURRENT_PAGE_SIZE_STYLE = "current-page-size";
	private static final String PAGE_BUTTON_STYLE = "page-button";
	private static final String PAGE_CONTROL_BUTTON_STYLE = "pagecontrol-button";

	
	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;
	
	/*
	 *  RMB: Oct 27, 2017: reduced table height to 8 from 11 
	 *  when data table moved to 100% width of bottom pane
	 */
	private static final int ROWS_TO_DISPLAY = 8; 
	
	@Autowired
	KnowledgeBeaconRegistry kbRegistry;
	
	@Autowired
	PredicateService predicateService;
	
	// Wrapper for datasource container,
	// to add extra action columns for 'details', 'data download', etc.
	private GeneratedPropertyContainer gpcontainer;

	// Identification/Unique Keys
	private Grid dataGrid = null;

	// view's name
	private String viewName = "";


	// Names of POJO properties to be displayed
	// in the 'details' pane for given datatype
	private List<String> detailFields;

	// Expanded properties of given identity for
	private GridLayout detailsPane = null;

	// Contains items and details for sake of animation.
	private Component listViewContents = null;

	// Animated gif for "query-in-progress"
	String BUSY_SIGNAL = "images/flower-loader.gif";

	private TextField simpleTextFilter = new TextField();

	@PostConstruct
	protected void initialize() {
		this.registerViews();
		setSizeFull();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.
	 * ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		this.viewName = event.getParameters();
		renderDisplay();
	}

	class ListContainer implements Serializable {

		private static final long serialVersionUID = -1922666185642169173L;

		public final static int PAGE_WINDOW_SIZE = 5;
		
		private static final int PAGE_WINDOW_OFFSET = PAGE_WINDOW_SIZE / 2;

		private static final int DEFAULT_PAGE_SIZE = 15;
		private static final int DEFAULT_CURRENT_PAGE_INDEX = 0;
		private static final boolean DEFAULT_IS_ASCENDING = false;

		private BeanItemContainer<IdentifiedEntity> container = null;

		private int currentPageIndex = DEFAULT_CURRENT_PAGE_INDEX;
		private int pageSize = DEFAULT_PAGE_SIZE;
		private int totalPages = 0;

		private String simpleTextFilter = "";

		// By default, it will be false to list predication in descending order
		// of evidence count
		private boolean isAscending = DEFAULT_IS_ASCENDING;
		private TableSorter sorter = TableSorter.DEFAULT;

		protected ListContainer() {
		}

		public Boolean isEmpty() {
			if (container == null || container.size() == 0)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		}

		@SuppressWarnings({ "unchecked" })
		public void setContainer(BeanItemContainer<? extends IdentifiedEntity> container) {
			// Not sure how to best deal with this issue...
			this.container = (BeanItemContainer<IdentifiedEntity>) container;
		}

		public Container.Indexed getContainer() {
			// May get a fresh page...
			refresh();
			return container;
		}

		/**
		 * @param pager
		 */
		private ListTablePager<? extends IdentifiedEntity> pager = null;

		public void setPager(ListTablePager<? extends IdentifiedEntity> pager) {
			this.pager = pager;
		}

		/**
		 */
		private ListTableEntryCounter entryCounter = null;

		public void setEntryCounter(ListTableEntryCounter entryCounter) {
			this.entryCounter = entryCounter;
		}

		/**
		 */
		private ListTableFilteredHitCounter hitCounter = null;

		public void setHitCounter(ListTableFilteredHitCounter hitCounter) {
			this.hitCounter = hitCounter;
		}

		/**
		 * @param pager
		 */
		private ListTablePageCounter pageCounter = null;

		public void setPageCounter(ListTablePageCounter pageCounter) {
			this.pageCounter = pageCounter;
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

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public void setSimpleTextFilter(String textfilter) {
			this.simpleTextFilter = textfilter;
		}

		public void setDirection(boolean isAscending) {
			this.isAscending = isAscending;
		}
		
		public void refresh(ConceptMapArchiveService.SearchMode searchMode) {
			conceptMapArchiveService.setSearchMode(searchMode);
			refresh();
		}
		
		private int countKnowledgeBeacons() {
			List<String> beacons = query.getCustomBeacons();
			return kbService.getKnowledgeBeaconCount(beacons);
		}
		
		public String makeFilter() {
			String filter;
			if (viewName.equals(ViewName.CONCEPTS_VIEW)) {
				filter = DesktopUI.getCurrent().getDesktop().getSearch().getValue();
			} else {
				filter = "";
			}
			
			if(!simpleTextFilter.isEmpty()) {
				filter += " " + simpleTextFilter ;
			}
			
			return filter;
		}

		public void refresh() {
			if (pager != null) {
				if (container.removeAllItems()) {
					AuthenticationManager authenticationManager = ((DesktopUI) UI.getCurrent()).getAuthenticationManager();
					
					if (authenticationManager.isUserAuthenticated()) {

						User user = authenticationManager.getCurrentUser();
						authenticationState.setState(user.getId(), user.getIdsOfGroupsBelongedTo());

					} else {
						authenticationState.setState(null, null);
					}
					
					String filter = makeFilter();
					int pageNumber = currentPageIndex + 1;
					if (filter.isEmpty() && viewName.equals(ViewName.CONCEPTS_VIEW)){
						// We don't want to initiate an empty search when we load the CONCEPTS_VIEW page
					} else {
						container.addAll(pager.getDataPage(pageNumber, pageSize, filter, sorter, isAscending));
					}
					
					//TODO: DEC 15 2018
//					sortDataTable();
				}
			}
			
			createPageControls(listContainer);
		}
		
		public void sortDataTable() {
			
			for (SortOrder order : dataGrid.getSortOrder()) {
				dataGrid.sort(order.getPropertyId(), order.getDirection());
			}
		}

		/**
		 * 
		 * @return total number of data entries in the current list view data
		 *         table
		 */
		public long getTotalEntries() {
			return entryCounter != null ? entryCounter.countEntries() : 0;
		}

		/**
		 * 
		 * @return total number of data entries in the current list view data
		 *         table
		 */
		public long getTotalHits() {
			return 1000;
			//TODO: DEC 15 2018 commented this line out
//			return hitCounter != null ? hitCounter.countHits(simpleTextFilter) : 0;
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
			long end = (currentPageIndex + 1) * pageSize;
			Long totalHits = getTotalHits();
			end = end < totalHits ? end : totalHits;

			if (start > end)
				start = end; // end is probably zero?

			String[] range = new String[2];
			range[0] = new Long(start).toString();
			range[1] = new Long(end).toString();
			return range;
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

	/**
	 * Child classes of ListView can override this method as a mechanism for
	 * child classes to add more columns of extra-bean functionality to a table
	 * after the Bean data and standard additional columns (of 'Show' and
	 * 'Download')
	 * 
	 * @param dataTable
	 *            to which to add data columns
	 * @param gpcontainer
	 *            wrapper container for extra table properties (like action
	 *            buttons)
	 */
	protected void addChildColumns(Grid dataTable, GeneratedPropertyContainer gpcontainer) {
		/* do nothing in ListView */ }

	// The Bean data container of the current view
	private ListContainer listContainer = new ListContainer();

	private void gotoPageIndex(int pageIndex) {

		listContainer.setCurrentPageIndex(pageIndex);
		listContainer.refresh();

		// refresh page controls
		createPageControls(listContainer);
	}
	
	private void pageSizeSelector(ClickEvent e) {

		String value = e.getButton().getCaption();

		int pageSize = 10;
		try {
			pageSize = Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			pageSize = 10;
		}
		listContainer.setPageSize(pageSize);

		gotoPageIndex(0);
	}
	
	/**
	 * TODO: this method does not yet take 'filtered' hit numbers into account
	 *
	 * @param listContainer
	 */
	private void createPageControls(ListContainer listContainer) {

		pageBar.removeAllComponents();
		enPageBar.removeAllComponents();

		enPageBar.setWidth("100%");

		// entries per page label and buttons
		createPageEntriesBar(listContainer);

		// buttons for selecting pages
		createPageButtons(listContainer);

		pageBar.addStyleName("max-width-full");
		pageBar.setResponsive(true);
	}
	
	private Label currentStatusLabel = new Label();
	
	private void createPageEntriesBar(ListContainer listContainer) {
		long totalHits = listContainer.getTotalHits();

		HorizontalLayout entriesLayout = new HorizontalLayout();
		entriesLayout.addStyleName("max-width-full");

		Label entriesLabel = new Label(getMessage("entries_per_page") + ":");
		entriesLabel.addStyleName("page-entries-label");

		entriesLayout.addComponent(entriesLabel);
		entriesLayout.setComponentAlignment(entriesLabel, Alignment.MIDDLE_LEFT);

		int[] pageSizes = { 5, 10, 25, 50, 100 };
		for (int ps : pageSizes) {
			Button pageButton = new Button(new Integer(ps).toString().trim());
			pageButton.addClickListener(e -> pageSizeSelector(e));

			if (listContainer.getPageSize() == ps) {
				pageButton.addStyleName(CURRENT_PAGE_SIZE_STYLE);
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

		enPageBar.addComponents(currentStatusLabel, entriesLayout);

		enPageBar.setComponentAlignment(currentStatusLabel, Alignment.MIDDLE_LEFT);
		enPageBar.setComponentAlignment(entriesLayout, Alignment.MIDDLE_RIGHT);

		enPageBar.setExpandRatio(currentStatusLabel, 4);
		enPageBar.setExpandRatio(entriesLayout, 7);
	}
	
	private void createPageButtons(ListContainer listContainer) {
		//TODO: DEC 15 2018
//		int totalPages = listContainer.getPageCount();
		int totalPages = 100;

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
		Button previousBtn = new Button(getMessage("button_previous"));

		previousBtn.setEnabled(false);
		previousBtn.addStyleName(PAGE_CONTROL_BUTTON_STYLE);

		previousBtn.addClickListener(e -> {
			int pageIndex = listContainer.getCurrentPageIndex() - 1;
			gotoPageIndex(pageIndex);
		});

		// set up the Next button
		Button nextBtn = new Button(getMessage("button_next"));

		nextBtn.setEnabled(false);
		nextBtn.addStyleName(PAGE_CONTROL_BUTTON_STYLE);

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

		pageBar.addComponent(previousBtn);

		// create the first page button, and ellipsis if applicable (before the
		// window)
		if (windowStartIndex >= 1) {
			Button firstPageBtn = new Button("1");
			firstPageBtn.addStyleName(PAGE_BUTTON_STYLE);
			firstPageBtn.addClickListener(e -> {
				gotoPageIndex(0);
			});

			pageBar.addComponent(firstPageBtn);

			if (windowStartIndex >= ELLIPSIS_INDEX_OFFSET) {
				Label ellipsisLabel = new Label("...");
				ellipsisLabel.addStyleName("page-ellipsis-label");

				pageBar.addComponent(ellipsisLabel);
				pageBar.setComponentAlignment(ellipsisLabel, Alignment.MIDDLE_CENTER);
			}
		}

		// create the buttons within the window's range
		for (int i = windowStartIndex; i <= windowEndIndex; i++) {
			Button pageBtn = new Button(Integer.toString(i + 1));
			pageBtn.addStyleName(PAGE_BUTTON_STYLE);
			pageBtn.addClickListener(e -> {
				int pageIndex = Integer.parseInt(e.getButton().getCaption()) - 1;
				gotoPageIndex(pageIndex);
			});

			pageBar.addComponent(pageBtn);
		}

		// create the last page button, and ellipsis if applicable (after the
		// window)

		if (windowEndIndex <= (finalPageIndex - 1)) {
			Button finalPageBtn = new Button(Integer.toString(finalPageIndex + 1));
			finalPageBtn.addStyleName(PAGE_BUTTON_STYLE);
			finalPageBtn.addClickListener(e -> {
				gotoPageIndex(finalPageIndex);
			});

			if (windowEndIndex <= (finalPageIndex - ELLIPSIS_INDEX_OFFSET)) {
				Label ellipsisLabel = new Label("...");
				ellipsisLabel.addStyleName("page-ellipsis-label");

				pageBar.addComponent(ellipsisLabel);
				pageBar.setComponentAlignment(ellipsisLabel, Alignment.MIDDLE_CENTER);
			}

			pageBar.addComponent(finalPageBtn);
		}

		// set current button style
		Iterator<Component> iterator = pageBar.iterator();

		while (iterator.hasNext()) {
			Component component = iterator.next();
			if (component.getClass().equals(Button.class)) {
				Button button = (Button) component;
				String caption = button.getCaption();

				if (caption.equals(Integer.toString(currentPageIndex + 1))) {
					button.addStyleName(CURRENT_PAGE_BUTTON_STYLE);
				}
			}
		}

		pageBar.addComponent(nextBtn);
	}

	private HorizontalLayout pageBar = new HorizontalLayout();
	private HorizontalLayout enPageBar = new HorizontalLayout();

	private void loadDataTable(VerticalLayout dataTableLayout) {

		@SuppressWarnings("rawtypes")
		BeanItemContainer container = (BeanItemContainer) listContainer.getContainer();

		gpcontainer = new GeneratedPropertyContainer(container);
		
		gpcontainer.addGeneratedProperty("type", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = 2919141699691390192L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				if (itemId instanceof IdentifiedConcept) {
					IdentifiedConcept concept = (IdentifiedConcept) itemId;
					return concept.getType().toString();
				} else {
					return "";
				}
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});
		
		gpcontainer.addGeneratedProperty("beaconSource", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = -2448949798858851325L;

			@Override
			public String getValue(Item item, Object object, Object propertyId) {
				if (object instanceof BeaconResponse) {
					BeaconResponse beaconResponse = (BeaconResponse) object;
					return beaconResponse.getBeaconSource();
				}
				return "";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});

		gpcontainer.addGeneratedProperty("subjectId", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = 2393111751549742042L;

			@Override
			public String getValue(Item item, Object object, Object propertyId) {
				if (object instanceof DisplayableStatement) {
					DisplayableStatement c = (DisplayableStatement) object;
					return c.getSubject().getId();
				}
				return "";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});

		gpcontainer.addGeneratedProperty("subjectClique", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = 5174351710679478588L;

			@Override
			public String getValue(Item item, Object object, Object propertyId) {
				if (object instanceof DisplayableStatement) {
					DisplayableStatement c = (DisplayableStatement) object;
					return c.getSubject().getCliqueId();
				}
				return "";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});

		gpcontainer.addGeneratedProperty("objectId", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = 5437937835853569650L;

			@Override
			public String getValue(Item item, Object object, Object propertyId) {
				if (object instanceof DisplayableStatement) {
					DisplayableStatement c = (DisplayableStatement) object;
					return c.getObject().getId();
				}
				return "";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});

		gpcontainer.addGeneratedProperty("objectClique", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = 8058294339364614146L;

			@Override
			public String getValue(Item item, Object object, Object propertyId) {
				if (object instanceof DisplayableStatement) {
					DisplayableStatement c = (DisplayableStatement) object;
					return c.getObject().getCliqueId();
				}
				return "";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});

		gpcontainer.addGeneratedProperty(COL_ID_DETAILS, new PropertyValueGenerator<String>() {
			
			private static final long serialVersionUID = 8058294339364614146L;
			
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return "show";
			}
			
			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		
		// Create a header row to hold column filters
		// HeaderRow filterRow = dataTable.appendHeaderRow();

		if (!detailFields.isEmpty()) {
			/*
			 * TODO: need a generic name for the identifier column, such that
			 * the details button will always know what to point to. TODO:
			 * should I be overriding the container or should I be creating a
			 * new container? TODO: Do I really need the
			 * GeneratedPropertyContainer?
			 */
			// Details pane: we expect two columns: one for keys
			// and one for values on the introspected bean.
			detailsPane = new GridLayout();

			detailsPane.setSpacing(true);
			detailsPane.setMargin(true);

			detailsPane.setColumns(2);
			detailsPane.addStyleName("outlined");
			detailsPane.setSizeFull();

			dataGrid.addColumn("show", Resource.class).setRenderer(new ImageRenderer());
			dataGrid.getHeaderRow(0).getCell("show").setStyleName(ViewUtil.HEADER_STYLENAME);

			gpcontainer.addGeneratedProperty("show", new PropertyValueGenerator<Resource>() {
				private static final long serialVersionUID = 3271937727404606863L;

				@Override
				public Resource getValue(Item item, Object itemId, Object propertyId) {
					return new ThemeResource("icons/show.png");
				}

				@Override
				public Class<Resource> getType() {
					return Resource.class;
				}

			});

			ViewUtil.makeIconButton(dataGrid, "show", e -> onShowDetails(e));

			HorizontalSplitPanel listAndDetailsSplitPanel = new HorizontalSplitPanel();
			listAndDetailsSplitPanel.setSizeFull();
			listAndDetailsSplitPanel.setLocked(true);
			listAndDetailsSplitPanel.setSplitPosition(100, Unit.PERCENTAGE);
			listAndDetailsSplitPanel.setFirstComponent(dataTableLayout);
			listAndDetailsSplitPanel.setSecondComponent(detailsPane);

			listViewContents = listAndDetailsSplitPanel;

		} else {
			// just add in the vanilla container
			listViewContents = dataTableLayout; // only the data table, no
												// details
		}
		
		gpcontainer.addGeneratedProperty(
				"author", 
				new PropertyValueGenerator<String>() {

					private static final long serialVersionUID = -2448949798858851325L;

					@Override
					public String getValue(Item item, Object itemId, Object propertyId) {
						
						ConceptMapArchive map = (ConceptMapArchive) itemId;
						
						String userId = map.getAuthorsAccountId();
						if (userId != null) {
							User userProfile = DesktopUI.getCurrent().getAuthenticationManager().getUser(userId);
							
							if (userProfile != null) {
								return (String) userProfile.getUsername();
							} else {
								return null;
							}
						} else {
							return null;
						}
					}
		
					@Override
					public Class<String> getType() {
						return String.class;
					}
					
				});

		addChildColumns(dataGrid, gpcontainer);

		dataGrid.setContainerDataSource(gpcontainer);
		
		for (Column column : dataGrid.getColumns()) {
			String columnID = (String) column.getPropertyId();
			column.setSortable(
					columnID.equals(COL_ID_SUBJECT) ||
					columnID.equals(COL_ID_OBJECT)  ||
					columnID.equals(COL_ID_RELATION)
			);
		}
		
		container.setItemSorter(new ItemSorter() {

			private static final long serialVersionUID = -6433226581531591726L;

			@Override
			public void setSortProperties(Sortable container, Object[] propertyId, boolean[] ascending) {
				
			}

			@Override
			public int compare(Object a, Object b) {
				List<SortOrder> sortOrders = dataGrid.getSortOrder();
				if (sortOrders.isEmpty()) {
					return 0;
				} else {
					SortOrder sortOrder = sortOrders.get(0);
					String columnID = ((String) sortOrder.getPropertyId()).trim();
					
					if (viewName.startsWith(ViewName.RELATIONS_VIEW)) {
						Statement A = (Statement) a;
						Statement B = (Statement) b;
						
						if (columnID.equals(COL_ID_SUBJECT)) {
							return A.getSubject().getName().compareToIgnoreCase(B.getSubject().getName());
						} else if (columnID.equals(COL_ID_OBJECT)) {
							return A.getObject().getName().compareToIgnoreCase(B.getObject().getName());
						} else if (columnID.equals(COL_ID_RELATION)) {
							return A.getRelation().getName().compareToIgnoreCase(B.getRelation().getName());
						} else {
							return 0;
						}
					} else {
						return 0;
					}
				}
			}
			
		});

		listContainer.setFirstPage();

		createPageControls(listContainer);

		dataTableLayout.addComponent(pageBar);
		dataTableLayout.addComponent(enPageBar);
		dataTableLayout.setSizeFull();

		addComponents(listViewContents);
	}

	private TabSheet makeTabSheetController() {
		// This is a hack to bypass tabs requiring and being identified by their
		// content. We're creating invisible labels to use as the content of each
		// tab. We only want the functionality of this tabsheet, we don't want to 
		// visibly put anything in it!
		TabSheet tabsheet = new TabSheet();
		Label publicHiddenLabel = new Label("");
		Label personalHiddenLabel = new Label("");
		Label groupHiddenLabel = new Label("");
		tabsheet.addTab(personalHiddenLabel, "Maps You've Authored");
		tabsheet.addTab(groupHiddenLabel, "Maps Shared With You");
		tabsheet.addTab(publicHiddenLabel, "All Available Maps");

		tabsheet.addSelectedTabChangeListener(event -> {
			Label selectedLabel = (Label) tabsheet.getSelectedTab();
			
			if (selectedLabel == publicHiddenLabel) {
				listContainer.setFirstPage();
				listContainer.refresh(SearchMode.ALL_MAPS);
				
			} else if (selectedLabel == personalHiddenLabel){
				listContainer.setFirstPage();
				listContainer.refresh(SearchMode.AUTHORED_MAPS);
				listContainer.refresh();
				
			} else if (selectedLabel == groupHiddenLabel) {
				listContainer.setFirstPage();
				listContainer.refresh(SearchMode.SHARED_MAPS);
				
			} else {
				throw new RuntimeException("Invalid tabsheet selection");
			}
		});
		
		listContainer.setFirstPage();
		listContainer.refresh(SearchMode.ALL_MAPS);
		return tabsheet;
	}

	private void makeAddToMapButton(HorizontalLayout hostLayout) {

		Button addToGraphButton = new Button("Add to Map", e -> {
			
			Collection<Object> items = dataGrid.getSelectionModel().getSelectedRows();
			
			DesktopUI ui = (DesktopUI) UI.getCurrent();
			
			for (Object item: items) {
				
				Statement statement = (Statement) item;
				
				IdentifiedConcept subject = statement.getSubject() ;
				IdentifiedConcept object  = statement.getObject() ;
				
				// Unusual case of missing data (mostly in sample data?)
				if( subject == null || object == null ) continue ;
				
				ui.addNodeToConceptMap(subject);
				ui.addNodeToConceptMap(object);
				ui.addEdgeToConceptMap(statement);
				
				// just in case, reset the currently active highlighted node(?)
				Optional<IdentifiedConcept> selectedConceptOpt = query.getCurrentSelectedConcept();
				if (selectedConceptOpt.isPresent()) { 
					IdentifiedConcept concept = selectedConceptOpt.get();
					ui.setHighlightedNode(concept) ;
				}
			}
			// https://dev.vaadin.com/ticket/16345
			((SelectionModel.Multi) dataGrid.getSelectionModel()).deselectAll();
		});

		addToGraphButton.setEnabled(false);
		addToGraphButton.setWidth(250, Unit.PIXELS);
		addToGraphButton.setStyleName("add-to-map-button");

		// add selection listener for the grid
		dataGrid.addSelectionListener(selection -> {
			boolean isEmpty = selection.getSelected().isEmpty();

			addToGraphButton.setEnabled(isEmpty ? false : true);
		});
		VerticalLayout atgbLayout = new VerticalLayout();
		atgbLayout.addComponent(addToGraphButton);
		hostLayout.addComponent(atgbLayout);
		hostLayout.setComponentAlignment(atgbLayout, Alignment.BOTTOM_RIGHT);

	}

	/*
	 * Cell Style Names for particular ListView columns can be set here? TODO:
	 * Might be nice to have styleName settings as a Registry.Mapping variable?
	 */
	private String getStyle(CellReference cellRef) {
		return getStyle((String) cellRef.getPropertyId());
	}

	/**
	 * Gets the style of a column or cell by its property id (i.e., its name).
	 * See also {@link #getStyle(CellReference)}.
	 * @param cellPropertyId
	 * @return See
	 */
	private String getStyle(String cellPropertyId) {
		String styleName = "";
		switch (cellPropertyId) {

		// Concept-by-Text table
		case "name":
			styleName = "name-cell";
			break;

		case "type":
			styleName = "type-cell";
			break;

		case "description":
			styleName = "description-cell";
			break;

		// Relation table
		case COL_ID_SUBJECT:
		case COL_ID_OBJECT:
			styleName = "concept-cell";
			break;

		case COL_ID_RELATION:
			styleName = "relation-cell";
			break ;
			
//		case COL_ID_EVIDENCE:
//			styleName = "evidence-cell";
//			break ;
//
//		case COL_ID_DETAILS:
//			styleName = "evidence-cell";
//			break ;
			
		case "library":
		case "parents":
			styleName = "library-cell" ;
			break ;
			
		case COL_ID_PUBLICATION_DATE:
			styleName = "publication-date-cell" ;
			break ;

		case COL_ID_REFERENCE:
			styleName = "reference-cell" ;
			break ;

		// Otherwise ignore?
		default:
			styleName = null;
			break;
		}
		return styleName;
	}

	private Label formatDataTableLabel(String prefix, String middle, String suffix) {
		String html = "";

		if (!prefix.isEmpty())
			html += "<span class=\"data-table-label-regular\">" + prefix + "</span>&nbsp;";

		if (!middle.isEmpty())
			html += "<span class=\"data-table-label-highlight\">'" + middle + "'</span>";

		if (!suffix.isEmpty())
			html += "&nbsp;<span class=\"data-table-label-regular\">" + suffix + "</span>";

		return new Label(html, ContentMode.HTML);
	}

	private Label formatDataTableLabel(String prefix, String subject) {
		return formatDataTableLabel(prefix, subject, "");
	}

	private Label formatDataTableLabel(String message) {
		return formatDataTableLabel(message, "");
	}
	
	private VerticalLayout formatGenericTableComponents(String datatype) {
		
		//TODO: DEC 15 2018 - Removed custom Grid
//		dataTable = new bio.knowledge.grid.Grid(scrollListener);
		dataGrid = new Grid();
		dataGrid.setWidth("100%");
		dataGrid.setHeightMode(HeightMode.ROW);
		dataGrid.setHeightByRows(ROWS_TO_DISPLAY);
		dataGrid.setImmediate(true);

		// create a style name for the grid
		dataGrid.addStyleName("results-grid");

		// set custom style names for the cells in particular columns
		dataGrid.setCellStyleGenerator(cellRef -> getStyle(cellRef));

		// activate multi selection mode if it is relations table view
		if (datatype.equals(ViewName.RELATIONS_VIEW))
			dataGrid.setSelectionMode(SelectionMode.MULTI);

		// the panel will give it scrollbars.
		//Panel dataPanel = new Panel(getMessage("global_menu_list", getMessage(datatype + "_plural")));
		Panel dataPanel = new Panel("Data Table");
		dataPanel.setContent(dataGrid);
		dataPanel.setSizeFull();

		VerticalLayout dataTableLayout = new VerticalLayout();
		dataTableLayout.addStyleName("table-layout");
		dataTableLayout.setSizeFull();
		dataTableLayout.setMargin(true);
		dataTableLayout.setSpacing(true);

		HorizontalLayout filterHeader = new HorizontalLayout();

		filterHeader.setWidth("100%");
		filterHeader.setSpacing(true);

		// add the relation triple in evidence view
		Label dataTableLabel = null;

		// currentQueryConcept might be empty
		// and/or ignored for some views
		Optional<IdentifiedConcept> currentQueryConcept = query.getCurrentQueryConcept(); 
		
		if (viewName.equals(ViewName.EVIDENCE_VIEW)) {

			switch (query.getRelationSearchMode()) {
				case PMID:
						dataTableLabel = 
							formatDataTableLabel(
									"Sentences Extracted from PMID", 
									query.getCurrentPmid().get() 
							) ;
					break ;
					
				case RELATIONS:
					Optional<Statement> statementOpt = query.getCurrentStatement();
					Optional<Evidence> evidenceOpt = query.getCurrentEvidence() ;
					if ( evidenceOpt.isPresent() ) {
						Evidence evidence = evidenceOpt.get() ;
						Statement statement = evidence.getStatement() ;
						if( statement != null ) {
							String subject      = statement.getSubject().getName();
							String object       = statement.getObject().getName();
							String relationship = statement.getRelation().getName();	
							dataTableLabel = formatDataTableLabel( subject, relationship, object ) ;
						} else if (statementOpt.isPresent()) {
							Statement s = statementOpt.get();
							dataTableLabel = formatDataTableLabel(
									s.getSubject().getName(),
									s.getRelation().getName(),
									s.getObject().getName()
							);
						} else
							dataTableLabel = formatDataTableLabel( "No Statement is Currently Selected?" );
					} else
						dataTableLabel = formatDataTableLabel("No Evidence is Currently Selected?");
					break;

			case WIKIDATA:
				break; // nothing doing here ... yet?

			default:
				throw new DataServiceException("ListView.formatGenericTableComponents(): Invalid RelationSearchMode?");
			}

		} else if (viewName.equals(ViewName.CONCEPTS_VIEW)) {

			String searchString = query.getCurrentQueryText();
			dataTableLabel = formatDataTableLabel("Concepts Matching ", searchString);

		} else if (viewName.equals(ViewName.LIBRARY_VIEW)) {
			
			String target = "" ;
			String title = "Viewing Available Concept Maps " ;
			switch( query.getLibraryMode() ) {
				
				case BY_CONCEPT:
			        if( currentQueryConcept.isPresent() ) {
						IdentifiedConcept concept = currentQueryConcept.get() ;
			        	target = concept.getName()+" ["+concept.getType().getDescription()+"]" ;
			        	title += "for Concept" ;
			        } else
			        	throw new RuntimeException("DesktopUI.displayLibraryByConceptSemanticType() error: CST not set for LibraryByConceptSearch?") ;
					break ;
					
				case BY_LIBRARY:
					target = query.getCurrentQueryText() ;
					if(!target.trim().isEmpty())
						title += "with Concept or Map Names Matching";
					break ;
					
				case BY_PARENTS:
					Optional<Library> libraryOpt = query.getCurrentLibrary();
					if(libraryOpt.isPresent()) {
						Library library = libraryOpt.get() ;
						target = library.getName() ;
						title += "of";
					}
					break ;
		
				case HIDDEN:
				case NONE:
					throw new RuntimeException("ConceptMapArchiveService.countEntries() error: invalid query state?") ;
			}

			dataTableLabel = formatDataTableLabel(title, target);
		} else if (viewName.equals(ViewName.ANNOTATIONS_VIEW)) {
			
			//String searchString = query.getCurrentQueryText();
			dataTableLabel = formatDataTableLabel("Add More Nodes");
			
		} else if (viewName.equals(ViewName.RELATIONS_VIEW)) {

			switch (query.getRelationSearchMode()) {
			
				case PMID:
					dataTableLabel = 
						formatDataTableLabel("Relations Extracted from PMID ", query.getCurrentPmid().get());
				break;

				case RELATIONS:
					if (currentQueryConcept.isPresent()) {
						IdentifiedConcept concept = currentQueryConcept.get();
						dataTableLabel = formatDataTableLabel("Relations for Concept ",
								concept.getName() + " (as " + concept.getType().getDescription() + ")");
					} // else
						// dataTableLabel = formatDataTableLabel( "No Relations are
						// (Yet) Available?" );
					break;
				case WIKIDATA:
					// For WikiData retrieval, it is the currently selected concept
					// that is of interest...
					Optional<IdentifiedConcept> currentSelectedConcept = query.getCurrentSelectedConcept();
					if (currentSelectedConcept.isPresent()) {
						IdentifiedConcept concept = currentSelectedConcept.get();
	
						dataTableLabel = formatDataTableLabel("Data Properties for Concept", concept.getName());
					} else
						dataTableLabel = formatDataTableLabel("No Data are Available?");
	
					break;
	
				default:
					throw new DataServiceException("ListView.formatGenericTableComponents(): Invalid RelationSearchMode?");
				}
		}

		if (dataTableLabel != null) {
			dataTableLabel.addStyleName("predication-label");
			dataTableLayout.addComponent(dataTableLabel);
			dataTableLayout.setComponentAlignment(dataTableLabel, Alignment.MIDDLE_CENTER);
		}

		HorizontalLayout filterMenuBar = new HorizontalLayout();
		filterMenuBar.setSpacing(true);
		filterMenuBar.setMargin(false);
		
		Label filterPrefixLabel = new Label("Filter By");
		filterPrefixLabel.setStyleName("relation-filter-prefix");
		filterMenuBar.addComponent(filterPrefixLabel);
		
		setSemGroupFilter(filterMenuBar);		
		setPredicateFilter(filterMenuBar);		
		setUpTextFilter(filterMenuBar);
		
		// button that can add row(s) of the grid to the graph
		if (viewName.equals(ViewName.RELATIONS_VIEW)) {
			makeAddToMapButton(filterMenuBar);
		} else if (viewName.equals(ViewName.CONCEPTS_VIEW)) {
			
		}
		
		filterHeader.addComponent(filterMenuBar);
		filterHeader.setComponentAlignment(filterMenuBar, Alignment.MIDDLE_CENTER);

		if (viewName.equals(ViewName.RELATIONS_VIEW) || viewName.equals(ViewName.CONCEPTS_VIEW)
				|| viewName.equals(ViewName.LIBRARY_VIEW) || viewName.equals(ViewName.EVIDENCE_VIEW)
				|| viewName.equals(ViewName.ANNOTATIONS_VIEW)) {
			dataTableLayout.addComponent(filterHeader);
		}

		if (viewName.equals(ViewName.LIBRARY_VIEW)) {
			if (query.getLibraryMode().equals(LibrarySearchMode.BY_LIBRARY)) {
				AuthenticationManager auth = ((DesktopUI) UI.getCurrent()).getAuthenticationManager();
				if (auth.isUserAuthenticated()) {
					dataTableLayout.addComponent(makeTabSheetController());
					conceptMapArchiveService.setSearchMode(SearchMode.AUTHORED_MAPS);
				} else {
					conceptMapArchiveService.setSearchMode(SearchMode.ALL_MAPS);
				}
			} else if (query.getLibraryMode().equals(LibrarySearchMode.BY_PARENTS)) {
				this.listContainer.setFirstPage();
			}
		}

		dataTableLayout.addComponent(dataPanel);

		return dataTableLayout;
	}
	 
	private void updateCurrentTable() {
		gotoPageIndex(0);
	}

	// Popup Window for "Other..." Semantic Group selection
	private void otherSemanticGroupSelectionWindow() {
		
		Window conceptSemanticWindow = new Window("Others...");
		
		conceptSemanticWindow.addStyleName("semanticfilter-window");
		conceptSemanticWindow.setModal(true);
		conceptSemanticWindow.center();

		SemanticFilterView filterView = new SemanticFilterView(conceptSemanticWindow, viewName, query);

		conceptSemanticWindow.setContent(filterView);
		conceptSemanticWindow.setWidth("23%");
		conceptSemanticWindow.setHeight(450, Unit.PIXELS);

		conceptSemanticWindow.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = -4799904881921178600L;

			@Override
			public void windowClose(CloseEvent e) {

				// save the values selected so that it can be
				// displayed next time when the window is opened
				if (!viewName.equals(ViewName.CONCEPTS_VIEW)) {
					Object treeValue = filterView.getTree().getValue();
					query.setOtherSemGroupFilterValue(treeValue);
				}

				if (filterView.shouldRefresh()) {
					updateCurrentTable();
				}
			}
		});

		UI.getCurrent().addWindow(conceptSemanticWindow);
	}
	
	private void setSemGroupFilter(HorizontalLayout filterMenuBar) {

		if (!(
				viewName.equals(ViewName.RELATIONS_VIEW) ||
				viewName.equals(ViewName.CONCEPTS_VIEW) || 
				viewName.equals(ViewName.ANNOTATIONS_VIEW))
			) {
			return;
		}

		MenuBar semanticFilterMenu = new MenuBar();
		semanticFilterMenu.addStyleName("semanticfilter-menu");

		MenuItem types = semanticFilterMenu.addItem("Any", null, null);

		MenuItem any = types.addItem("Any", null, null);
		MenuItem disorders = types.addItem("Disorders", null, null);
		MenuItem genes = types.addItem("Genes", null, null);
		MenuItem drugs = types.addItem("Drugs", null, null);
		MenuItem others = types.addItem("Others...", null, null);

		// update the filter text (only in relations view)
		String type = query.getSemGroupFilterType();

		if (viewName.equals(ViewName.RELATIONS_VIEW)) {
			if (nullOrEmpty(type)) {
				types.setText("Any");
			} else {
				types.setText(type);
			}
		}

		MenuBar.Command selectCommand = new MenuBar.Command() {
			private static final long serialVersionUID = 6098702627001408240L;

			public void menuSelected(MenuItem selectedItem) {
				types.setText(selectedItem.getText());

				// update the filter text so that it remembers its value when
				// comes back
				if (viewName.equals(ViewName.RELATIONS_VIEW)) {
					query.setSemGroupFilterType(selectedItem.getText());
				}

				Set<ConceptType> typeSet = new HashSet<ConceptType>();
				ConceptType type = null;

				if (selectedItem == disorders) {
					type = ConceptType.DISO;
					
				} else if (selectedItem == genes) {
					type = ConceptType.GENE;
					
				} else if (selectedItem == drugs) {
					type = ConceptType.CHEM;
					
				} else if (selectedItem == any) {
					type = null;
					
				} else if (selectedItem == others) {
					otherSemanticGroupSelectionWindow() ;
				}

				if (type != null) {
					typeSet.add(type);
				}

				// prevent the table immediately refresh upon clicking 'others'
				if (selectedItem != others) {
					setQueryTypes(typeSet);
					updateCurrentTable();
				}
			}

			private void setQueryTypes(Set<ConceptType> typeSet) {
				if (viewName.equals(ViewName.CONCEPTS_VIEW)) {
					query.setInitialConceptTypes(typeSet);
				} else if (viewName.equals(ViewName.RELATIONS_VIEW)) {
					query.setConceptTypes(typeSet);
				}
			}
		};

		for (MenuItem item : types.getChildren()) {
			item.setCommand(selectCommand);
		}

		VerticalLayout semGrpLayout = new VerticalLayout();
		Label semGrpLabel = new Label("Semantic Group");
		semGrpLayout.addComponents(semGrpLabel,semanticFilterMenu);
		filterMenuBar.addComponent(semGrpLayout);
		filterMenuBar.setComponentAlignment(semGrpLayout, Alignment.MIDDLE_LEFT);
	}

	private void displayPredicateFilter(ComboBox selectedRelations) {
			
		Window predicateFilterWindow = new Window("Predicate Filter");
		
		predicateFilterWindow.addStyleName("predicatefilter-window");
		predicateFilterWindow.setModal(true);
		predicateFilterWindow.center();
		
		PredicateFilterView predicateFilterView = 
				new PredicateFilterView(predicateService, predicateFilterWindow);

		predicateFilterWindow.setContent(predicateFilterView);
		predicateFilterWindow.setWidth("23%");
		predicateFilterWindow.setHeight(450, Unit.PIXELS);

		predicateFilterWindow.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = -4799904881921178600L;

			@Override
			public void windowClose(CloseEvent e) {

				if (viewName.equals(ViewName.RELATIONS_VIEW)) {
					Set<Predicate> selections = 
							predicateFilterView.getSelectedPredicates();
					query.setPredicateFilterValue(selections);
					selectedRelations.addItems(selections);
					if (predicateFilterView.shouldRefresh()) {
						updateCurrentTable();
					}
				}
			}
		});

		UI.getCurrent().addWindow(predicateFilterWindow);
	}
	private void setPredicateFilter(HorizontalLayout filterMenuBar) {
		
		if (! viewName.equals(ViewName.RELATIONS_VIEW) ) {
			return;
		}
				
		Button showRelationFilter = new Button("Relation Filter");
		showRelationFilter.setStyleName("relation-filter");
		
		ComboBox selectedRelations = new ComboBox("Selected Relations");
		
		showRelationFilter.addClickListener(e -> {
			displayPredicateFilter(selectedRelations);
		});
		
		filterMenuBar.addComponent(showRelationFilter);
		filterMenuBar.setComponentAlignment(showRelationFilter, Alignment.BOTTOM_CENTER);
	
		filterMenuBar.addComponent(selectedRelations);
		filterMenuBar.setComponentAlignment(selectedRelations, Alignment.BOTTOM_CENTER);
		
		/*
		ComboBox predicateFilterSelector = new ComboBox("Relation",predicates);
		predicateFilterSelector.setNullSelectionItemId(ANY_RELATION);
		
		// update the filter text 
		Optional<Predicate> optionalPredicateFilter = query.getPredicateFilterValue();
		
		Predicate predicateFilter = null;
		
		if (optionalPredicateFilter.isPresent()) {
			predicateFilter = optionalPredicateFilter.get();
			predicateFilterSelector.select(predicateFilter);
		} else {
			predicateFilterSelector.select(predicateFilterSelector.getNullSelectionItemId());
		}

		predicateFilterSelector.addValueChangeListener(event -> {
			Object value = event.getProperty().getValue();
			Predicate selected = (Predicate)value;
			if(selected!=null) {
			    _logger.debug("Selected Relation: "+selected.toString());
			    query.setPredicateFilterValue(selected);
			} else {
			    _logger.debug("No Predicate filter selected");
				query.resetPredicateFilterValue();
			}
			updateCurrentTable();
		});
		
		filterMenuBar.addComponent(predicateFilterSelector);
		filterMenuBar.setComponentAlignment(predicateFilterSelector, Alignment.MIDDLE_CENTER);
		*/
		
	}
	
	private void setUpTextFilter(HorizontalLayout filterMenuBar) {
		
		if (!viewName.equals(ViewName.RELATIONS_VIEW) && !viewName.equals(ViewName.CONCEPTS_VIEW)
				&& !viewName.equals(ViewName.LIBRARY_VIEW) && !viewName.equals(ViewName.EVIDENCE_VIEW)
				&& !viewName.equals(ViewName.ANNOTATIONS_VIEW)) {
			return;
		}

		simpleTextFilter.setValue("");
		if (viewName.equals(ViewName.EVIDENCE_VIEW)) {
			simpleTextFilter.setInputPrompt("Filter Sentences");
		} else if (viewName.equals(ViewName.RELATIONS_VIEW)) {
			simpleTextFilter.setInputPrompt("Filter Statement Text");
		} else if (viewName.equals(ViewName.CONCEPTS_VIEW)) {
			simpleTextFilter.setInputPrompt("Filter Concepts");
		} else if (viewName.equals(ViewName.LIBRARY_VIEW)) {
			simpleTextFilter.setInputPrompt("Filter Map Names");
		} else if (viewName.equals(ViewName.ANNOTATIONS_VIEW)) {
			simpleTextFilter.setInputPrompt("Search");
		} else {
			simpleTextFilter.setInputPrompt("Search");
		}

		simpleTextFilter.setWidth("15em");
		simpleTextFilter.setIcon(FontAwesome.SEARCH);
		simpleTextFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		Collection<?> listeners = simpleTextFilter.getListeners(AbstractField.ValueChangeEvent.class);

		if (listeners.isEmpty()) {
			simpleTextFilter.addValueChangeListener(event -> {
				String filterText = (String) event.getProperty().getValue();
				filterText = filterText.trim();
				query.setSimpleTextFilter(filterText);
				listContainer.setSimpleTextFilter(filterText);
				gotoPageIndex(0); // refreshes the view
			});
		}

		// Need a layout to keep the icon inside
		HorizontalLayout textFilterBar = new HorizontalLayout(); 
		textFilterBar.addComponent(simpleTextFilter);

		VerticalLayout textFilterLayout = new VerticalLayout();
		Label textLabel = new Label("Text");
		textFilterLayout.addComponents(textLabel,textFilterBar);
		filterMenuBar.addComponent(textFilterLayout);
		filterMenuBar.setComponentAlignment(textFilterLayout, Alignment.MIDDLE_RIGHT);
	}

	private String getCellDescription(CellReference cell) {

		Object value = cell.getValue();
		String description = "";

		if (value != null) {
			String name = value.toString();
			description = name;

			// display the definition of the relation if applicable
			String propertyId = (String) cell.getPropertyId();
			String definition = "Unknown";

			// if (propertyId.equals(COL_ID_RELATION)) {
			if (propertyId.equals(COL_ID_RELATION)) {
				if (value instanceof Predicate) {
					definition = ((Predicate) value).getDescription();
					description = "<span style=\"font-weight: bold;\">" + name + ": " + "</span>" + definition;
				}
			}

			int tfstart = name.lastIndexOf(SEMGROUP_FIELD_START);
			if (tfstart != -1) {
				int tfend = name.lastIndexOf(SEMGROUP_FIELD_END);
				if (tfend != -1) {
					String semtypeCode = name.substring(tfstart + 1, tfend);
					description = name.substring(0, tfstart);
					try {
						SemanticType semtype = SemanticType.lookUpByCode(semtypeCode);
						description += " " + SEMGROUP_FIELD_START + semtype.getDescription()
								+ SEMGROUP_FIELD_END;
					} catch (DomainModelException dme) {
						// code not recognized...fail silently
					}
				}
			}

		}
		return description;
	}

	private void formatDefinedDataTypeTable(Registry.Mapping mapping) {

		List<String> columns = new ArrayList<String>();
		Map<String, RendererClickListener> selectionHandlers = mapping.getSelectionHandlers();

		for (String column : mapping.getColumns()) {
			String[] colspec = column.split("\\|");
			if (colspec.length == 1) {
				columns.add(column);
				dataGrid.addColumn(column);

				// Different views will have the dataTable be different sizes,
				// requiring slightly different maximum widths for each column.
				// I've arrived at these numbers through experimentation. They
				// should probably be made into constants.
				if (viewName.equals(ViewName.CONCEPTS_VIEW)) {
					dataGrid.getColumn(column).setMaximumWidth(200);
				} else {
					dataGrid.getColumn(column).setMaximumWidth(150);
				}

			} else {

				// Add a button with callback to this column
				String columnName = colspec[0].trim();
				columns.add(columnName);
				RendererClickListener handler = selectionHandlers.get(columnName);
				dataGrid.addColumn(columnName);
				
				// Different views will have the dataTable be different sizes,
				// requiring slightly different maximum widths for each column.
				// I've arrived at these numbers through experimentation. They
				// should probably be made into constants.
				switch(viewName) {
					case ViewName.CONCEPTS_VIEW: // name and description field
						dataGrid.getColumn(columnName).setMaximumWidth(200);
						break;
//					case ViewName.EVIDENCE_VIEW: // supportingText field
//						dataGrid.getColumn(columnName).setMaximumWidth(400);
//						break;
//					default:
//						dataGrid.getColumn(columnName).setMaximumWidth(150);
				}
				
				if (selectionHandlers.containsKey(columnName))
					ViewUtil.makeButton(dataGrid, columnName, handler::click, viewName);
			}
		}

		ViewUtil.formatGrid(this, dataGrid, columns.toArray(new String[] {}));

		detailFields = mapping.getDetailFields();

		dataGrid.addSortListener(e -> {
			if (e.isUserOriginated()) {
				listContainer.sortDataTable();
			}
		});

		// tooltip for cells
		dataGrid.setCellDescriptionGenerator(cell -> getCellDescription(cell));

		// the container is of the BeanItemContainer type
		listContainer.setContainer(mapping.getContainer());
		listContainer.setPager(mapping.getPager());
		listContainer.setEntryCounter(mapping.getEntryCounter());
		listContainer.setHitCounter(mapping.getHitCounter());
		listContainer.setPageCounter(mapping.getPageCounter());
	}

	private void formatDefaultTable() {

		// As default use Identification interface name and description
		dataGrid.addColumn("name", String.class);
		dataGrid.addColumn("description", String.class);

		ViewUtil.formatGrid(this, dataGrid, new String[] { "name", "description" });

		detailFields = new ArrayList<String>();
		detailFields.add("name");
		detailFields.add("description");

		// Stub implementation for unregistered data types,
		// uses Identifier listing for now,
		// TODO: Pageable page_spec = new Pageable() ;
		// Page<Identifier> page = service.getIdentifiers(page_spec) ;
		listContainer.setContainer(
				new BeanItemContainer<IdentifiedEntity>(IdentifiedEntity.class));
	}

	protected void renderDisplay() {

		_logger.trace("Entering ListView.renderDisplay(" + viewName.toString() + ")");

		removeAllComponents();

		// Clear the simpleTextFilter so that it isn't
		// sticky across various list views!
		listContainer.setSimpleTextFilter("");

		// TODO: refactor the execution flow. batch-handle
		// the property value declarations, the event handler declarations
		if (nullOrEmpty(viewName)) {

			String message = new String("Not sure what type of data you want to list here...");
			Label label = new Label(message);
			addComponent(label);

		} else {
			
			String viewNameWithoutUrlParameters = viewName.split("/")[0];
			
			VerticalLayout dataTableLayout = formatGenericTableComponents(viewNameWithoutUrlParameters);
			
			// Format Table View
			Registry.Mapping mapping = registry.getMapping(viewNameWithoutUrlParameters);
			if (mapping != null)
				formatDefinedDataTypeTable(mapping);
			else
				formatDefaultTable();

			loadDataTable(dataTableLayout);
			
			Column col = dataGrid.getColumn(COL_ID_DETAILS);
			if (col != null) {				
				col.setRenderer(new ButtonRenderer(e -> {
					showDetails(e);
				}));
			}
		}
	}

	/**
	 * Used by the 'Details' column in the concept search result grid.
	 * Click it to show the concept details in a new Window.
	 * @param e
	 */
	private void showDetails(RendererClickEvent e) {
		Window conceptDetailsWindow = new Window("Concept Details");
		VerticalLayout detailsContent = new VerticalLayout();
		detailsContent.setSpacing(true);
		conceptDetailsWindow.setContent(detailsContent);
		
		conceptDetailsWindow.setStyleName("concept-details-window");
		conceptDetailsWindow.center();
		conceptDetailsWindow.setWidthUndefined();
		conceptDetailsWindow.setHeightUndefined();
		conceptDetailsWindow.setResizable(false);
		
		IdentifiedConcept concept = (IdentifiedConcept) e.getItemId();
		CompletableFuture<AnnotatedConcept> future = getConceptWithDetails(concept.getCliqueId());

		try {
			concept = future.get(kbService.weightedTimeout(), KnowledgeBeaconService.BEACON_TIMEOUT_UNIT);
		} catch (InterruptedException | ExecutionException | TimeoutException | IndexOutOfBoundsException exception) {
			// should do something useful here
			concept = new AnnotatedConceptImpl(concept.getCliqueId(), concept.getName(), concept.getType(), concept.getTaxon());
			_logger.error("Error retrieving concept details from the search result table");
		}
		
		VerticalLayout details = detailshandler.getDetails(concept);
		detailsContent.addComponent(details);
		detailsContent.addComponent(new Label("&nbsp;", ContentMode.HTML));
		
		UI.getCurrent().addWindow(conceptDetailsWindow);
	}

	private void onShowDetails(RendererClickEvent event) {

		detailsPane.removeAllComponents();

		// will return the type of the BeanItemContainer's
		// class argument, as the type of the items being stored.
		IdentifiedEntity entity = (IdentifiedEntity) event.getItemId();
		Indexed gpc = gpcontainer.getWrappedContainer();
		Item item = gpc.getItem(entity);

		/*
		 * A field name should correspond to the exact string name of a
		 * 'container' Bean field.
		 * 
		 * If a pipe "|" character is appended, followed by an asterix "*", then
		 * the field value is taken to cliekable field.
		 * 
		 * If a pipe "|" character is appended with additional characters OTHER
		 * THAN an asterix "*", those characters are assumed to be a URL
		 * template.
		 * 
		 * The URL template can contain special 'macro' values for string
		 * substitution.
		 * 
		 * At the moment, the following two macros are supported: $L - insert
		 * the current Locale two character language code (e.g. 'en', 'de',
		 * 'es', 'fr', 'pt') at this point in the URL string
		 * 
		 * $V - insert the field value string into the URL at this point in the
		 * URL string
		 * 
		 * e.g. "deltaId|http://www.delta-intkey.com/wood/$L/www/$V.htm"
		 * 
		 * specifies a language $L constrained URL to the Delta commercial wood
		 * anatomy database, in which $V is the wood HTML page name for the
		 * given taxon.
		 */
		for (String fieldName : detailFields) {

			String[] fieldParts = fieldName.split("\\|");
			fieldName = fieldParts[0];
			
			Property<?> itemProperty = item.getItemProperty(fieldName);

			Label label = new Label();
			label.setStyleName("emphasized");
			detailsPane.addComponent(label);
			
			Object fieldValue = null;

			if (itemProperty != null) {
				
				fieldValue = itemProperty.getValue();

				label.setCaption(getMessage(fieldName) + ": ");
				
				if (fieldValue != null) {
					if (fieldParts.length > 1) {
						String url = fieldParts[1];
						if (url.equals("*")) {
							url = fieldValue.toString();
						} else if( 
									url.toLowerCase().startsWith("http://") || 
									url.toLowerCase().startsWith("https://")
								) {
							// dissect url template
							Locale locale = this.getCurrentUI().getLocale();
							String language = locale.getLanguage().substring(0, 2);
							url = url.replace("$L", language);
							url = url.replace("$V", fieldValue.toString());
							Link link = new Link(fieldValue.toString(), new ExternalResource(url));
							link.setTargetName("_blank");
							detailsPane.addComponent(link);
						} else {
							// maybe just another field name to render as a tool tip?
							itemProperty = item.getItemProperty(url);
							String toolTipString = itemProperty.getValue().toString();
							Label theField = new Label(fieldValue.toString());
							theField.setDescription(toolTipString);
							detailsPane.addComponent(theField);
						}

					} else
						detailsPane.addComponent(new Label(fieldValue.toString()));
				} else
					detailsPane.addComponent(new Label("Unknown"));
			} else {
				label.setCaption(fieldName + ": field missing?");
			}
		}

		// Once the key-map field generation is in -
		// attach this functionality to the cursor
		// that iterates and populates those columns.
		Button collapseButton = new Button(getMessage("button_hide_details"));
		collapseButton.addClickListener(e -> {
			detailsPane.removeAllComponents();
			((HorizontalSplitPanel) listViewContents).setSplitPosition(100, Unit.PERCENTAGE);
		});

		// collapse button component
		detailsPane.addComponent(collapseButton);

		// expand the window for
		((HorizontalSplitPanel) listViewContents).setSplitPosition(60, Unit.PERCENTAGE);
	}

	static enum ConceptRole {
		SUBJECT, OBJECT;
	}

	private void selectionContext(DesktopUI ui, PopupWindow conceptDetailsWindow, IdentifiedConcept selectedConcept) {
		ui.queryUpdate(selectedConcept, RelationSearchMode.RELATIONS);
		conceptDetailsWindow.close();
		ui.gotoStatementsTable();
	}
	
	@Autowired
	ConceptDetailsHandler detailshandler;
	
	@Autowired
	KnowledgeBeaconService kbService;
	
	private HorizontalLayout buttonsLayout;
	
	// Handler for Concept details in various data tables
	private void onConceptDetailsSelection(RendererClickEvent event, ConceptRole role) {
		Statement statement = (Statement) event.getItemId();
		IdentifiedConcept subject = statement.getSubject();
		Predicate predicate = statement.getRelation();
		IdentifiedConcept object = statement.getObject();

		RelationSearchMode searchMode = query.getRelationSearchMode();
		if (searchMode.equals(RelationSearchMode.WIKIDATA) && role.equals(ConceptRole.OBJECT)) {

			// This is a WikiData item property value...
			detailshandler.displayDataPage(predicate.getId(), object.getName());

		} else {

			DesktopUI ui = (DesktopUI) UI.getCurrent();

			PopupWindow conceptDetailsWindow = new PopupWindow();
			conceptDetailsWindow.addStyleName("concept-details-window");
			conceptDetailsWindow.center();
			conceptDetailsWindow.setSizeUndefined();

			//String predicateLabel;
			
			String cliqueId;
			if (role.equals(ConceptRole.SUBJECT)) {				
//				cliqueId = subject.getId();
				cliqueId = subject.getCliqueId();
			} else if (role.equals(ConceptRole.OBJECT)) {
//				cliqueId = object.getId();
				cliqueId = object.getCliqueId();
			} else
				throw new RuntimeException("Unsupported Relationship Concept Role?");

			CompletableFuture<AnnotatedConcept> future = getConceptWithDetails(cliqueId);
			
			IdentifiedConcept selectedConcept;
			
			try {
				selectedConcept = 
						future.get(
								kbService.weightedTimeout(), 
								KnowledgeBeaconService.BEACON_TIMEOUT_UNIT
						);
			} catch (InterruptedException | ExecutionException | TimeoutException | IndexOutOfBoundsException e1) {
				selectedConcept = role.equals(ConceptRole.SUBJECT) ? subject : object;
			}

			String conceptName;

			if (selectedConcept != null) {
				conceptName = selectedConcept.getName();
			} else {
				conceptName = "Unknown concept";
			}

			//predicateLabel = predicate.getName();

			Button showRelationsBtn = new Button("Show Relations");
			final IdentifiedConcept finallySelectedConcept = selectedConcept;
			showRelationsBtn.addClickListener(e -> selectionContext(ui, conceptDetailsWindow, finallySelectedConcept));

			// RMB: 9 September 2016 - deprecating relation table display of
			// WikiData
			/*
			 * Button showData = new Button("Find Data") ;
			 * showData.addClickListener( e -> dataSelectionContext( ui,
			 * conceptDetailsWindow, selectedConcept ) );
			 */

			Button addToMap = new Button("Add to Map");
			addToMap.addClickListener(e -> {

				ui.addNodeToConceptMap(subject);
				ui.addNodeToConceptMap(object);
				ui.addEdgeToConceptMap(statement);

				conceptDetailsWindow.close();
			});

			Button closeButton = new Button("Close");
			closeButton.addClickListener(e -> {
				conceptDetailsWindow.close();
			});

			HorizontalLayout operationsLayout = new HorizontalLayout();
			operationsLayout.addComponent(showRelationsBtn);

			// RMB: 9 September 2016 - deprecating relation table display of
			// WikiData
			// operationsLayout.addComponent(showData);

			operationsLayout.addComponent(addToMap);
			operationsLayout.setSpacing(true);

			buttonsLayout = new HorizontalLayout();
			buttonsLayout.addComponents(operationsLayout, closeButton);

			buttonsLayout.setSpacing(true);
			buttonsLayout.setWidth("100%");

			buttonsLayout.setComponentAlignment(operationsLayout, Alignment.MIDDLE_LEFT);
			buttonsLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_RIGHT);

			VerticalLayout wd_details = detailshandler.getDetails(selectedConcept);

			VerticalLayout windowContent = new VerticalLayout();
			windowContent.setSpacing(true);
			windowContent.addComponent(wd_details);
			windowContent.addComponent(buttonsLayout);

			conceptDetailsWindow.setCaption(conceptName);
			conceptDetailsWindow.setContent(windowContent);

			ui.addWindow(conceptDetailsWindow);
		}
	}

	private CompletableFuture<AnnotatedConcept> getConceptWithDetails(String cliqueId) {
		List<String> beacons = query.getCustomBeacons();
		String sessionId = query.getUserSessionId();
		CompletableFuture<AnnotatedConcept> future = kbService.getConceptWithDetails(cliqueId,beacons,sessionId);
		return future;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.starinformatics.ui.view.ListView#registerViews()
	 */
	protected void registerViews() {

		// view used for searching while creating a user annotation
		registry.setMapping(
				ViewName.ANNOTATIONS_VIEW, 
				new BeanItemContainer<IdentifiedConcept>(IdentifiedConcept.class),
				// TODO: use the cache to get the results
				conceptService, 
				new String[] { "beaconSource", "name|*", "type" }, 
				null, 
				null);

		registry.addSelectionHandler(
			ViewName.ANNOTATIONS_VIEW, 
			"name", 
			event -> {
				IdentifiedConcept concept = (IdentifiedConcept) event.getItemId();
				DesktopUI ui = (DesktopUI) UI.getCurrent();
	
				ui.addNodeToConceptMap(concept);
				ui.closeConceptSearchWindow();
				ui.getPredicatePopupWindow().addConceptToComboBoxes(concept);
				// These get reset for some reason when we ask for them? Passing out of the popup instance using KBQuery
				ui.getPredicatePopupWindow()
					.conceptMapUserAnnotation(concept, query.tempCoordX(), query.tempCoordY());
				ui.gotoStatementsTable();
		});

		// misc views
		registry.setMapping("contactform", new BeanItemContainer<ContactForm>(ContactForm.class

		// don't bother to initialize the data source for the Bean...
		// A page-specific initialization will happen later
		// , getContactMessages()
		), contactFormService, new String[] { "name", "email", COL_ID_SUBJECT },
				new String[] { "name", "email", "affiliation", "country", COL_ID_SUBJECT, "message" });

		registry.setMapping("ontology", new BeanItemContainer<OntologyTerm>(OntologyTerm.class), ontologyTermService,
				new String[] { "name", "description" }, null // no details view
																// needed
		);

		/* Attempt real data integration here! */

		/* Newer idea: query the Concept nodes directly by name... */

		// concepts view
		registry.setMapping(
				ViewName.CONCEPTS_VIEW,
				new BeanItemContainer<IdentifiedConcept>(IdentifiedConcept.class), 
				conceptService,
				new String[] { 
						"cliqueId", 
						"name|*", 
						"type",
						COL_ID_DETAILS + "|*"
						/*, RMB: adding 'usage' here might be interesting? */
						/*, OBSOLETE: i.e. only in details now? "description|*", "synonyms|*" */
				},
				null, 
				null);

		registry.addSelectionHandler(ViewName.CONCEPTS_VIEW, "cliqueId",e->{/*NOP*/});

		registry.addSelectionHandler(ViewName.CONCEPTS_VIEW, "name", event -> {
			IdentifiedConcept concept = (IdentifiedConcept) event.getItemId();
			DesktopUI ui = (DesktopUI) UI.getCurrent();
			ui.processConceptSearch(concept);
			ui.closeConceptSearchWindow();
			ui.gotoStatementsTable();
		});
		
		registry.addSelectionHandler(ViewName.CONCEPTS_VIEW, "synonyms",e->{/*NOP*/});
		registry.addSelectionHandler(ViewName.CONCEPTS_VIEW, "description",e->{/*NOP*/});

		registry.addSelectionHandler(ViewName.CONCEPTS_VIEW, "library", event -> {
			IdentifiedConcept concept = (IdentifiedConcept) event.getItemId();
			
			// Ignore ConceptSemanticType entries with empty libraries
			Library library = concept.getLibrary();
			if (library.isEmpty())
				return;

			DesktopUI ui = (DesktopUI) UI.getCurrent();

			// Search for maps by Concept
			query.setLibraryMode(LibrarySearchMode.BY_CONCEPT);

			ui.queryUpdate(concept, RelationSearchMode.RELATIONS);

			ui.displayConceptMapList();
		});

		registry.setMapping(ViewName.LIBRARY_VIEW, new BeanItemContainer<ConceptMapArchive>(ConceptMapArchive.class),
				conceptMapArchiveService, new String[] { "name|*", "author|*", "dateLastModified|*" }, null, null);

		registry.addSelectionHandler(ViewName.LIBRARY_VIEW, "name", event -> {
			ConceptMapArchive map = (ConceptMapArchive) event.getItemId();
			((DesktopUI) UI.getCurrent()).switchLibraryToDetails(map);
		});

		registry.addSelectionHandler(ViewName.LIBRARY_VIEW, "author", event -> {
			ConceptMapArchive map = (ConceptMapArchive) event.getItemId();
			String userId = map.getAuthorsAccountId();
			if (userId != null) {
				((DesktopUI) UI.getCurrent()).switchLibraryToUserDetails(userId);
			}
		});

		registry.setMapping(ViewName.RELATIONS_VIEW, new BeanItemContainer<Statement>(Statement.class),
				statementService, new String[] { 
						"beaconSource", 
						"subjectClique",
						"subjectId",
						"subject|*", 
						COL_ID_RELATION, 
						"object|*", 
						"objectId",
						"objectClique",
						"evidence|*" 
				}, null);

		
		registry.addSelectionHandler(ViewName.RELATIONS_VIEW, COL_ID_SUBJECT,
				e -> onConceptDetailsSelection(e, ConceptRole.SUBJECT));

		registry.addSelectionHandler(ViewName.RELATIONS_VIEW, COL_ID_OBJECT,
				e -> onConceptDetailsSelection(e, ConceptRole.OBJECT));

		registry.addSelectionHandler(ViewName.RELATIONS_VIEW, COL_ID_EVIDENCE, event -> {
			DesktopUI ui = (DesktopUI) UI.getCurrent();

			Statement selectedStatement = (Statement) event.getItemId();
			query.setCurrentStatement(selectedStatement);

			_logger.trace("Display Evidence for " + selectedStatement.getName());

			ui.displayEvidence(selectedStatement.getId());
		});

		registry.setMapping(ViewName.EVIDENCE_VIEW, 
				new BeanItemContainer<Annotation>(Annotation.class),
				annotationService,
				new String[] { /* "reference|*", */"beaconSource", "publicationDate", "supportingText|*" /* ,"evidenceCode" */ }, 
				null, 
				null);

		registry.addSelectionHandler(
				ViewName.EVIDENCE_VIEW, 
				COL_ID_SUPPORTING_TEXT, 
				event -> {
					Annotation annotation = (Annotation) event.getItemId();
		
					_logger.trace("Display 3rd Party Evidence Page for Annotation " + annotation.toString() + "...");
		
					DesktopUI ui = (DesktopUI) UI.getCurrent();
					ui.displayReference(annotation);
				});
	}
}
