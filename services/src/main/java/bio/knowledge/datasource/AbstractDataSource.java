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

package bio.knowledge.datasource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import bio.knowledge.model.ConceptType;

/**
 * @author Richard
 *
 */
public abstract class AbstractDataSource 
	extends AbstractDescribed implements DataSource {
	
	private final Map<ConceptType,Set<DataService>> dataServicesBySemanticGroup = 
			new HashMap<ConceptType,Set<DataService>>() ;
	
	private final Map<String,DataService> dataServicesByIdentifier = 
			new HashMap<String,DataService>() ;
	
	/**
	 * 
	 * @param dataSourceId
	 */
	protected AbstractDataSource( String dataSourceId ) {
		this(dataSourceId,"") ;
	}

	/**
	 * 
	 * @param dataSourceId
	 */
	protected AbstractDataSource(String dataSourceId,String name) {
		super(dataSourceId,name) ;
		initialize() ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bio.knowledge.datasource.DataSource#getTargetSemanticGroups()
	 */
	@Override
	public Set<ConceptType> getTargetSemanticGroups() {
		return new HashSet<ConceptType>( dataServicesBySemanticGroup.keySet() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.datasource.DataSource#getAllServices()
	 */
	@Override
	public Set<DataService> getAllServices( ) {
		return new HashSet<DataService>( dataServicesByIdentifier.values() ) ; 
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.datasource.DataSource#getServicesBySemanticGroup(bio.knowledge.model.
	 * Concept.SemanticGroup)
	 */
	@Override
	public Set<DataService> getServicesBySemanticGroup( ConceptType type ) {
		Set<DataService> dataServices = new HashSet<DataService>();
		if(dataServicesBySemanticGroup.containsKey(type)) {
			dataServices.addAll( dataServicesBySemanticGroup.get( type ) ) ;
		}
		return dataServices;
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.datasource.DataSource#getServiceByIdentifier(java.lang.String)
	 */
	public Optional<DataService> getServiceByIdentifier( String identifier ) {
		if( dataServicesByIdentifier.containsKey(identifier) )
			return  Optional.of( dataServicesByIdentifier.get(identifier) ) ;
		return Optional.empty() ;
	}
	
	/**
	 * Method to add a single Typed DataService to a DataSource
	 * @param dataService added to the DataSource
	 * @throws DataSourceException if DataService identifier is duplicated.
	 */
	protected void addDataService( DataService dataService ) {
		ConceptType type = dataService.getTargetSemanticGroup() ;
		Set<DataService> typedDataServicesSet ;
		if( dataServicesBySemanticGroup.containsKey(type) )
			typedDataServicesSet = dataServicesBySemanticGroup.get( type ) ;
		else {
			typedDataServicesSet = new HashSet<DataService>() ;
			dataServicesBySemanticGroup.put( type, typedDataServicesSet ) ;
		}
		
		if( !typedDataServicesSet.contains( dataService ) ) {
			typedDataServicesSet.add( dataService ) ;
			dataServicesByIdentifier.put( dataService.getIdentifier(), dataService ) ;
			
		} else
			throw new DataSourceException("AbstractDataSource.addDataService() error: "
					+ "attempting to add a duplicate DataService '"+dataService.getIdentifier()+"'"
			) ;
	}
	
	/**
	 * Classes extending AbstractDataSource need to implement this abstract method 
	 * to initialize internal specifics of a DataSource, that is, need to initialize
	 * instances of all the available DataService types and add them into the internal map
	 * using the addDataService( SemanticGroup type, DataService dataService ) method.
	 */
	protected abstract void initialize() ; 
}
