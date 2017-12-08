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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import bio.knowledge.model.Annotation;
import bio.knowledge.model.Concept;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.Library;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.Statement;

/**
 * @author Richard
 * @author Chandan Mishra (cmishra@sfu.ca)
 *
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KBQueryImpl implements KBQuery {

	private String userId = "";
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#currentUserId()
	 */
	@Override
	public String currentUserId() {
		return this.userId;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#currentUserId(java.lang.String)
	 */
	@Override
	public void currentUserId(String userId) {
		this.userId = userId;
	}

	private String sessionId = "";
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setUserSessionId(java.lang.String)
	 */
	@Override
	public void setUserSessionId() {
		this.sessionId = RandomStringUtils.randomAlphanumeric(20);
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getUserSessionId()
	 */
	@Override
	public String getUserSessionId() {
		return sessionId;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#clearUserSessionId()
	 */
	@Override
	public void clearUserSessionId() {
		sessionId = "";
	}
	

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#hasSessionId()
	 */
	@Override
	public boolean hasSessionId() {
		return !sessionId.isEmpty();
	}

	private List<String> customBeacons = new ArrayList<String>();
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCustomBeacons(java.util.List)
	 */
	@Override
	public void setCustomBeacons(List<String> customBeacons){ 
		this.customBeacons = customBeacons;
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCustomBeacons()
	 */
	@Override
	public List<String>  getCustomBeacons(){ 
		return customBeacons;
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#clearCustomBeacons()
	 */
	@Override
	public void clearCustomBeacons() {
		customBeacons = null;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#countCustomBeacons()
	 */
	@Override
	public int countCustomBeacons() {
		return customBeacons.size();
	}

	
	private String currentQueryText = "" ; 

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentQueryText(java.lang.String)
	 */
	@Override
	public void setCurrentQueryText(String query) {
		this.currentQueryText = query ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentQueryText(java.lang.String)
	 */
	@Override	
	public String getCurrentQueryText() {
		return currentQueryText ;
	}

	private Boolean matchingByIdentifier = false;
	
	@Override
	public void setMatchingMode(Boolean matchByID) {
		matchingByIdentifier = matchByID;
	}

	@Override
	public Boolean matchByIdentifier() {
		return matchingByIdentifier;
	}


	
	@Autowired
	private ConceptService conceptService ;

	private Optional<Concept> queryConcept = Optional.empty() ;

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentQueryConcept(bio.knowledge.model.Concept)
	 */
	@Override
	public void setCurrentQueryConceptById(String identifier) {
		resetQuery();
		this.currentQueryConceptId = identifier;
	}
	private String currentQueryConceptId = null;
	@Override public String getCurrentQueryConceptId() {
		return this.currentQueryConceptId;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCurrentQueryConcept()
	 */
	@Override
	public Optional<Concept> getCurrentQueryConcept() {
		return queryConcept;
	}
	
	/*
	 * flag for tracking type of Library search
	 */
	private LibrarySearchMode librarySearchMode = LibrarySearchMode.NONE ;

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setLibraryMode(bio.knowledge.service.KBQuery.LibrarySearchMode)
	 */
	@Override
	public void setLibraryMode( LibrarySearchMode mode ) {
		librarySearchMode = mode ;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getLibraryMode()
	 */
	@Override
	public LibrarySearchMode getLibraryMode() {
		return librarySearchMode;
	}

	private Optional<Library> currentLibrary = Optional.empty() ;
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentLibrary(bio.knowledge.model.Library)
	 */
	@Override
	public void setCurrentLibrary( Library library ) {
		if( library==null )
			currentLibrary = Optional.empty() ;
		else {
			currentLibrary = Optional.of(library) ;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCurrentLibrary()
	 */
	@Override	
	public Optional<Library> getCurrentLibrary() {
		return currentLibrary ;
	}

	private Optional<Library> currentImportedMaps = Optional.empty() ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentImportedMap(bio.knowledge.model.ConceptMapArchive)
	 */
	@Override
	public void setCurrentImportedMaps( Library library ) {
		if( library==null )
			currentImportedMaps = Optional.empty() ;
		else {
			currentImportedMaps = Optional.of(library) ;
		}
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#addImportedMap(bio.knowledge.model.ConceptMapArchive)
	 */
	@Override
	public void addImportedMap(ConceptMapArchive map) {
		Library library ;
		if(!currentImportedMaps.isPresent())
			library = new Library() ;
		else
			library = currentImportedMaps.get() ;
		
		library.addConceptMap(map); 
		setCurrentImportedMaps( library ) ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCurrentImportedMap()
	 */
	@Override
	public Optional<Library> getCurrentImportedMaps() {
		return currentImportedMaps;
	}

	private Optional<Concept> selectedConcept = Optional.empty() ;

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentQueryConcept(bio.knowledge.model.Concept)
	 */
	@Override
	public void setCurrentSelectedConcept(Concept query) {
		if(query==null)
			selectedConcept = Optional.empty() ;
		else {
			// the concept is pulled in by clique
			String identifier = query.getClique();
			selectedConcept = conceptService.getDetailsByCliqueId(identifier) ;
		}
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCurrentQueryConcept()
	 */
	@Override
	public Optional<Concept> getCurrentSelectedConcept() {
		return selectedConcept;
	}

	private Optional<Statement> currentStatement = Optional.empty() ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentStatement(bio.knowledge.model.semmeddb.Statement)
	 */
	@Override
	public void setCurrentStatement(Statement statement) {
		Evidence evidence = statement.getEvidence();
		this.currentEvidence  = Optional.ofNullable(evidence) ;
		this.currentStatement = Optional.ofNullable(statement);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCurrentStatement()
	 */
	@Override
	public Optional<Statement> getCurrentStatement() {
		return currentStatement;
	}

	private Optional< Evidence > currentEvidence = Optional.empty() ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentEvidence(java.util.Set)
	 */
	@Override
	public void setCurrentEvidence( Evidence evidence ) {
		currentEvidence = Optional.of(evidence) ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCurrentEvidence()
	 */
	@Override
	public Optional< Evidence > getCurrentEvidence() {
		return currentEvidence;
	}

	private Optional<Annotation> currentAnnotation = Optional.empty() ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setCurrentAnnotation(bio.knowledge.model.semmeddb.Annotation)
	 */
	@Override
	public void setCurrentAnnotation(Annotation annotation) {
		this.currentAnnotation = Optional.of(annotation) ; 
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getCurrentAnnotation()
	 */
	@Override
	public Optional<Annotation> getCurrentAnnotation() {
		return currentAnnotation;
	}
	
	private Optional< Set<SemanticGroup> > selectedInitialConceptTypes = Optional.empty() ;
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getInitialConceptTypes()
	 */
	@Override
	public Optional<Set<SemanticGroup>> getInitialConceptTypes() {
		return selectedInitialConceptTypes;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setInitialConceptTypes(java.util.Set)
	 */
	@Override
	public void setInitialConceptTypes( Set<SemanticGroup> typeSet ) {
		this.selectedInitialConceptTypes = Optional.of(typeSet);
	}

	private Optional< Set<SemanticGroup> > selectedConceptTypes = Optional.empty() ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getConceptTypes()
	 */
	@Override
	public Optional<Set<SemanticGroup>> getConceptTypes() {
		return selectedConceptTypes;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setConceptTypes(java.util.Set)
	 */
	@Override
	public void setConceptTypes( Set<SemanticGroup> typeSet ) {
		this.selectedConceptTypes = Optional.of(typeSet);
	}


	/**
	 *  This is for retrieval of predication based on PMID.
	 */

	private Optional<String> pmid = Optional.empty() ;
	
	@Override
	public void setCurrentPmid(String pmid) {
		//this.resetQuery();
		//currentEvidence = Optional.empty(); //not sure why this should be emptied here?
		this.pmid = Optional.of(pmid);
		this.relationSearchMode = RelationSearchMode.PMID ;
	}

	@Override
	public Optional<String> getCurrentPmid() {
		return pmid;
	}
	
	private RelationSearchMode relationSearchMode = RelationSearchMode.RELATIONS ;
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#setRelationSearchMode(bio.knowledge.service.KBQuery.RelationSearchMode)
	 */
	@Override
	public void setRelationSearchMode(RelationSearchMode mode) {
		relationSearchMode = mode ;
	}

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#getRelationSearchMode()
	 */
	@Override
	public RelationSearchMode getRelationSearchMode() {
		return relationSearchMode ;
	}
	
	// the type used to filter relations table (e.g. drugs, diseases, etc.)
	private String type = "";
	
	@Override
	public void setSemGroupFilterType(String type) {
		this.type = type;
	}
	
	@Override
	public String getSemGroupFilterType() {
		return this.type;
	}
	
	@Override
	public void resetSemGroupFilterType() {
		setSemGroupFilterType("");
	}
	
	private Object value = null;
	
	@Override
	public void setOtherSemGroupFilterValue(Object value) {
		this.value = value;
	}
	
	@Override
	public Object getOtherSemGroupFilterValue() {
		 return value;
	}
	
	// resets the the item id(s) to be used for the tree in the semantic popup window
	private void resetOtherFilterValue() {
		value = null;
	}
	
	private Optional<Set<Predicate>> predicateFilterValue = Optional.empty();

	@Override
	public void setPredicateFilterValue(Set<Predicate> predicates) {
		predicateFilterValue = Optional.of(predicates);
	}

	@Override
	public Optional<Set<Predicate>> getPredicateFilterValue() {
		return predicateFilterValue;
	}

	@Override
	public void resetPredicateFilterValue() {
		predicateFilterValue = Optional.empty();
	}
	
	private Set<String> nodeIds = new HashSet<>();

	@Override
	public Set<String> getNodeIdsfromConceptMap() {
		return nodeIds;
	}

	@Override
	public void addNodeIdToSet(String cstId) {
		nodeIds.add(cstId);
	}
	
	public void clearNodeIdsFromConceptMap(){
		nodeIds.clear();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.KBQuery#resetQuery()
	 */
	@Override
	public void resetQuery() {
		queryConcept = Optional.empty() ;
		currentStatement = Optional.empty() ;
		currentEvidence = Optional.empty() ;
		selectedConceptTypes = Optional.empty() ;
		pmid = Optional.empty();
		resetSemGroupFilterType();
		resetOtherFilterValue();
		resetPredicateFilterValue();
		clearNodeIdsFromConceptMap();
	}

	private ConceptSearchMode conceptSearchMode = ConceptSearchMode.DEFAULT ;

	private Concept lastSelectedConcept;
	
	@Override
	public void setConceptSearchMode(ConceptSearchMode mode) {
		this.conceptSearchMode = mode;
	}

	@Override
	public ConceptSearchMode getConceptSearchMode() {
		return this.conceptSearchMode;
	}

	@Override
	public void setLastSelectedConcept(Concept concept) {
		this.lastSelectedConcept = concept;
	}

	@Override
	public Concept getLastSelectedConcept() {
		return lastSelectedConcept;
	}

	public int tempX;
	public int tempY;

	@Override
	public int tempCoordX() {
		return tempX;
	}

	@Override
	public int tempCoordY() {
		return tempY;
	}
	
	@Override
	public void tempCoordX(int x) {
		this.tempX = x;
	}

	@Override
	public void tempCoordY(int y) {
		this.tempY = y;
	}

	private String relationsTextFilter;
	@Override
	public void setSimpleTextFilter(String filterText) {
		this.relationsTextFilter = filterText;
	}

	@Override
	public String getSimpleTextFilter() {
		return this.relationsTextFilter;
	}

}
