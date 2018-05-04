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

import bio.knowledge.model.Annotation;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.ConceptType;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.Library;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.Statement;

/**
 * @author Richard
 * @author Chandan Mishra (cmishra@sfu.ca)
 */
public interface KBQuery {
	
	/* 
	 * For User authentication, we use string instead of the userprofile object
	 *  since the Userprofile object is defined in a package whose dependencies 
	 *  are downstream from core.
	 */
	
	/**
	 * 
	 * @return String user identifier 
	 */
	public String currentUserId() ;
	
	/**
	 * 
	 * @param userId user identifier
	 */
	public void currentUserId(String userId);

	/**
	 * 
	 * Generates a unique String session identifier user and activates it in the current session.
	 */
	@Deprecated
	public void generateUserSessionId();

	/**
	 * 
	 * @return String session identifier
	 */
	@Deprecated
	public String getUserSessionId();

	/**
	 * 
	 */
	@Deprecated
	public void clearUserSessionId();
	

	/**
	 * 
	 * @return true if a session identifier is set; false otherwise
	 */
	@Deprecated
	public boolean hasSessionId();

	/**
	 * 
	 * @param customBeacons
	 */
	void setCustomBeacons(List<Integer> customBeacons);

	/**
	 * 
	 * @return
	 */
	List<Integer> getCustomBeacons();

	/**
	 * Clears the list of custom beacons
	 */
	void clearCustomBeacons();
	
	/**
	 * Clears the list of custom beacons
	 */
	int countCustomBeacons();
	
	/**
	 * 
	 * @param query user's current query text
	 */
	public void setCurrentQueryText(String query) ;
	
	/**
	 * 
	 * @return user's current query text
	 */
	public String getCurrentQueryText() ;
	
	/**
	 * 
	 * @return current query id
	 */
	public String getCurrentQueryId();
	
	/**
	 * Sets the current query id. This can change in the middle of a TKBio session by a user who browses different queries they've created
	 * @return
	 */
	public void setCurrentQueryId(String queryId);
	
	/**
	 * @param matchById is true if matching a CURIE; false if simple keyword search
	 */
	public void setMatchingMode(Boolean matchByID);
	
	/**
	 * 
	 * @return true if directly querying by CURIE identifier
	 */
	public Boolean matchByIdentifier() ;
	
	/**
	 * @param identifier of the user's current query Concept
	 */
	public void setCurrentQueryConceptById(String identifier) ;
	
	/**
	 * 
	 * @return user's current query Concept
	 */
	public Optional<IdentifiedConcept> getCurrentQueryConcept() ;

	public enum LibrarySearchMode {
		NONE, BY_CONCEPT, BY_LIBRARY, BY_PARENTS, HIDDEN ;
	}
	
	/**
	 * Method to specify mode of library search
	 * @param mode LibrarySearchMode
	 */
	public void setLibraryMode(LibrarySearchMode mode);
	
	/**
	 * Method to get mode of library search
	 * @return LibrarySearchMode 
	 */
	public LibrarySearchMode getLibraryMode();

	/**
	 * @param library to be specifically retrieved and displayed
	 */
	public void setCurrentLibrary( Library library );
	
	/**
	 * @return library to be specifically retrieved and displayed
	 */
	public Optional<Library> getCurrentLibrary();

	/**
	 * @param library of ConceptMapArchives currently loaded by user
	 * 
	 */
	public void setCurrentImportedMaps( Library library );
	
	/**
	 * @param map currently loaded by user
	 * 
	 */
	public void addImportedMap( ConceptMapArchive map );
	
	/**
	 * @return map currently loaded by user
	 */
	public Optional<Library> getCurrentImportedMaps();
	
	/**
	 * 
	 * @param query user's current query Concept
	 */
	public void setCurrentSelectedConcept(IdentifiedConcept query) ;
	
	/**
	 * 
	 * @return user's current query Concept
	 */
	public Optional<IdentifiedConcept> getCurrentSelectedConcept() ;

	/**
	 * 
	 * @param BeaconStatement currently displayed

	 */
	public void setCurrentStatement(Statement selectedStatement) ;
	
	/**
	 * 
	 * @return optional of Statement currently for display
	 */
	public Optional<Statement> getCurrentStatement() ;
	
	/**
	 * 
	 * @param Evidence currently displayed
	 */
	public void setCurrentEvidence( Evidence evidence ) ;
	
	/**
	 * 
	 * @return optional of Evidence currently for display
	 */
	public Optional<Evidence> getCurrentEvidence() ;

