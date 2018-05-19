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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.knowledge.model.datasource.ResultSet;

/**
 * @author Richard
 *
 */
public interface DataServiceUtility {
	
	public Logger _logger = LoggerFactory.getLogger(DataServiceUtility.class);
	
	default void runSimpleQuery(
			DataService ds,
			String input, 
			Function<ResultSet,Void> handler 
	)  throws Exception {
		
		if( !ds.isSimple() ) 
			throw new DataSourceException( 
					"ConceptService.runSimpleQuery() error: "
					+ "simple data source expected?" );
		
		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = (SimpleDataService<String>)ds;
		
		CompletableFuture<ResultSet> futureResultSet = sds.query(input);
		try {
			futureResultSet.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
			futureResultSet.thenApply(handler);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			futureResultSet.completeExceptionally(e);
			throw e;
		}		
	}
	
	default void runComplexQuery(
			DataService ds,
			Map<String, Object> args, 
			Function<ResultSet,Void> handler 
	)  throws Exception {
		
		if( ds.isSimple() ) 
			throw new DataSourceException( 
					"ConceptService.runComplexQuery() error: "
					+ "complex data source expected?" );
		
		ComplexDataService cds =  (ComplexDataService)ds ;
		
		CompletableFuture<ResultSet> futureResultSet = cds.query(args);
		try {
			futureResultSet.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
			futureResultSet.thenApply(handler);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			futureResultSet.completeExceptionally(e);
			throw e;
		}		
	}
	
	default void processResults(
			CompletableFuture< ResultSet > futureResult,
			Function<? super ResultSet, ? extends Void> resultHandler 
	) {
		try {
			futureResult.get( DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT );
			futureResult.thenApply(resultHandler);
	
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			futureResult.completeExceptionally(e);
		} catch (Exception e) {
		}
	}
	
	default Void dumpResults( ResultSet rs ) {
		rs.stream().forEach(r->{
			_logger.info(r.toString());
		});
		return (Void)null ;
	}

}
