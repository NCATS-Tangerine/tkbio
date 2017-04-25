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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.v7.ui.renderers.ButtonRenderer;
import com.vaadin.v7.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.v7.ui.renderers.Renderer;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.service.core.IdentifiedEntityService;
import bio.knowledge.service.core.ListTableEntryCounter;
import bio.knowledge.service.core.ListTableFilteredHitCounter;
import bio.knowledge.service.core.ListTablePageCounter;
import bio.knowledge.service.core.ListTablePager;

/**
 * The "Registry" is a mechanism for various object services to 
 * register some meta-data about how to display their data type 
 * properly in the ListView data tables.
 * 
 * @author Richard
 *
 */
@SpringComponent
@VaadinSessionScope
public class Registry {
	
	// Singleton
	//static final private Registry registry = new Registry() ;
	
	public class Mapping {
		
		/**
		 * 
		 * @param container to which the Mapping belongs
		 */
		public Mapping(BeanItemContainer<? extends IdentifiedEntity> container) {
			this.container = container ;
		}
		
		private BeanItemContainer<? extends IdentifiedEntity> container ;
		
		/**
		 * 
		 * @return bean item container belonging to this Mapping instance
		 */
		public BeanItemContainer<? extends IdentifiedEntity> getContainer() {
			return container ;
		}	
		
		private List<String> columns = new ArrayList<String>() ;
		
		/**
		 * 
		 * @param name
		 */
		public void addColumn(String name) {
			columns.add(name) ;
		}
		
		/**
		 * 
		 * @return
		 */
		public List<String> getColumns() { return columns; }
		
		private List<String> detailFields = new ArrayList<String>() ;
		
		/**
		 * 
		 * @param name
		 */
		public void addDetailFields(String name) {
			detailFields.add(name) ;
		}
		
		/**
		 * 
		 * @return
		 */
		public List<String> getDetailFields() {
			return detailFields; 
		}
		
		private Boolean hasDownloadableData = false ;
		
		/**
		 * 
		 * @param status
		 */
		public void hasDownloadableData(Boolean status) {
			hasDownloadableData = status ;
		}
		
		/**
		 * 
		 * @return
		 */
		public Boolean hasDownloadableData() {
			return this.hasDownloadableData ;
		}
		
		private ListTablePager<? extends IdentifiedEntity> pager = null ;
		
		/**
		 * 
		 * @param pager
		 */
		public void addPager( ListTablePager<? extends IdentifiedEntity> pager ) {
			this.pager = pager ;
		}
		
		/**
		 * 
		 * @return
		 */
		public ListTablePager<? extends IdentifiedEntity> getPager() {
			return pager ;
		}
		
		private ListTablePageCounter pageCounter = null ;
		
		/**
		 * @param pageCounter
		 */
		public void setPageCounter(ListTablePageCounter pageCounter) {
			this.pageCounter = pageCounter ;
		}
		
		/**
		 * 
		 * @return
		 */
		public ListTablePageCounter getPageCounter() {
			return pageCounter ;
		}

		private ListTableEntryCounter entryCounter = null ;
		
		/**
		 * @param entryCounter list table entry lambda API
		 */
		public void setEntryCounter(ListTableEntryCounter entryCounter) {
			this.entryCounter = entryCounter ;
		}
		
		/**
		 * @return ListTableEntryCounter lambda binding
		 */
		public ListTableEntryCounter getEntryCounter() {
			return entryCounter;
		}

		private ListTableFilteredHitCounter hitCounter = null ;
		
		/**
		 * @param hitCounter
		 */
		public void setHitCounter( ListTableFilteredHitCounter hitCounter) {
			this.hitCounter = hitCounter ;
		}
		/**
		 * @return
		 */
		public ListTableFilteredHitCounter getHitCounter() {
			return hitCounter;
		}
		
		private Map<String,RendererClickListener> selectionHandlers = 
				new HashMap<String, RendererClickListener>();
		
		public void addSelectionHandler( String name, RendererClickListener handler) {
			selectionHandlers.put( name, handler ) ;
		}
		
		/**
		 * @return Map of SelectionHandler instances keyed by ListView column names
		 */
		public Map<String, RendererClickListener> getSelectionHandlers() {
			return selectionHandlers ;
		}

		private Map<String,Renderer<?>> rendererMap = 
					new HashMap<String,Renderer<?>>() ;
		
		/**
		 * @param column
		 * @param renderer
		 */
		public void addColumnRenderer(String column, Renderer<?> renderer) {
			this.rendererMap.put( column, renderer ) ;
		}
		
