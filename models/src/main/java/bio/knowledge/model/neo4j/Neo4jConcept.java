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
package bio.knowledge.model.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.Library;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.core.neo4j.Neo4jAnnotatedEntity;

/**
 * @author Richard Bruskiewich
 * 
 * Concept is a fundamental currency in Knowledge.Bio, 
 * representing the unit of semantic representation of globally distinct ideas.
 * 
 */
@NodeEntity(label="Concept")
public class Neo4jConcept extends Neo4jAnnotatedEntity {

	public static final String SEMGROUP_FIELD_START = "[" ;
	public static final String SEMGROUP_FIELD_END   = "]" ;
	
    private SemanticGroup semanticGroup;

    // Counter for the number of times that this 
    // Concept SemanticGroup is used in Statements.
    // This helps the code filter out unproductive SemMedDb concepts
    // from being listed in the "Concept by Text" search results.
    private Long usage = 0L ;

    // The Library class is an indirect wrapper class for
    // the set of associated ConceptMap's related to the included Concept nodes
    @Relationship( type="LIBRARY" )
    private Library library = new Library();
    
    // Cross-reference to the 
    // Genetic Home References identifier 
    // for genes and disorders
    private String ghr ;
    
	// Human Metabolome Database identifier
	// i.e. to use to access record at
	// http://www.hmdb.ca/metabolites/HMDB06408
	private String hmdbId ; 
	
    // Cross-reference to the 
    // Chemical Entities of Biological Interest (ChEBI)
    private String chebi ;
    
    private Set<String> dbLinks = new HashSet<String>() ;
    
    private Set<String> terms = new HashSet<String>() ;
    
    protected Neo4jConcept() {
    	super() ;
    }
    
    public Neo4jConcept( SemanticGroup semgroup, String name ) {
    	super(name) ;
    	this.semanticGroup = semgroup ;
    }

    public Neo4jConcept( String accessionId, SemanticGroup semgroup, String name ) {
    	super(accessionId,name,"") ;
    	this.semanticGroup = semgroup ;
    }

	/**
     * 
     * @param the Concept Semantic Group 
     * Should generally be set at node creation, but sometimes not?
     */
    public void setSemanticGroup(SemanticGroup semgroup) {
    	this.semanticGroup = semgroup ;
    }
    
	/**
     * 
     * @return the Explicit Concept SemanticGroup 
     */
    public SemanticGroup getSemanticGroup() {
    	if(semanticGroup==null) {
    		return SemanticGroup.OBJC;
    	}
    	return semanticGroup ;
    }

	/**
	 * @return the usage of the Concept in Statements
	 */
	public Long getUsage() {
		return usage;
	}

	/**
	 * @param usage to set counting the number of Concept used in Statements
	 */
	public void setUsage(Long usage) {
		this.usage = usage;
	}
	
	/**
	 * @param increment to add to count of the number of Concept used in Statements
	 */
	public void incrementUsage(Long increment) {
		this.usage += increment;
	}
	
	/**
	 * @param increment by one the count of the number of Concept used in Statements
	 */
	public void incrementUsage() {
		this.usage += 1;
	}
	
	/**
	 * 
	 * @param library of archived ConceptMaps associated with the Concept
	 */
	public void setLibrary( Library library ) {
		this.library = library;
	}

	/**
	 * @return Library of archived ConceptMaps associated with the Concept
	 */
	public Library getLibrary() {
		return library;
	}
	
    /**
	 * @return the Genetic Home References identifier (for genes and disorders)
	 */
	public String getGhr() {
		return ghr;
	}

	/**
	 * @param ghr the Genetic Home References identifier to set
	 */
	public void setGhr(String ghr) {
		this.ghr = ghr;
	}
	
	/**
	 * 
	 * @return the Human Metabolome DataBase identifier (as a string)
	 */
	public String getHmdbId() {
		return hmdbId ;
	}
	
	/**
	 * @param hmdbId the Human Metabolome DataBase identifier (as a string) to set
	 */
	public void setHmdbId(String hmdbId) {
		this.hmdbId = hmdbId;
	}	

	/**
	 * @return any associated identifier for Chemical Entities of Biological Interest (ChEBI)
	 */
	public String getChebi() {
		return chebi;
	}

	/**
	 * @param chebi associated identifier for Chemical Entities of Biological Interest (ChEBI)
	 */
	public void setChebi(String chebi) {
		this.chebi = chebi;
	}

	/**
     * 
     * @return
     */
    public Set<String> getCrossReferences() {
    	return dbLinks ;
    }
    
	/**
     * 
     * @return
     */
    public Set<String> getTerms() {
    	return terms ;
    }
    
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
    	return getName() ;
    }

}
