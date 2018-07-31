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
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bio.knowledge.database.repository.ConceptMapArchiveRepository;
import bio.knowledge.database.repository.LibraryRepository;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.Library;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.core.TableSorter;

/**
 * @author Richard
 * @author Chandan Mishra
 */
@Service
public class ConceptMapArchiveService extends IdentifiedEntityServiceImpl<ConceptMapArchive> {
	public enum SearchMode {
		ALL_MAPS, AUTHORED_MAPS, SHARED_MAPS
	}
	
	private SearchMode searchMode = SearchMode.ALL_MAPS;
	
	public void setSearchMode(SearchMode mode) {
		this.searchMode = mode;
	}
	
	@Override
    public List<ConceptMapArchive> getDataPage(
    		int pageIndex,
    		int pageSize,
    		List<String> filter,
    		TableSorter sorter,
    		boolean isAscending
    ) {
		String accountId = authState.getUserId();
		String[] groupIds = authState.getGroupIds();
		String simpleTextFilter = query.getSimpleTextFilter();
		String[] keywords = simpleTextFilter == null || simpleTextFilter.isEmpty() ? null : simpleTextFilter.split(" ");
		
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
		
		List<Map<String, Object>> data;
		
		switch (searchMode) {
		case ALL_MAPS:
			data = archiveRepository.apiGetPublicConceptMaps(keywords, accountId, groupIds, pageIndex, pageSize);
			break;
		case AUTHORED_MAPS:
			data = archiveRepository.apiGetPersonalConceptMaps(keywords, accountId, groupIds, pageIndex, pageSize);
			break;
		case SHARED_MAPS:
			data = archiveRepository.apiGetSharedConceptMaps(keywords, accountId, groupIds, pageIndex, pageSize);
			break;
		default:
			throw new RuntimeException("SearchMode option " + searchMode.toString() + " is not implemented");
		}
		
		List<ConceptMapArchive> conceptMapArchives = new ArrayList<ConceptMapArchive>();
		
		for (Map<String, Object> d : data) {
			ConceptMapArchive archive = (ConceptMapArchive) d.get("conceptMap");
			Library parent = (Library) d.get("parents");
			archive.setParents(parent);
			
			conceptMapArchives.add(archive);
		}
		
		return conceptMapArchives;
	}
	
	@Autowired
	private KBQuery query;

	private static final String SEPARATOR = " ";

	@Autowired
	private ConceptMapArchiveRepository archiveRepository;

	@Autowired
	private LibraryRepository libraryRepository;
	
	@Autowired
	private AuthenticationState authState;

	/**
	 * Create ConceptMapArchive node and attach library to Concept node
	 * 
	 * @param conceptMapArchive
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Throwable.class })
	public boolean save(ConceptMapArchive conceptMapArchive, String usersAccountId, boolean isPublic) {
		conceptMapArchive.setAuthorsAccountId(usersAccountId);
		conceptMapArchive.setPublic(isPublic);
		conceptMapArchive.setDateLastModifiedToNow();

		if (conceptMapArchive.getParents() != null) {
			// save any associated parent library?
			Library parentMaps = conceptMapArchive.getParents();
			parentMaps.setName("Parents of Concept Map " + conceptMapArchive.getName());
			parentMaps = libraryRepository.save(parentMaps);
			conceptMapArchive.setParents(parentMaps);
		}
		ConceptMapArchive savedConceptMap = archiveRepository.save(conceptMapArchive);
		Set<String> conceptAccessionIds = query.getNodeIdsfromConceptMap();
		for (String accessionId : conceptAccessionIds) {
			archiveRepository.attachLibraryToConcept( savedConceptMap.getDbId(), accessionId );
		}
		return true;
	}

	private Page<ConceptMapArchive> parseLibraryResult(List<Map<String, Object>> data, Pageable pageable) {

		List<ConceptMapArchive> conceptMapArchives = new ArrayList<>();

		for (Map<String, Object> d : data) {
			ConceptMapArchive cm = (ConceptMapArchive) d.get("conceptMap");
			// if parents return add it
			if (d.get("parents") != null) {
				Library parent = (Library) d.get("parents");
				cm.setParents(parent);
			}

			conceptMapArchives.add(cm);
		}

		return new PageImpl<ConceptMapArchive>(conceptMapArchives, pageable, conceptMapArchives.size());
	}

	/**
	 * This API will return ConceptMapArchive associated with Library. (On Click
	 * of Library in first ConceptSearchWindow)
	 * 
	 * @param library
	 * @param filter
	 * @param pageable
	 * @return
	 */
	public Page<ConceptMapArchive> getConceptMapArchiveByLibraryFiltered(
			Library library, 
			List<String> filter,
			Pageable pageable, 
			String accountId, 
			String[] groupIds
	) {
		List<Map<String, Object>> data = 
				archiveRepository.getConceptMapArchiveByLibraryFiltered(library, filter,
				pageable, accountId, groupIds);
		return parseLibraryResult(data, pageable);
	}