	/**
	 * 
	 * @param BeaconAnnotation currently selected
	 */
	public void setCurrentAnnotation( Annotation annotation ) ;
	
	/**
	 * 
	 * @return optional of Annotation currently selected
	 */
	public Optional<Annotation> getCurrentAnnotation() ;
	
	/**
	 * 
	 * @param typeSet specification on initial search result Set of Concept query constraints 
	 */
	public void setInitialConceptTypes( Set<ConceptType> typeSet ) ;
	
	/**
	 * 
	 * @return optional of initial search result Set Concept query constraints
	 */
	public Set<ConceptType> getInitialConceptTypes() ;
	
	/**
	 * 
	 * @param types specification of current Set of Concept query constraints in Relations Data Table
	 */
	public void setSelectedConceptTypes( Set<ConceptType> types ) ;
	
	/**
	 * 
	 * @return optional of current Concept query constraints in Relations Data Table
	 */
	public Set<ConceptType> getSelectedConceptTypes() ;
	
	/**
	 * 
	 * @param query user's current Pubmed Identifier
	 */
	public void setCurrentPmid(String string) ;
	
	/**
	 * 
	 * @return user's current Pubmed Identifier, if available
	 */
	public Optional<String> getCurrentPmid() ;
	
	/**
	 * RelationSearchMode differs depending on whether the
	 * source query is a RELATIONS concept, PMID or WIKIDATA_PROPERTY.
	 */
	public enum RelationSearchMode {
		RELATIONS, PMID, WIKIDATA, HIDDEN ;
	}
	
	/**
	 * Method to set the mode of "Relations" data table search 
	 * @param mode of Relation data table search
	 */
	public void setRelationSearchMode(RelationSearchMode mode);
	
	/**
	 * Method to identify mode of "Relations" data table search 
	 * @return RelationSearchMode in effect for the Relations table 
	 */
	public RelationSearchMode getRelationSearchMode();
	
	/**
	 * Method to clear any cached queries
	 */
	public void resetQuery() ;

	/**
	 * Set the filter type to filter the relations table
	 * @param the filter type (e.g. drugs, diseases, etc.)
	 */
	public void setSemGroupFilterType(String type);
	
	/**
	 * Get the filter type that filters the relations table
	 */
	public String getSemGroupFilterType();
	
	/**
	 * Reset the filter type to the default empty value
	 */
	public void resetSemGroupFilterType();
	
	/**
	 * Set the item id(s) to be used for the tree in the semantic popup window
	 */
	public void setOtherSemGroupFilterValue(Object value);
	
	/**
	 * Get the item id(s) used for the tree in the semantic popup window
	 */
	public Object getOtherSemGroupFilterValue();
	
	/**
	 * 
	 * @param value
	 */
	public void setPredicateFilterValue(Set<Predicate> predicates);
	
	/**
	 * 
	 * @return
	 */
	public Optional<Set<Predicate>> getPredicateFilterValue() ;
	
	/**
	 * 
	 */
	public void resetPredicateFilterValue() ;
	
	/**
	 * This will return id of all ConceptMap's node. (Used in ConceptMapArchiveService)
	 * @return
	 */
	public Set<String> getNodeIdsfromConceptMap();
	/**
	 * 	This will add node id to set. (Used in DesktopUI)
	 * @param cstId
	 */
	public void addNodeIdToSet(String id);
	
	/**
	 *  This will clear all node's id.
	 */
	public void clearNodeIdsFromConceptMap();

	
	public enum ConceptSearchMode {
		DEFAULT, ANNOTATION, HIDDEN ;
	}
	
	/**
	 * Method to set the mode of "Concept" data table search 
	 * @param mode of Relation data table search
	 */
	public void setConceptSearchMode(ConceptSearchMode mode);
	
	/**
	 * Method to identify mode of "Concept" data table search 
	 * @return RelationSearchMode in effect for the Relations table 
	 */
	public ConceptSearchMode getConceptSearchMode();
	
	public void setLastSelectedConcept(IdentifiedConcept concept);
	
	public IdentifiedConcept getLastSelectedConcept();
		
	/* Passing out coordinates of popups when need be */
	public int tempCoordX();
	public int tempCoordY();
	public void tempCoordX(int x);
	public void tempCoordY(int y);

	public String currentConceptId=null;

	public String getCurrentConceptId();

	public void setSimpleTextFilter(String filterText);
	public String getSimpleTextFilter();
	
}
