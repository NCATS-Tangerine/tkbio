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
package bio.knowledge.service.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;

import bio.knowledge.model.core.IdentifiedEntity;

/**
 * 
 * @author Richard
 * 
 * IdentifiedEntityServiceImpl<T extends IdentifiedEntity> is an abstract superclass
 * encoding generic IdentifierEntity data management behaviours for more specific child data types.
 *
 * @param <T extends IdentifiedEntity> is the data type wrapped here with generic behaviours.
 */
@SpringComponent
@VaadinSessionScope
public abstract class IdentifiedEntityServiceImpl<T extends IdentifiedEntity> 
	implements
		ListTablePager<T>, 
		ListTableEntryCounter, 
		ListTableFilteredHitCounter, 
		ListTablePageCounter, 
		IdentifiedEntityService<T> {
	
	private ListTablePager<T> pager =  
			( int pageNo, int pageSize, String filter, TableSorter sorter, boolean direction) ->
				getDataPage( pageNo, pageSize, filter, sorter, direction) ;
			
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getPager()
	 */
	@Override
	public ListTablePager<T> getPager() { return pager ; }
	
 	/**
 	 * This ListTablePager implementation returns a Page List 
 	 * of data for the specified generic data type.
 	 * 
 	 * The subclass must implement the abstract 
 	 * findAll(Pageable) method for this method to work.
 	 * 
 	 * @param pageNo is equal to or greater than 1
 	 * @param pageSize is equal to or greater than 1
 	 * @return a List ('Page') of DataFile records
 	 */
 	public List<T> getDataPage( int pageIndex, int pageSize, String filter, TableSorter sorter, boolean isAscending) {
 		// Note that PageRequests are zero-based indexed
// 		if( pageNo < 1 ) 
// 			pageNo = 0 ;
// 		else
// 			pageNo-- ;
 		
 		if (pageSize < 1) pageSize = 1 ;
 		
 		Direction direction; 
 		
 		if (isAscending){
 			direction = Direction.ASC;
 		}
 		else {
 			direction = Direction.DESC;
 		}
 		
 		Pageable pageable = new PageRequest(pageIndex, pageSize, 
 									new Sort(direction, sorter.getType())) ;
 		Page<T> page;
 		if (filter.isEmpty()){
			page = this.findAll(pageable);
		} else {
			page = this.findByNameLike(filter, pageable) ;
		}

 		if(page != null)
 			return page.getContent();
 		else
 			return new ArrayList<T>(); // empty list sent if nothing returned?
 	}

	/**
 	 * Method to return pages of text filtered data. 
 	 * Note that specific concrete backend data source 
 	 * implementations of this method should take into 
 	 * account that the filter string might be an embedded one, 
 	 * hence, needs to be delimited by wildcards (e.g. "%" in SQL; 
 	 * perhaps other wildcards for other data sources?)
 	 * 
 	 * @param pageable specification of what slice ("page") of data to return
 	 * @param filter text to filter out matches, usually by name. 
 	 * @return Page of data entries retrieved
 	 */
 	public abstract Page<T> findByNameLike(String filter, Pageable pageable);
 	
 	public abstract Page<T> findAll(Pageable pageable);
 	
	private ListTableEntryCounter entryCounter = () -> countEntries() ;
			
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getEntryCounter()
	 */
	@Override
	public ListTableEntryCounter getEntryCounter() { return entryCounter ; }
	
	/**
 	 * @return total number of entries of data in the database table of a given data type
 	 */
 	public abstract long countEntries() ;
 	
	private ListTableFilteredHitCounter hitCounter = ( String filter ) -> countHits( filter ) ;
			
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getHitCounter()
	 */
	@Override
	public ListTableFilteredHitCounter getHitCounter() { return hitCounter ; }

	/**
 	 * 
 	 * @return total number of hits in the database table matching the given filter
 	 */
 	public long countHits( String filter ) {
 		long hits ;
 		if (filter.isEmpty()){
			hits = countEntries() ;
		} else {
			hits = this.countHitsByNameLike( filter ) ;
		}
 		return hits ;
 	}
 	
 	/**
 	 * 
 	 * @param filter
 	 * @return
 	 */
 	public abstract long countHitsByNameLike(String filter) ;
 	
	private ListTablePageCounter pageCounter = ( String filter, int pageSize ) -> countPages( filter, pageSize ) ;
			
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getPageCounter()
	 */
	@Override
	public ListTablePageCounter getPageCounter() { return pageCounter ; }
	
	/**
 	 * 
 	 * @return total number of pages of data of a given page size, in the database table of a given data type
 	 */
 	public int countPages( String filter, int pageSize ) {
 		int pages ;
 		if (filter.isEmpty()){
			pages = (int)( this.countEntries() / (long)pageSize) + 1 ;
		} else {
			pages = (int)( this.countHitsByNameLike( filter ) / (long)pageSize) + 1 ;
		}
 		return pages ;
 	}
}
