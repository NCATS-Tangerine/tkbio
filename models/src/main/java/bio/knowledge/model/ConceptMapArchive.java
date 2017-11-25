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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;

/**
 * @author Richard
 * 
 * Represents a single instance of a named User Archived Concept Map
 * 
 * TODO: Initial implementation: store the KB2 concept map cytoscape.js 'kb' JSON record as a String blob here?
 *
 */
@NodeEntity(label="ConceptMap")
public class ConceptMapArchive extends Neo4jAbstractIdentifiedEntity {
	
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/*
	 * ConceptMaps that may have been imported by the user
	 * should serve as the starting point for this concept map version
	 */
	@Relationship( type="PARENTS" )
    private Library parents = new Library();

	@Property(name="comments")
	private String comments;
	
	@Property(name="dateCreated")
	private String dateCreated;
	
	@Property(name="dateLastModified")
	private String dateLastModified;
	
	@Property(name="authorsAccountId")
	private String authorsAccountId;
	
	@Property(name="groupId")
	private String groupId;
	
	@Property(name="isPublic")
	private boolean isPublic;
	
	public void setGroupId(String id) {
		this.groupId = id;
	}
	
	public String getGroupId() {
		return this.groupId;
	}
	
	public void setAuthorsAccountId(String id) {
		this.authorsAccountId = id;
	}
	
	public String getAuthorsAccountId() {
		return this.authorsAccountId;
	}
	
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public boolean isPublic() {
		return this.isPublic;
	}

	public String getDateCreated() {
		return this.dateCreated;
	}

	public String getDateLastModified() {
		return this.dateLastModified;
	}

	public void setDateLastModifiedToNow() {
		this.dateLastModified = dateFormat.format(new Date());
	}

	public String getComments() {
		return this.comments != null ? this.comments : "";
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	/*
	 * This attribute holds the 'kb' formatted Concept Map from cytoscape.js
	 */
	private String conceptMapJson = "";
	
	private String conceptMapPng = "";
	
	private String conceptMapSif = "";
	
	private String conceptMapTsv = "";
	
	public ConceptMapArchive() {  }

	public ConceptMapArchive(String name, String description) {
		super(name,description) ;
		this.dateCreated = dateFormat.format(new Date());
	}

	public ConceptMapArchive(String name) {
		super(name) ;
		this.dateCreated = dateFormat.format(new Date());
	}

	/**
	 * @return the parentMap
	 */
	public Library getParents() {
		return parents;
	}

	/**
	 * @param parentMap the parentMap to set
	 */
	public void setParents(Library parents) {
		this.parents = parents;
	}

	/**
	 * @return the conceptMapJson
	 */
	public String getConceptMapJson() {
		return conceptMapJson;
	}
	
	public String getConceptMapPng() {
		return conceptMapPng;
	}
	
	public String getConceptMapSif() {
		return this.conceptMapSif;
	}
	
	public String getConceptMapTsv() {
		return this.conceptMapTsv;
	}

	/**
	 * @param conceptMapJson the conceptMapJson to set
	 */
	public void setConceptMapJson(String conceptMapJson) {
		this.conceptMapJson = conceptMapJson;
	}
	
	public void setConceptMapPng(String conceptMapPng) {
		this.conceptMapPng = conceptMapPng;
	}
	
	public void setConceptMapSif(String conceptMapSif) {
		this.conceptMapSif = conceptMapSif;
	}
	
	public void setConceptMapTsv(String conceptMapTsv) {
		this.conceptMapTsv = conceptMapTsv;
	}
	
}