	public ConceptMapArchive getConceptMapArchiveByName(String conceptMapName, String userId, String[] groupIds) {
		List<Map<String, Object>> results = archiveRepository.getConceptMapArchiveByName(conceptMapName, userId, groupIds);
		
		for (Map<String, Object> map : results) {
			for (String key : map.keySet()) {
				return (ConceptMapArchive) map.get(key);
			}
		}
		
		return null;
	}

	/**
	 * 
	 * @param library
	 * @param pageable
	 * @return
	 */
	public Page<ConceptMapArchive> getConceptMapArchiveByLibrary(Library library, Pageable pageable) {
		List<Map<String, Object>> data = archiveRepository.getConceptMapArchiveByLibrary(library, pageable);
		return parseLibraryResult(data, pageable);
	}

	/**
	 * This API will return count of ConceptMapArchive associated with Library.
	 * (On Click of Library in first ConceptSearchWindow)
	 * 
	 * @param library
	 * @param pageable
	 * @return
	 */
	public Long countConceptMapArchiveByLibrary(Library library, Pageable pageable) {
		return archiveRepository.countConceptMapArchiveByLibrary(library);
	}

	public Long countConceptMapArchiveByLibraryFiltered(Library library, String[] filter, Pageable pageable) {
		return archiveRepository.countConceptMapArchiveByLibraryFiltered(library, filter);
	}

	public ConceptMapArchive createInstance(Object... args) {
		return null;
	}

	@Override
	public List<ConceptMapArchive> getIdentifiers() {
		return null;
	}

	@Override
	public Page<ConceptMapArchive> getIdentifiers(Pageable pageable) {
		return null;
	}

	/**
	 *  This will return all ConceptMapArchive containing Concept having name matched as filter.
	 */
	@Override
	public Page<ConceptMapArchive> findByNameLike(List<String> filter, Pageable pageable) {

		List<ConceptMapArchive> result = new ArrayList<>();
		String[] words;
		switch (query.getLibraryMode()) {

		case BY_CONCEPT:
			Optional<IdentifiedConcept> optConcept = query.getCurrentSelectedConcept();
			if ( optConcept.isPresent() ) {
				IdentifiedConcept concept = (IdentifiedConcept) optConcept.get() ;
				Library library = concept.getLibrary();

				return getConceptMapArchiveByLibraryFiltered(
						library,
						filter,
						pageable,
						authState.getUserId(),
						authState.getGroupIds()
				);
			} else
				// Empty result?
				result = new ArrayList<ConceptMapArchive>();
			break;

		case BY_LIBRARY:
			String searchString = query.getCurrentQueryText();
			if (searchString != null) {
				searchString = searchString + SEPARATOR + filter;
			}
			words = searchString.split(SEPARATOR);

			List<Map<String, Object>> data = null;

			switch (searchMode) {
				case ALL_MAPS:
					data = archiveRepository.findConceptMapArchive(words, pageable, authState.getUserId(), authState.getGroupIds());
					for (Map<String, Object> m : data) {
						System.out.println(m);
					}
					break;
				case AUTHORED_MAPS:
					data = archiveRepository.findConceptMapArchivesByAuthorAccountId(words, pageable, authState.getUserId());
					break;
				case SHARED_MAPS:
					data = archiveRepository.findConceptMapArchivesByGroupId(words, pageable, authState.getGroupIds());
					break;
				default:
					throw new RuntimeException("SearchMode option " + searchMode.toString() + " is not implemented");
			}

			for (Map<String, Object> d : data) {
				ConceptMapArchive cm = (ConceptMapArchive) d.get("conceptMap");
				// if parents return add it
				if (d.get("parents") != null) {
					Library parent = (Library) d.get("parents");
					cm.setParents(parent);
				}
				result.add(cm);
			}
			break;

		case BY_PARENTS:
			Optional<Library> libraryOpt = query.getCurrentLibrary();
			if (libraryOpt.isPresent()) {
				Library library = libraryOpt.get();
				return getConceptMapArchiveByLibraryFiltered(
						library,
						filter,
						pageable,
						authState.getUserId(),
						authState.getGroupIds()
				);
			}
			break;

		default:
			throw new RuntimeException("ConceptMapArchiveService.findByNameLike() error: invalid query state?");

		}

		return new PageImpl<ConceptMapArchive>(result, pageable, result.size());
	}

