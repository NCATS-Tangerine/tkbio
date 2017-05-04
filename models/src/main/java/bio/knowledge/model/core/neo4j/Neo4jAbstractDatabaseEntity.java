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

import java.util.Calendar;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import bio.knowledge.model.core.DatabaseEntity;

/**
 * @author Richard
 *
 * This class is the parent superclass of all STAR domain Neo4j Node classes
 * designed to hold globally common identification state variables
 * and perhaps, global behavior of Neo4j nodes persisted to databases.
 * 
 * Should be abstract, since this class is NOT generally directly instantiated, 
 * but Neo4j doesn't really like abstract classes for @NodeEntity tagged classes
 *
 */
@NodeEntity(label="DatabaseEntity")
public class Neo4jAbstractDatabaseEntity 
	implements DatabaseEntity {
	
	@GraphId
    private Long dbid;
	
    private Integer version=1;

    // Neo4j doesn't really support dates 
    // directly so...store as milliseconds?
    private Long versionDate=Calendar.getInstance().getTimeInMillis();
    
	public Neo4jAbstractDatabaseEntity() {
		super() ;
		version = 1 ;
		versionDate = Calendar.getInstance().getTimeInMillis() ;
	}
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.DatabaseEntity#getId()
	 */
	@Override
	public Long getDbId() {
        return dbid;
    }

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.DatabaseEntity#setId(java.lang.Long)
	 */
	@Override
	public void setDbId(Long dbid) {
        this.dbid = dbid;
    }

	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.VersionedObject#setVersion(java.lang.Integer)
	 */
	@Override
	public void setVersion(Integer version) {
        this.version = version;
    }
	
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.model.core.VersionedObject#getVersion()
	 */
	@Override
	public Integer getVersion() {
        return version;
    }

    /*
     * (non-Javadoc)
     * @see bio.knowledge.model.core.VersionedObject#setVersionDate(java.util.Date)
     */
    @Override
	public void setVersionDate(long date) {
        versionDate = date;
    }
    
    /*
     * (non-Javadoc)
     * @see bio.knowledge.model.core.VersionedObject#getVersionDate()
     */
	@Override
	public long getVersionDate() {
        return versionDate;
    }
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
	public String toString() {
    	return "STAR Neo4j Node "+ dbid.toString()+"."+getVersion().toString()+" ["+getVersionDate()+"]" ;
    }
    
}
