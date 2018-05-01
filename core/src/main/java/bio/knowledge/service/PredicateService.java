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
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.PredicateRepository;
import bio.knowledge.model.Predicate;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

/**
 * @author Richard
 *
 */
@Service
public class PredicateService {
	
	private Logger _logger = LoggerFactory.getLogger(PredicateService.class);
	
	@Autowired
	PredicateRepository predicateRepository;

    @Autowired
    private KnowledgeBeaconService kbService;
	    
    /**
     * 
     * @return
     */
    private static Set<Predicate> predicateListCache = new TreeSet<Predicate>();
    
	public Set<Predicate> findAllPredicates(List<Integer> beacons) {
		
		if(predicateListCache.isEmpty()) {
			/*
			 * Try to populate the list the first time?
			 * Danger is the if some beacons fail to contribute the first time, 
			 * they won't have a second chance?
			 * */
	    	CompletableFuture<Set<Predicate>> future = kbService.getPredicates(beacons);
	    	
	    	try {
	    		predicateListCache = future.get(
						kbService.weightedTimeout(), 
						KnowledgeBeaconService.BEACON_TIMEOUT_UNIT
				);
	    		
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				_logger.warn(e.getMessage());
			}
		}
		return predicateListCache;
    }
    
    class PredicateIndex {
    	
    	PredicateIndex(List<Integer> beacons) {
    		Set<Predicate> predicates = findAllPredicates(beacons) ;
        	addPredicates(predicates);
    	}
    	
    	private String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    	
    	class Node {
    		
    		private Node[] children = new Node[ALPHABET.length()];
    		
    		private Set<Predicate> predicates = new TreeSet<Predicate>();
    		
		    public Optional<Set<Predicate>> getMatchingPredicates(String s) {
		    	
		    	Node node = this;
		    	
		    	for (int i = 0; i < s.length(); i++) {
		    		
		    		int index = ALPHABET.indexOf(s.charAt(i));
		    		
		    		/*
		    		 * ignore unrecognized characters in 
		    		 * predicate names, e.g. spaces and punctuation?
		    		 */
		    		if(index<0) continue;

		    		Node child = node.children[index];
		    		if (child == null) {
		    			// There is no predicates whose name full matches the string
		    			return Optional.empty();
		    		}
		    		node = child;
		    	}
		    	return Optional.of(node.predicates);
		    }
 	
		    public void insert(Predicate p) {
		    	
		    	String s = p.getName().toLowerCase();
		    	
		    	Node node = this;
		    	
		    	/*
		    	 * I'll point to the Predicate at every level it matches(?)
		    	 * I'm not sure how space efficient this is, but it should work
		    	 */
		    	node.predicates.add(p);
		    	
		    	for (int i = 0; i < s.length(); i++) {
		    		
		    		int index = ALPHABET.indexOf(s.charAt(i));
		    		
		    		/*
		    		 * ignore unrecognized characters in 
		    		 * predicate names, e.g. spaces and punctuation?
		    		 */
		    		if(index<0) continue; 
		    		
		    		Node child = node.children[index];
		    		if (child == null) {
		    			// insert new nodes as needed
		    			child = new Node();
		    			node.children[index] = child;
		    		}
		    		node = child;
		    		node.predicates.add(p);
		    	}
		    }
		}
    	
    	private Node root = new Node();
    	
    	public void insert(Predicate predicate) {
    		root.insert(predicate);
    	}
    	
    	public Optional<Set<Predicate>> getMatchingPredicates(String query, List<Integer>beacons) {
    		if(query.isEmpty()) return Optional.of(findAllPredicates(beacons));
    		return root.getMatchingPredicates(query.toLowerCase());
    	}

		public void addPredicates(Set<Predicate> predicates) {
			predicates.stream().forEach(p-> this.insert(p));
		}
    }
    
    // defer initialization until first use?
    private PredicateIndex predicateIndex = null; 
    
	public Optional<Set<Predicate>> getMatchingPredicates(String query, List<Integer> beacons) {
		if(predicateIndex==null)
			predicateIndex = new PredicateIndex(beacons);
		return predicateIndex.getMatchingPredicates(query, beacons);
	}
	
    /**
     * Returns Predicates partitioned by beacon source
     * @return
    public Map<String,List<Predicate>> getPredicateCatalog() {
    	if(catalog.isEmpty()) {
    		// build the catalog
    		predicateListCache.stream().map(p -> p.getBeacons());
    	}
    	return new TreeMap<String,List<Predicate>>();
    }
     */

}