	/**
	 * This will return all ConceptMapArchive.
	 */
	@Override
	public Page<ConceptMapArchive> findAll(Pageable pageable, String queryId) {
		List<ConceptMapArchive> result = new ArrayList<>();

		switch (query.getLibraryMode()) {

			case BY_CONCEPT:
				Optional<IdentifiedConcept> optConcept = query.getCurrentSelectedConcept();
				if ( optConcept.isPresent() ) {
					IdentifiedConcept concept = (IdentifiedConcept)optConcept.get() ;
					Library library = concept.getLibrary() ;
					return getConceptMapArchiveByLibraryFiltered(
							library,
							EMPTY_KEYWORDS,
							pageable,
							authState.getUserId(),
							authState.getGroupIds()
					);
				} else
					// Empty result?
					result = new ArrayList<ConceptMapArchive>();
				break;
	
			case BY_LIBRARY:
				String searchString = query.getCurrentQueryText();
				String[] words = searchString.split(SEPARATOR);
				List<Map<String, Object>> data;
				
				switch (searchMode) {
					case ALL_MAPS:
						data = archiveRepository.findConceptMapArchive(words, pageable, authState.getUserId(), authState.getGroupIds());
						for (Map<String, Object> m : data) {
							System.out.println(m);
						}
						break;
					case AUTHORED_MAPS:
						data = archiveRepository.findConceptMapArchivesByAuthorAccountId(words, pageable, authState.getUserId());
						break;
					case SHARED_MAPS:
						data = archiveRepository.findConceptMapArchivesByGroupId(words, pageable, authState.getGroupIds());
						break;
					default:
						throw new RuntimeException("SearchMode option " + searchMode.toString() + " is not implemented");
				}
				
				for (Map<String, Object> d : data) {
					ConceptMapArchive cm = (ConceptMapArchive) d.get("conceptMap");
					// if parents return add it
					if (d.get("parents") != null) {
						Library parent = (Library) d.get("parents");
						cm.setParents(parent);
					}
					result.add(cm);
				}
				break;
	
			case BY_PARENTS:
				Optional<Library> libraryOpt = query.getCurrentLibrary();
				if (libraryOpt.isPresent()) {
					Library library = libraryOpt.get();
					return getConceptMapArchiveByLibrary(library, pageable);
				}
				break;
	
			default:
				throw new RuntimeException("ConceptMapArchiveService.findAll() error: invalid query state?");
		}
		
		return new PageImpl<ConceptMapArchive>(result, pageable, result.size());
	}

	/**
	 * 
	 * This will return count of all ConceptMapArchive.
	 */
	@Override
	public long countEntries() {
		

		long count = 0L;

		switch (query.getLibraryMode()) {

			case BY_CONCEPT:
				Optional<IdentifiedConcept> optConcept = query.getCurrentSelectedConcept();
				if (optConcept.isPresent()) {
					IdentifiedConcept concept = optConcept.get();
					Library library = concept.getLibrary();
					String[] words = new String[0];
					count = archiveRepository.countConceptMapArchiveByLibraryFiltered(library, words);
				}
				break;
	
			case BY_LIBRARY:
				String searchString = query.getCurrentQueryText();
				String[] words = searchString.split(SEPARATOR);
				switch (searchMode) {
					case ALL_MAPS:
						count = archiveRepository.countAllConceptMapArchive(words, authState.getUserId());
						break;
					case AUTHORED_MAPS:
						count = archiveRepository.countAuthoredConceptMapArchive(words, authState.getUserId());
						break;
					case SHARED_MAPS:
						count = archiveRepository.countSharedConceptMapArchive(words, authState.getGroupIds());
						break;
					default:
					throw new RuntimeException("SearchMode option " + searchMode.toString() + " is not implemented");
				}
				break;
	
			case BY_PARENTS:
				Optional<Library> libraryOpt = query.getCurrentLibrary();
				if (libraryOpt.isPresent()) {
					Library library = libraryOpt.get();
					count = archiveRepository.countConceptMapArchiveByLibrary(library);
				}
				break;
	
			default:
				throw new RuntimeException("ConceptMapArchiveService.countEntries() error: invalid query state?");
		}

		return count;

	}

	/**
	 *  This will return count of ConceptMapArchive containing Concept having name matched as filter.
	 */
	@Override
	public long countHitsByNameLike(List<String> filter) {
		long count = 0L;
		String[] words;
		switch (query.getLibraryMode()) {

		case BY_CONCEPT:
			Optional<IdentifiedConcept> optConcept = query.getCurrentSelectedConcept();
			if (optConcept.isPresent()) {
				IdentifiedConcept concept = optConcept.get();
				Library library = concept.getLibrary();

				count = archiveRepository.
							countConceptMapArchiveByLibraryFiltered(
									library, filter.toArray(new String[0])
							);
			}
			break;

		case BY_LIBRARY:
			String searchString = query.getCurrentQueryText();
			if (searchString != null) {
				searchString = searchString + SEPARATOR + filter;
			}
			words = searchString.split(SEPARATOR);
			switch (searchMode) {
				case ALL_MAPS:
					count = archiveRepository.countAllConceptMapArchive(words, authState.getUserId());
					break;
				case AUTHORED_MAPS:
					count = archiveRepository.countAuthoredConceptMapArchive(words, authState.getUserId());
					break;
				case SHARED_MAPS:
					count = archiveRepository.countSharedConceptMapArchive(words, authState.getGroupIds());
					break;
				default:
					throw new RuntimeException("SearchMode option " + searchMode.toString() + " is not implemented");
			}
			break;

		case BY_PARENTS:
			Optional<Library> libraryOpt = query.getCurrentLibrary();
			if (libraryOpt.isPresent()) {
				Library library = libraryOpt.get();
				count = 
					archiveRepository.
							countConceptMapArchiveByLibraryFiltered(
									library, filter.toArray(new String[0]));
			}
			break;

		default:
			throw new RuntimeException("ConceptMapArchiveService.countHitsByNameLike() error: invalid query state?");
		}
		return count;
	}
}
