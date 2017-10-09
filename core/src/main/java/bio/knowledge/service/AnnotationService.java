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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.datasource.DataService;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.Reference;
import bio.knowledge.model.Statement;
import bio.knowledge.service.Cache.CacheLocation;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.core.TableSorter;

/**
 * @author Richard
 *
 */
@Service
public class AnnotationService extends IdentifiedEntityServiceImpl<Annotation> {
	
	@Autowired
	private KBQuery query ;
	
	@Autowired
	private Cache cache;
    
    @Autowired
    private KnowledgeBeaconService kbService;
    
    @Override
    public List<Annotation> getDataPage(
    		int pageIndex,
    		int pageSize,
    		String filter,
    		TableSorter sorter,
    		boolean isAscending
    ) {
    	Optional<Statement> statementOpt = query.getCurrentStatement();
		if( !statementOpt.isPresent() ) return new ArrayList<Annotation>() ;
		Statement statement = statementOpt.get();
		
    	CompletableFuture<List<Annotation>> future =
    			kbService.getEvidences(statement, filter, pageIndex, pageSize);
    	
    	try {
			List<Annotation> annotations =
					future.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
			return annotations;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return new ArrayList<Annotation>();
		}
    }

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	@Deprecated
	public List<Annotation> getIdentifiers() {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Deprecated
	public Page<Annotation> getIdentifiers(Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Annotation> findByNameLike(String filter, Pageable pageable) {
		if ( !query.getCurrentEvidence().isPresent() ) {
			switch (query.getRelationSearchMode()) {
				case PMID:
					return findHelperByPMID(filter, pageable);
				case RELATIONS:
					return findHelper(filter, pageable);
				case WIKIDATA:
					return null;
				default:
					throw new DataServiceException("AnnotationService.findByNameLike(): Invalid RelationSearchMode()?");
			}
		} else
			return findHelper(filter, pageable);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Annotation> findAll(Pageable pageable) {
		if( !query.getCurrentEvidence().isPresent() ){
			switch (query.getRelationSearchMode()) {
				case PMID:
					return findHelperByPMID("", pageable);
				case RELATIONS:
					return findHelper("", pageable);
				case WIKIDATA:
					return null;
				default:
					throw new DataServiceException("AnnotationService.findAll(): Invalid RelationSearchMode()?");
			} 
		} else
			return findHelper("", pageable);
	}

	

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countEntries()
	 */
	@Override
	public long countEntries() {
		if( query.getCurrentEvidence().isPresent())
			switch (query.getRelationSearchMode()) {
				case PMID:
					return countHelperByPMID("");
				case RELATIONS:
					return countHelper("");
				case WIKIDATA:
					return 0L;
				default:
					throw new DataServiceException("AnnotationService.countEntries(): Invalid RelationSearchMode()?");
			} 
		else
			return 0L;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countHitsByNameLike(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		if( query.getCurrentEvidence().isPresent() )
			switch (query.getRelationSearchMode()) {
				case PMID:
					return countHelperByPMID(filter);
				case RELATIONS:
					return countHelper(filter);
				case WIKIDATA:
					return 0L;
				default:
					throw new DataServiceException("AnnotationService.countHitsByNameLike(): Invalid RelationSearchMode()?");
			} 
		else
			return countHelper(filter);
	}
	
	/**
	 * 
	 * @param annotation
	 * @return
	 */
	@Deprecated
	public Reference getReference(Annotation annotation) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	@Deprecated
	private Page<Annotation> findHelper(String filter, Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	
	private long countHelper(String filter) {
		
		Optional<Evidence> evidenceOpt = query.getCurrentEvidence() ;
		if( !evidenceOpt.isPresent() ) return 0 ;
		Evidence evidence = evidenceOpt.get() ;
		
		Long evidenceId = evidence.getDbId();
		// creating cache key using (evidenceId + textFilter)
		//String cacheKey = (evidenceId + "#" + filter);
		//cacheKey = Base64.getEncoder().encodeToString(cacheKey.getBytes());
		CacheLocation cacheLocation = 
				cache.searchForCounter(
						"Annotation", 
						evidenceId.toString(), 
						new String[] { filter }
				);
		
		// Is key present ? then fetch it from cache
		// Long count = cache.getCountCache().get(cacheKey);
		Long count = cacheLocation.getCounter();
		
		
		return 0;
		// TODO: Figure out why this code was not working??? But it's being phased out anyway...
		
//		if (count == null && evidence != null) {
//			
//			if (filter.trim().isEmpty()) {
//				count = annotationRepository.countByEvidence(evidence);
//			} else {
//				count = annotationRepository.countByEvidenceFiltered(evidence, filter);
//			}
//			// put fetched result to map before returning
//			//cache.getCountCache().put(cacheKey, count);
//			cacheLocation.setCounter(count);
//		}
//		
//		return count;
	}

	@Deprecated
	private Page<Annotation> findHelperByPMID(String filter, Pageable pageable){
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	
	@Deprecated
	private long countHelperByPMID(String filter){
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/**
	 * @param annotation
	 * @return
	 */
	@Deprecated
	public Annotation save(Annotation annotation) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/**
	 * @param annotationId
	 * @return
	 */
	@Deprecated
	public Annotation findById(String annotationId) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
}
