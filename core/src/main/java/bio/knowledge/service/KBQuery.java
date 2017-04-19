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

import java.util.Optional;
import java.util.Set;

import bio.knowledge.model.Annotation;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.Library;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.Statement;
import bio.knowledge.model.neo4j.Neo4jConcept;

/**
 * @author Richard
 * @author Chandan Mishra (cmishra@sfu.ca)
 */
public interface KBQuery {
	
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
	 * @param identifier of the user's current query ConceptSemanticType
	 */
	public void setCurrentQueryConceptById(String identifier) ;
	
	/**
	 * 
	 * @return user's current query ConceptSemanticType
	 */
	public Optional<Neo4jConcept> getCurrentQueryConcept() ;

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
	 * @param query user's current query ConceptSemanticType
	 */
	public void setCurrentSelectedConcept(Neo4jConcept query) ;
	
	/**
	 * 
	 * @return user's current query ConceptSemanticType
	 */
	public Optional<Neo4jConcept> getCurrentSelectedConcept() ;

	/**
	 * 
	 * @param Statement currently displayed

	 */
	public void setCurrentStatement( Statement statement ) ;
	
	/**
	 * 
	 * @return optional of Statement currently for display
	 */
	public Optional< Statement > getCurrentStatement() ;
	
	/**
	 * 
	 * @param Evidence currently displayed
	 */
	public void setCurrentEvidence( Evidence evidence ) ;
	
	/**
	 * 
	 * @return optional of Evidence currently for display
	 */
	public Optional< Evidence > getCurrentEvidence() ;

	/**
	 * 
	 * @param Annotation currently selected
	 */
	public void setCurrentAnnotation( Annotation annotation ) ;
	
	/**
	 * 
	 * @return optional of Annotation currently selected
	 */
	public Optional< Annotation > getCurrentAnnotation() ;
	
	/**
	 * 
	 * @param typeSet specification on initial search result Set of Concept Semantic Type query constraints 
	 */
	public void setInitialConceptTypes( Set<SemanticGroup> typeSet ) ;
	
	/**
	 * 
	 * @return optional of initial search result Set Concept Semantic Types query constraints
	 */
	public Optional< Set<SemanticGroup> > getInitialConceptTypes() ;
	
	/**
	 * 
	 * @param typeSet specification of current Set of Concept Semantic Type query constraints in Relations Data Table
	 */
	public void setConceptTypes( Set<SemanticGroup> typeSet ) ;
	
	/**
	 * 
	 * @return optional of current Concept Semantic Types query constraints in Relations Data Table
	 */
	public Optional< Set<SemanticGroup> > getConceptTypes() ;
	
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
	 * @param the filter type (e.g. drugs, dieases, etc.)
	 */
	public void setFilterType(String type);
	
	/**
	 * Get the filter type that filters the relations table
	 */
	public String getFilterType();
	
	/**
	 * Reset the filter type to the default empty value
	 */
	public void resetFilterType();
	
	/**
	 * Set the item id(s) to be used for the tree in the semantic popup window
	 */
	public void setOtherFilterValue(Object value);
	
	/**
	 * Get the item id(s) used for the tree in the semantic popup window
	 */
	public Object getOtherFilterValue();
	
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
	
	public void setLastSelectedConcept(Neo4jConcept concept);
	
	public Neo4jConcept getLastSelectedConcept();
		
	/* Passing out coordinates of popups when need be */
	public int tempCoordX();
	public int tempCoordY();
	public void tempCoordX(int x);
	public void tempCoordY(int y);
	
	// we use string instead of the userprofile object since the Userprofile object is defined in a package whose dependencies are downstream from core.
	public String currentUserId() ;
	public void currentUserId(String userId);
	
}
