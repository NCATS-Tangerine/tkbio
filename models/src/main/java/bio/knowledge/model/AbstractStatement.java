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

import bio.knowledge.model.core.AbstractIdentifiedEntity;

public abstract class AbstractStatement 
	extends AbstractIdentifiedEntity implements Statement {
	
    private List<IdentifiedConcept> subjects = new ArrayList<IdentifiedConcept>() ;
    
    private Predicate relation ;

    private List<IdentifiedConcept> objects = new ArrayList<IdentifiedConcept>() ;
	
	/*
	 *  The Transient subject and object attributes here
	 *  are only loaded when the Statement POJO is used 
	 *  as a DTO for transferring data to the "Relations" table.
	 */
	
	private IdentifiedConcept subject ;
	private IdentifiedConcept object ;

	protected Evidence evidence ;
	
	private String beaconUrl;
    
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
    		IdentifiedConcept subject,
    		Predicate predicate,
    		IdentifiedConcept object
    ) {
    	super(accessionId,subject.getName()+" - "+predicate.getName()+" -> "+object.getName(),"") ;
    	setSubject(subject);
    	setObject(object);
    	setRelation(predicate);
    	setEvidence(new EvidenceImpl());
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
    
    @Override
	public void setBeaconSource(String url) {
    	this.beaconUrl = url;
	}
    
    @Override
    public String getBeaconSource() {
    	return this.beaconUrl;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#addSubject(bio.knowledge.model.neo4j.Neo4jConcept)
	 */
	@Override
	public void addSubject(IdentifiedConcept subject) {
		if(subjects==null)
			subjects = new ArrayList<IdentifiedConcept>() ;
		subjects.add(subject);
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#setSubjects(java.util.List)
	 */
	@Override
	public void setSubjects(List<IdentifiedConcept> subjects) {
		this.subjects = subjects;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#getSubjects()
	 */
	@Override
	public List<IdentifiedConcept> getSubjects() {
		return subjects;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#setSubject(bio.knowledge.model.neo4j.Neo4jConcept)
	 */
	@Override
	public  void setSubject(IdentifiedConcept subject) {
		addSubject(subject);
		this.subject = subject ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#getSubject()
	 */
	@Override
	public IdentifiedConcept getSubject() {
		return subject ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#setRelation(bio.knowledge.model.neo4j.Neo4jPredicate)
	 */
	public void setRelation(Predicate relation) {
		this.relation = relation;
	}

    /* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#getRelation()
	 */
	
	public Predicate getRelation() {
		return relation;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#addObject(bio.knowledge.model.neo4j.Neo4jConcept)
	 */
	@Override
	public void addObject(IdentifiedConcept object) {
		if(objects==null)
			objects = new ArrayList<IdentifiedConcept>() ;
		objects.add(object);
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#setObjects(java.util.List)
	 */
	@Override
	public void setObjects(List<IdentifiedConcept> objects) {
		this.objects = objects;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#getObjects()
	 */
	@Override
	public List<IdentifiedConcept> getObjects() {
		return objects;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#setObject(bio.knowledge.model.neo4j.Neo4jConcept)
	 */
	@Override
	public void setObject(IdentifiedConcept object) {
		addObject(object);
		this.object = object ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#getObject()
	 */
	@Override
	public IdentifiedConcept getObject() {
		return object ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#setEvidence(bio.knowledge.model.neo4j.Neo4jEvidence)
	 */
	@Override
	public void setEvidence( Evidence evidence ) {
		this.evidence = evidence;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#getEvidence()
	 */
	@Override
	public Evidence getEvidence() {
		return evidence;
	}
    
    /*
     * (non-Javadoc)
     * @see bio.knowledge.model.core.neo4j.Neo4jIdentifiedEntity#toString()
     */
	/* (non-Javadoc)
	 * @see bio.knowledge.model.Statement#toString()
	 */
	@Override
    public String toString() {
    	return  "( subject:Concept "+subjects.toString()+
    			")-[:"+getName()+
    			"]->( object:Concept "+objects.toString()+")" ;
    	
    }
}
