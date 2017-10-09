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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bio.knowledge.datasource.gene.MyGeneInfoDataSource;
import bio.knowledge.datasource.wikidata.WikiDataDataSource;

/**
 * @author Richard
 */
// Not sure if this could or should be classified as a a Spring @Service
// Should also likely be a singleton in the system
@Component 
public class DataSourceRegistry {
	
	private final Map<String,DataSource> dataSourceMap = new HashMap<String,DataSource>() ;
	
	/**
	 * @return Set of all DataSources known to the system
	 */
	public Set<DataSource> getDataSources( ) { 
		return new HashSet<DataSource>(dataSourceMap.values()) ; 
	}
	
	/**
	 * 
	 * @param dataSource added to the system
	 */
	public void addDataSource( DataSource dataSource ) {
		dataSourceMap.put( dataSource.getIdentifier(), dataSource ) ;
	}
	
	/**
	 * @param dataSources Set added to the system
	 */
	public void setDataSources( Set<DataSource> dataSources ) {
		for( DataSource ds : dataSources) {
			dataSourceMap.put(ds.getIdentifier(), ds) ;
		}
	}
	
	public Optional<DataSource> getDataSourceByIdentifier(String identifier) {
		if(dataSourceMap.containsKey(identifier))
			return  Optional.of(dataSourceMap.get(identifier)) ;
		return Optional.empty() ;
	}
	
	@Autowired
	private MyGeneInfoDataSource myGenesInfoDataSource ;
	
	@Autowired
	private WikiDataDataSource wikiDataDataSource ;
	
	@PostConstruct
	private void initialize() {
		// TODO: Need to load the registry with (SPI like?) discoverable data sources 
		// For now, we hard code in the small number of available data sources
		addDataSource(myGenesInfoDataSource) ;
		addDataSource(wikiDataDataSource) ;
	}
	
	/* 
	 * Mappings of Default DataServices by "NameSpace" identifiers
	 */
	private static Map<String,String[]> dataSourceByNameSpace = new HashMap<String,String[]>() ;
	
	static {
		dataSourceByNameSpace.put(
				"wd", 
				new String[]{
						WikiDataDataSource.WIKIDATA_DATASOURCE_ID,  
						WikiDataDataSource.WD_SDS_5_ID 						
				}  
		) ;
	}
	
	/**
	 * Retrieve a DataService by Qualified Data NameSpace
	 * 
	 * @param nameSpace
	 * @return
	 */
	public DataService getDefaultDataService(String nameSpace) {
		DataService ds = null ;
		if(dataSourceByNameSpace.containsKey(nameSpace)) {
			String[] dsId = dataSourceByNameSpace.get(nameSpace) ;
			ds = getDataService( dsId[0], dsId[1] ) ;
		}
		return ds ;
	}
	
	/**
	 * Retrieve a DataService by DataSource and DataService names
	 * 
	 * @param dataSourceName
	 * @param dataServiceName
	 * @return
	 */
	public DataService getDataService(String dataSourceName, String dataServiceName ) {
			
		Optional<DataSource> dsOpt = getDataSourceByIdentifier(dataSourceName) ;
		if(! dsOpt.isPresent() ) 
			throw new DataSourceException(
					"DataSourceRegistry error: Unknown DataSource '"+dataSourceName+"'"
			);
		
		DataSource dataSource = dsOpt.get() ;
		
		Optional<DataService> serviceOpt = dataSource.getServiceByIdentifier(dataServiceName) ;
		if(! serviceOpt.isPresent()) 
			throw new DataSourceException(
					"DataSourceRegistry error: Unknown DataService '"+
					dataServiceName+"' for DataSource '"+dataSourceName+"'"
			);
		
		DataService dataService = serviceOpt.get() ;
		
		return dataService ;
	}

}
