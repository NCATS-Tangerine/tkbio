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
package bio.knowledge.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;

/**
 * @author Richard
 * 
 */
@NodeEntity(label="Library")
public class Library extends Neo4jAbstractIdentifiedEntity {
	
	private Integer numberOfArchivedMaps = 0;

	@Relationship( type="ASSOCIATED_MAP" )
    private Set<ConceptMapArchive> associatedMaps = new HashSet<ConceptMapArchive>() ;

	/**
	 * This is modified every time a user queries the database for a library. We
	 * don't want to display the {@code numberOfArchivedMaps} since this
	 * includes private maps that may not be visible to that user.
	 * {@code numberOfVisibleMaps} should be assumed to be constantly changing
	 * in the database, and not used for anything other than displaying the
	 * number of concept maps associated with this library visible to the user.
	 */
	@Property(name = "numberOfVisibleMaps")
	private Integer numberOfVisibleMaps;
    
    public Library() {}

	/**
	 * 
	 * @param associatedMaps to be associated with the Evidence
	 */
	public void setConceptMaps( Set<ConceptMapArchive> maps ) {
		associatedMaps = maps ;
		numberOfArchivedMaps = new Integer( associatedMaps.size() ) ;
	}
	
	/**
	 * 
	 * @param associatedMap to be associated with a ConceptSemanticType
	 */
	public void addConceptMap( ConceptMapArchive associatedMap ) {
		if( associatedMaps==null)
			associatedMaps = new HashSet<ConceptMapArchive>() ;
		associatedMaps.add(associatedMap);
		++numberOfArchivedMaps ;
	}
	
	/**
	 * @return the Sentence-to-Predication mappings
	 */
	public Set<ConceptMapArchive> getConceptMaps() {
		return associatedMaps;
	}
	
	/**
	 * 
	 * @return true if Library is empty
	 */
	public boolean isEmpty() {
		if( numberOfArchivedMaps==null || numberOfArchivedMaps==0 )
			return true ;
		else
			return false ;
	}
	
    /**
	 * @return the numberOfArchivedMaps
	 */
	public Integer getNumberOfArchivedMaps() {
		if(numberOfArchivedMaps==null)
			numberOfArchivedMaps = new Integer( associatedMaps.size() ) ;
		return numberOfArchivedMaps;
	}

	/**
	 * @param numberOfArchivedMaps to set
	 */
	public void setNumberOfArchivedMaps(Integer numberOfArchivedMaps) {
		this.numberOfArchivedMaps = numberOfArchivedMaps;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
	 */
	@Override
	public String toString() {
		/*
		 * We don't want to use numberOfArchivedMaps, since that includes
		 * private maps that may not be visible to the current user. Instead we
		 * display numberOfVisibleMaps, which is set each time we call
		 * ConceptRepository.findByNameLikeIgnoreCase()
		 */

		if (numberOfVisibleMaps != null) {
			return Integer.toString(numberOfVisibleMaps);
		} else {
			return "0";
		}
	}
	
	// for lexicographical sorting by count purpose from UI
	@Override
	public int compareTo(IdentifiedEntity other) {
		return numberOfArchivedMaps.compareTo(((Library)other).numberOfArchivedMaps);
	}
    
}
