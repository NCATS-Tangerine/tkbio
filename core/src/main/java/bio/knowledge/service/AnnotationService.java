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
import bio.knowledge.model.EvidenceCode;
import bio.knowledge.model.neo4j.Neo4jAnnotation;
import bio.knowledge.model.neo4j.Neo4jEvidence;
import bio.knowledge.model.neo4j.Neo4jReference;
import bio.knowledge.model.neo4j.Neo4jAnnotation.Type;
import bio.knowledge.service.Cache.CacheLocation;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;

/**
 * @author Richard
 *
 */
@Service
public class AnnotationService extends IdentifiedEntityServiceImpl<Neo4jAnnotation> {
	
	@Autowired
	private KBQuery query ;
	
	@Autowired
	private Cache cache;

    @Autowired
	private AnnotationRepository annotationRepository ;

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	@Override
	public Neo4jAnnotation createInstance(Object... args) {
		if(args.length==5)
			return new Neo4jAnnotation(
		    		(String) args[0],      // accessionId
		    		(String) args[1],      // name == text of annotation
		    		(Type)   args[2],      // Annotation.Type
				    (EvidenceCode) args[3],
				    (Neo4jReference) args[4]
			) ;
		else
			throw new RuntimeException("Invalid AnnotationPredicationService.createInstance() arguments?") ;
	}
   
    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<Neo4jAnnotation> getIdentifiers() {
		return annotationRepository.getAnnotations();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Neo4jAnnotation> getIdentifiers(Pageable pageable) {
		return (Page<Neo4jAnnotation>)(Page)annotationRepository.findAll(pageable);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Neo4jAnnotation> findByNameLike(String filter, Pageable pageable) {
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
	public Page<Neo4jAnnotation> findAll(Pageable pageable) {
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
	public Neo4jReference getReference(Neo4jAnnotation annotation) {
		return  annotationRepository.findReferenceByAnnotation(annotation.getAccessionId()) ;
	}

	private Page<Neo4jAnnotation> findHelper(String filter, Pageable pageable) {

		Optional<Neo4jEvidence> evidenceOpt = query.getCurrentEvidence() ;

		if( !evidenceOpt.isPresent() ) return null ;

		Neo4jEvidence evidence = evidenceOpt.get() ;
		
		Long evidenceId = evidence.getId();
		String pageKey = new Integer(pageable.hashCode()).toString();
		CacheLocation cacheLocation = 
				cache.searchForResultSet(
						"Annotation", 
						evidenceId.toString(), 
						new String[] { filter, pageKey }
				);
		
		@SuppressWarnings("unchecked")
		List<Neo4jAnnotation> cachedResult = (List<Neo4jAnnotation>) cacheLocation.getResultSet();
		
		List<Neo4jAnnotation> annotations;
		
		if (cachedResult == null) {
			
			String userId = query.currentUserId();
			List<Map<String, Object>> data;

			if(filter.trim().isEmpty()){
				 data = annotationRepository.findByEvidence(evidence, pageable, userId);
			} else {
				 data = annotationRepository.findByEvidenceFiltered(evidence, filter, pageable, userId);
			}
			
			annotations = new ArrayList<>();
			
			for (Map<String, Object> d : data) {
				Neo4jAnnotation annotation = (Neo4jAnnotation) d.get("annotation");
				Neo4jReference   reference = (Neo4jReference)  d.get("reference");
				annotation.setReference(reference);
				annotations.add(annotation);
			}
			
			cacheLocation.setResultSet(annotations);
		} else {
			annotations = cachedResult;
		}
		return new PageImpl<Neo4jAnnotation>(annotations, pageable, annotations.size());

	}
	
	private long countHelper(String filter) {
		
		Optional<Neo4jEvidence> evidenceOpt = query.getCurrentEvidence() ;
		if( !evidenceOpt.isPresent() ) return 0 ;
		Neo4jEvidence evidence = evidenceOpt.get() ;
		
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

	private Page<Neo4jAnnotation> findHelperByPMID(String filter, Pageable pageable){
		
		Optional<String> currentPmidOpt = query.getCurrentPmid() ;
		
		if( !currentPmidOpt.isPresent() ) return null ;
		
		String pmid = currentPmidOpt.get() ;
		List<Map<String, Object>> data;
		if(filter.trim().isEmpty()){
			 data = annotationRepository.findByPMID(pmid, pageable);
		}else{
			 data = annotationRepository.findByPMIDFiltered(pmid, filter, pageable);
		}
		List<Neo4jAnnotation> results = new ArrayList<>();
		for (Map<String, Object> d : data) {
			Neo4jAnnotation annotation       = (Neo4jAnnotation) d.get("annotation");
			Neo4jReference   fromDbreference = (Neo4jReference)   d.get("reference");
			annotation.setReference(fromDbreference);
			results.add(annotation);
		}
		return new PageImpl<Neo4jAnnotation>(results, pageable, results.size());
		
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
	public Neo4jAnnotation save(Neo4jAnnotation annotation) {
		return annotationRepository.save(annotation);
	}

	/**
	 * @param annotationId
	 * @return
	 */
	public Neo4jAnnotation findByAccessionId(String annotationId) {
		return annotationRepository.findByAccessionId(annotationId);
	}
}
