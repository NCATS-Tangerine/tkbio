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
package bio.knowledge.model.core.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.ExternalDatabase;
import bio.knowledge.model.core.Ontology;

@NodeEntity(label="ExternalDatabase")
public class Neo4jAbstractExternalDatabase 
	extends Neo4jAbstractIdentifiedEntity implements ExternalDatabase {

	public Neo4jAbstractExternalDatabase() {
        super();
    }

    public Neo4jAbstractExternalDatabase( String name ) {
		super(name);
	}

    public Neo4jAbstractExternalDatabase( String name, String description ) {
		super(name,description);
	}
    
    public Neo4jAbstractExternalDatabase( String name, String description, String url ) {
		super(name,description);
		this.url = url ;
	}

    /**
     */
    private String url = "*";

    /**
     */
    private String nameSpacePrefix = "";

    /* (non-Javadoc)
	 * @see bio.knowledge.model.core.ExternalDatabase#toString()
	 */
    @Override
	public String toString() { return this.getName() ; }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.ExternalDatabase#getUrl()
	 */
	@Override
	public String getUrl() {
        return this.url;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.ExternalDatabase#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url) {
        this.url = url;
    }
	

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#getNameSpacePrefix()
	 */
	@Override
	public String getNameSpacePrefix() {
		return nameSpacePrefix;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.Ontology#setNameSpacePrefix(java.lang.String)
	 */
	@Override
	public void setNameSpacePrefix(String prefix) {
		this.nameSpacePrefix = prefix ;
	}
	
    /**
     */
	@Relationship( type="SOURCE", direction=Relationship.INCOMING )
	private Set<Neo4jAbstractOntology> ontology = new HashSet<Neo4jAbstractOntology>();

	/**
	 * 
	 * @return Set of Ontology associated with the Client
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<Ontology> getOntology() {
        return (Set<Ontology>)(Set)ontology;
    }

	/**
	 * 
	 * @param newLocations new Set of Locations to be associated with the Client
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setOntology( Set<Ontology> newOntology ) {
		ontology = (Set<Neo4jAbstractOntology>)(Set)newOntology ;
    }
}
