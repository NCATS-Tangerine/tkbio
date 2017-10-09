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
package bio.knowledge.graph.jsonmodels;

import java.lang.reflect.Type;

/**
 * 
 * @author Kenneth Bruskiewicz
 * @author Rudy Kong
 *
 */
public class EdgeData extends AbstractElementData {
	
	private static final long serialVersionUID = 8648870034063591468L;
	private int id;
	private String source;
	private String target;
	private String activeEdge = "no";
	private String statementId;
	
	// label of edge
	private String label;
	
	// status for checking whether it is new or delete
	private String state = "add";

	// user annotation if available
	private String description = "";
	private String uri = "";
	
	public String getStatementId() {
		return this.statementId;
	}
	
	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTarget() {
		return target;
	}
	
	/**
	 * 
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getActiveEdge() {
		return activeEdge;
	}
	
	/**
	 * 
	 * @param activeEdge
	 */
	public void setActiveEdge(String activeEdge) {
		this.activeEdge = activeEdge;
	}
	
	@Override
	/*
	 * stateful. updating two states is cheap so I do both simultaneously.
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.ElementDataInterface#update()
	 */
	public void update() {
		put("source", this.source);
		put("target", this.target);
		put("label", this.label);
		put("state", this.state);
		put("description", this.description);
		put("uri", this.uri);
		put("active_edge", this.activeEdge);
		put("statementId", this.statementId);
		updateId();
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.AbstractElementData#updateId()
	 */
	public void updateId() {
		this.id = (this.source+this.target+this.label).hashCode();
		put("id", this.id);
	};
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param label
	 * @param state
	 * @param description
	 * @param uri
	 */
	public EdgeData( String source, String target, String label, String state, String description, String uri, String statementId ) {
		setSource(source);
		setTarget(target);
		setLabel(label);
		setState(state);
		setDescription(description);
		setUri(uri);
		setStatementId(statementId);
		update();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUri() {
		return this.uri;
	}
	
	/**
	 * 
	 * @param uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.AbstractElementData#getActualTypeArguments()
	 */
	public Type[] getActualTypeArguments() {
		return new Type[] {"".getClass(), "".getClass()};
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.AbstractElementData#getRawType()
	 */
	public Type getRawType() {
		return this.getRawType();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
