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
public class NodeData extends AbstractElementData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2536432318404170526L;
	
	private String id;
	private String name;
	private String semgroup;
	private String semtype;
	private String parent = "";
	private String state ="add";
	private String activeNode = "no";
	
	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
		update();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		update();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSemgroup() {
		return semgroup;
	}
	
	/**
	 * 
	 * @param semgroup
	 */
	public void setSemgroup(String semgroup) {
		this.semgroup = semgroup;
		update();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSemtype() {
		return semtype;
	}
	
	/**
	 * 
	 * @param semtype
	 */
	public void setSemtype(String semtype) {
		this.semtype = semtype;
		update();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getParent() {
		return parent;
	}
	
	/**
	 * 
	 * @param parent
	 */
	public void setParent(String parent) {
		this.parent = parent;
		update();
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
		update();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getActiveNode() {
		return activeNode;
	}
	
	/**
	 * 
	 * @param activeNode
	 */
	public void setActiveNode(String activeNode) {
		this.activeNode = activeNode;
		update();
	}
	
	@Override
	/*
	 * stateful. updating two states is cheap so I do both simultaneously.
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.ElementDataInterface#update()
	 */
	public void update() {
		put("id", this.id);
		put("name", this.name);
		put("semgroup", this.semgroup);
		put("semtype", this.semtype);
		put("parent", this.parent);
		put("state", this.state);
		put("active_node", this.activeNode);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 */
	public NodeData(String id, String name) {
		this.id = id;
		this.name = name;
		this.semgroup = "";
		this.semtype = "";
		this.parent = "";
		this.state = "";
		this.activeNode = "no";
		update();
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param semgroup
	 * @param state
	 */
	public NodeData(String id, String name, String semgroup, String state) {
		this.id = id;
		this.name = name;
		this.semgroup = semgroup;
		this.parent = "";
		this.state = state;
		this.activeNode = "no";
		update();
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param semgroup
	 * @param state
	 * @param activeNode
	 */
	public NodeData(String id, String name, String semgroup, String state, Boolean activeNode) {
		this.id = id;
		this.name = name;
		this.semgroup = semgroup;
		this.parent = "";
		this.state = state;
		this.activeNode = activeNode?"yes":"no";
		update();
	}
	
	/**
	 * 
	 */
	public NodeData() {
		this.id = "";
		this.name = "";
		this.semgroup = "";
		this.semtype = "";
		this.parent = "";
		this.state = "";
		this.activeNode = "no";
		update();
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
	
}
