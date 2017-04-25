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
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.AnnotationRepository;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.Reference;
import bio.knowledge.service.Cache.CacheLocation;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;

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
	private AnnotationRepository annotationRepository ;

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<Annotation> getIdentifiers() {
		return (List<Annotation>)(List)annotationRepository.getAnnotations();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Annotation> getIdentifiers(Pageable pageable) {
		return (Page<Annotation>)(Page)annotationRepository.findAll(pageable);
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
	public Reference getReference(Annotation annotation) {
		return  annotationRepository.findReferenceByAnnotation(annotation.getAccessionId()) ;
	}

	private Page<Annotation> findHelper(String filter, Pageable pageable) {

		Optional<Evidence> evidenceOpt = query.getCurrentEvidence() ;

		if( !evidenceOpt.isPresent() ) return null ;

		Evidence evidence = evidenceOpt.get() ;
		
		Long evidenceId = evidence.getId();
		String pageKey = new Integer(pageable.hashCode()).toString();
		CacheLocation cacheLocation = 
				cache.searchForResultSet(
						"Annotation", 
						evidenceId.toString(), 
						new String[] { filter, pageKey }
				);
		
		@SuppressWarnings("unchecked")
		List<Annotation> cachedResult = (List<Annotation>) cacheLocation.getResultSet();
		
		List<Annotation> annotations;
		
		if (cachedResult == null) {
			
			String userId = query.currentUserId();
			List<Map<String, Object>> data;

			if(filter.trim().isEmpty()){
				// TODO: Should we be accessing the repository in this way in a service?
				data = annotationRepository.findByEvidence((Evidence) evidence, pageable, userId);
			} else {
				// TODO: Should we be accessing the repository in this way in a service?
				data = annotationRepository.findByEvidenceFiltered((Evidence) evidence, filter, pageable, userId);
			}
			
			annotations = new ArrayList<>();
			
			for (Map<String, Object> d : data) {
				Annotation annotation = (Annotation) d.get("annotation");
				Reference   reference = (Reference)  d.get("reference");
				annotation.setReference(reference);
				annotations.add(annotation);
			}
			
			cacheLocation.setResultSet(annotations);
		} else {
			annotations = cachedResult;
		}
		return new PageImpl<Annotation>(annotations, pageable, annotations.size());

	}
	
	private long countHelper(String filter) {
		
		Optional<Evidence> evidenceOpt = query.getCurrentEvidence() ;
		if( !evidenceOpt.isPresent() ) return 0 ;
		Evidence evidence = evidenceOpt.get() ;
		
		Long evidenceId = evidence.getId();
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
		
		if (count == null) {
			if (filter.trim().isEmpty()) {
				count = annotationRepository.countByEvidence(evidence);
			} else {
				count = annotationRepository.countByEvidenceFiltered(evidence, filter);
			}
			// put fetched result to map before returning
			//cache.getCountCache().put(cacheKey, count);
			cacheLocation.setCounter(count);
		}
		
		return count;
	}

	private Page<Annotation> findHelperByPMID(String filter, Pageable pageable){
		
		Optional<String> currentPmidOpt = query.getCurrentPmid() ;
		
		if( !currentPmidOpt.isPresent() ) return null ;
		
		String pmid = currentPmidOpt.get() ;
		List<Map<String, Object>> data;
		if(filter.trim().isEmpty()){
			 data = annotationRepository.findByPMID(pmid, pageable);
		}else{
			 data = annotationRepository.findByPMIDFiltered(pmid, filter, pageable);
		}
		List<Annotation> results = new ArrayList<>();
		for (Map<String, Object> d : data) {
			Annotation annotation       = (Annotation) d.get("annotation");
			Reference   fromDbreference = (Reference)   d.get("reference");
			annotation.setReference(fromDbreference);
			results.add(annotation);
		}
		return new PageImpl<Annotation>(results, pageable, results.size());
		
	}
	
	private long countHelperByPMID(String filter){
		Optional<String> currentPmidOpt = query.getCurrentPmid() ;
		if( !currentPmidOpt.isPresent() ) return 0 ;
		
		String pmid = currentPmidOpt.get() ;
		
		// creating cache key using (conceptId + textFilter)
		//String cacheKey = (pmid + "#" + filter);
		//cacheKey = Base64.getEncoder().encodeToString(cacheKey.getBytes());

		CacheLocation cacheLocation = 
				cache.searchForCounter("PMID", pmid, new String[] { filter });
		
		// Is key present ? then fetch it from cache
		// Long count = cache.getCountCache().get(cacheKey);
		Long count = cacheLocation.getCounter();
		
		if (count == null) {
			if (filter.trim().isEmpty()) {
				count = annotationRepository.countByPMID(pmid);
			} else {
				count = annotationRepository.countByPMIDFiltered(pmid, filter);
			}
			// put fetched result to map before returning
			//cache.getCountCache().put(cacheKey, count);
			cacheLocation.setCounter(count);
		}
		
		return count;
	}

	/**
	 * @param annotation
	 * @return
	 */
	public Annotation save(Annotation annotation) {
		return annotationRepository.save(annotation);
	}

	/**
	 * @param annotationId
	 * @return
	 */
	public Annotation findByAccessionId(String annotationId) {
		return annotationRepository.findByAccessionId(annotationId);
	}
}