		/**
		 * 
		 * @param column
		 * @return
		 */
		public Renderer<?> getColumnRenderer(String column){
			return rendererMap.getOrDefault(column, new ButtonRenderer()) ;
		}

	}
	
	private Map<String,Mapping> services = new HashMap<String,Mapping>() ;
	
	private Mapping add(String datatype, Mapping mapping) {
		return services.put(datatype, mapping) ;
	}
	
	/**
	 * 
	 * @param name of data type to which the Mapping belongs
	 * @param container for bean data items
	 * @param ieService IdentifiedEntityService api binding
	 * @param columns to view in main table
	 * @param detailFields - String array of "field names", "field name|" or "field name\|\<url template\>"
	 * @param hasDownloadableData flag to indicate that named data type is downloadable
	 * 
	 * See the 'ListView.onShowDetails() method for more information 
	 * about the semantics of the Mapping meta-data
	 */
	 public void setMapping(
			String name,
			BeanItemContainer<? extends IdentifiedEntity> container,
			IdentifiedEntityService<?> ieService,
			String[]   columns,
			String[] detailFields,
			Boolean hasDownloadableData
		) {
		
		Mapping mapping = this.new Mapping(container) ;
		
		if( ieService != null ) {
			
			ListTablePager<? extends IdentifiedEntity> pager = ieService.getPager() ;
			if(pager != null) {
				mapping.addPager(pager) ;
			}
			
			ListTableEntryCounter entryCounter = ieService.getEntryCounter() ;
			if( entryCounter != null ) {
				mapping.setEntryCounter(entryCounter) ;
			}
			
			ListTableFilteredHitCounter hitCounter = ieService.getHitCounter() ;
			if( hitCounter != null ) {
				mapping.setHitCounter(hitCounter) ;
			}
			
			ListTablePageCounter pageCounter = ieService.getPageCounter() ;
			if( pageCounter != null ) {
				mapping.setPageCounter(pageCounter) ;
			}
		}
		
		for(String column : columns) {
			mapping.addColumn(column);
		}

		if( detailFields != null)
			for(String field : detailFields) {
				mapping.addDetailFields(field);
			}
		
		mapping.hasDownloadableData(hasDownloadableData) ;
		
		this.add(name, mapping) ;
	}

	 public void setMapping(
			String name,
			BeanItemContainer<? extends IdentifiedEntity> container,
			String[]   columns,
			String[]   detailFields,
			Boolean    hasDownloadableData
	) {
		this.setMapping(name, container, null, columns, detailFields, hasDownloadableData);
	}
	
	public void setMapping(
			String name,
			BeanItemContainer<? extends IdentifiedEntity> container,
			IdentifiedEntityService<?> ieService,
			String[]   columns,
			String[]   detailFields
		) {
		this.setMapping(name, container, ieService, columns, detailFields, false);
	}
	
	public void setMapping(
			String name,
			BeanItemContainer<? extends IdentifiedEntity> container,
			String[]   columns,
			String[]   detailFields
		) {
		this.setMapping(name, container, null, columns, detailFields, false);
	}
	
	 public Mapping getMapping(String name) {
		return this.services.get(name) ;
	}
	
	private static Map<String,Boolean> specialFilterMap = new HashMap<String,Boolean>() ;
	
	/**
	 * Method to assign a special filtering widget to a given ListView
	 * e.g. "SpecialFilter by Semantic Type" for SemMedDb explicit relations
	 * 
	 * @param filter defining a Vaadin GUI component and callback behaviour
	 */
	static public void hasSemanticFilter(String name, Boolean flag) {
		if(name!=null) specialFilterMap.put(name, flag) ;
	}
	
	static public Boolean hasSemanticFilter(String datatype) {
		return specialFilterMap.getOrDefault(datatype, false) ;
	}
	
	 public boolean addSelectionHandler( 
			String name, 
			String column, 
			RendererClickListener handler
	) {
		Mapping mapping = this.getMapping(name) ;
		if( mapping == null ) return false ;
		
		mapping.addSelectionHandler(column, handler);
		
		return true ;
	}
	
	 public boolean addColumnRenderer( 
			String name, 
			String column, 
			Renderer<?> renderer
	) {
		Mapping mapping = this.getMapping(name) ;
		if( mapping == null ) return false ;
		
		mapping.addColumnRenderer(column, renderer);
		
		return true ;
	}
}
