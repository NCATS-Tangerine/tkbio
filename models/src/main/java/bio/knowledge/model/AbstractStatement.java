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

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity;

public abstract class AbstractStatement  extends Neo4jIdentifiedEntity {
	@Relationship( type="SUBJECT" )
    private List<Concept> subjects = new ArrayList<Concept>() ;
    
	@Relationship( type="RELATION" )
    private Predicate relation ;

	@Relationship( type="OBJECT" )
    private List<Concept> objects = new ArrayList<Concept>() ;
	
	/*
	 *  The Transient subject and object attributes here
	 *  are only loaded when the Statement POJO is used 
	 *  as a DTO for transferring data to the "Relations" table.
	 */
	
	@Transient
	private Concept subject ;
	
	@Transient
	private Concept object ;

    @Relationship( type="EVIDENCE" )
	protected Evidence evidence ;
    
	protected AbstractStatement() {}
    
	/**
	 * 
	 * @param name
	 */
	protected AbstractStatement(String name) {
    	super(name);
    }
    
	
    /**
     * Constructor creates a new Statement (by Predicate)
     * but defers setting of related concepts.
     * 
     * 
     * @param accessionId
     * @param predicate
     */
    protected AbstractStatement(
    		String accessionId,
    		Predicate predicate
    ) {
    	super(accessionId,predicate.getName(),"") ;
    	setRelation(predicate);
    }
	
    /**
     * Constructor creates a new Statement (by Predicate)
     * but defers setting of related concepts.
     * 
     * 
     * @param accessionId
     * @param type
     * @param predicate
     */
    protected AbstractStatement(
    		String accessionId,
    		Concept subject,
    		Predicate predicate,
    		Concept object
    ) {
    	super(accessionId,subject.getName()+" - "+predicate.getName()+" -> "+object.getName(),"") ;
    	setSubject(subject);
    	setObject(object);
    	setRelation(predicate);
    	setEvidence(new Evidence());
    }
	
    /**
     * Constructor creates a new Statement (by Predicate.name)
     * but defers setting of related concepts.
     * 
     * @param accessionId
     * @param type
     * @param predicateName
     */
    protected AbstractStatement(
    		String accessionId,
    		String predicateName
    ) {
    	super(accessionId,predicateName,"") ;
    }

	/**
	 * 
	 * @param subject to be added to the Statement
	 */
	public void addSubject(Concept subject) {
		if(subjects==null)
			subjects = new ArrayList<Concept>() ;
		subjects.add(subject);
	}
	
	/**
	 * @param subjects set to be added with the Statement
	 */
	public void setSubjects(List<Concept> subjects) {
		this.subjects = subjects;
	}

	/**
	 * @return subjects associated with the Statement
	 */
	public List<Concept> getSubjects() {
		return subjects;
	}
	
	/**
	 * 
	 * @param subject
	 */
	public  void setSubject(Concept subject) {
		addSubject(subject);
		this.subject = subject ;
	}
	
	/**
	 * 
	 * @return
	 */
	public Concept getSubject() {
		return subject ;
	}

	/**
	 * @param predicate the predicate to set
	 */
	public void setRelation(Predicate relation) {
		this.relation = relation;
	}

    /**
	 * @return the predicate
	 */
	public Predicate getRelation() {
		return relation;
	}
	
	/**
	 * 
	 * @param subject to be added to the Statement
	 */
	public void addObject(Concept object) {
		if(objects==null)
			objects = new ArrayList<Concept>() ;
		objects.add(object);
	}
	
	/**
	 * @param objects set to be added with the Statement
	 */
	public void setObjects(List<Concept> objects) {
		this.objects = objects;
	}

	/**
	 * @return objects associated with the Statement
	 */
	public List<Concept> getObjects() {
		return objects;
	}
	
	/**
	 * 
	 * @param object
	 */
	public void setObject(Concept object) {
		addObject(object);
		this.object = object ;
	}
	
	/**
	 * 
	 * @return
	 */
	public Concept getObject() {
		return object ;
	}
	
	/**
	 * 
	 * @param evidence to be associated with the Statement
	 */
	public void setEvidence( Evidence evidence ) {
		this.evidence = evidence;
	}

	/**
	 * @return associated Evidence (e.g. References) supporting the Statement
	 */
	public Evidence getEvidence() {
		return evidence;
	}
    
    /*
     * (non-Javadoc)
     * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
     */
	@Override
    public String toString() {
    	return  "( subject:Concept "+subjects.toString()+
    			")-[:"+getName()+
    			"]->( object:Concept "+objects.toString()+")" ;
    	
    }
}
