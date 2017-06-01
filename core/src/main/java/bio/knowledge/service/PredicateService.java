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
package bio.knowledge.service;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.datasource.DataSourceRegistry;
import bio.knowledge.model.Predicate;

/**
 * @author Richard
 *
 */
@Service
public class PredicateService {
	
	private Logger _logger = LoggerFactory.getLogger(PredicateService.class);

	@Autowired
	private Cache cache;
	
	@Autowired
	private DataSourceRegistry dataSourceRegistry ;
	
    /**
     * 
     * @param name
     * @return
     */
    public Predicate findPredicateByName(String name) {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
    
    /**
     * 
     * @return
     */
    public List<Predicate> findAllPredicates() {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
    
    /**
     * 
     * @param predicate
     * @return
     */
    public Predicate annotate(Predicate predicate) {
    	throw new NotImplementedException("Removed all reference to neo4j");
    }
}
