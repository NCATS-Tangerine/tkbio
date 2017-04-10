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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;

import bio.knowledge.model.Concept;

/**
 * 
 * @author Kenneth Bruskiewicz
 * @author Rudy Kong
 *
 */
public class Node extends HashMap<String, HashMap<String, Serializable>> implements ParameterizedType {

	private static final long serialVersionUID = 436360223563981083L;

	private Class<AbstractElement> ae;

	private NodeData data;
	private Position position;
	
	/**
	 * update data
	 */
	public void update() {
		data.update();
		position.update();
		put("data", data);
		put("position", position);
	}
	
	/**
	 * 
	 * @return
	 */
	public NodeData getData() {
		return data;
	}
	
	/**
	 * 
	 * @return
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * 
	 * @param newData
	 */
	public void setData(NodeData newData) {
		this.data = newData;
		newData.update();
		put("data", newData);
	}
	
	/**
	 * 
	 * @param position
	 */
	public void setPosition(Position position) {
		this.position = position;
		position.update();
		put("position", position);
	}
	
	/**
	 * 
	 * @param data
	 * @param position
	 */
	public Node(NodeData data, Position position) {
		setData(data);
		setPosition(position);
	}
	
	/**
	 * 
	 * @param newData
	 */
	public Node(NodeData newData) {
		this(newData, new Position(100, 100));
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param semgroup
	 * @param state
	 * @param x
	 * @param y
	 */
	public Node(String id, String name, String semgroup, String state, Integer x, Integer y) {
		setData(new NodeData(id, name, semgroup, state));
		setPosition(new Position(x, y));
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 */
	public Node(String id, String name) {
		this(id, name, "", "add", 100, 100);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param semgroup
	 * @param state
	 */
	public Node(String id, String name, String semgroup, String state) {
		this(id, name, semgroup, state, 100, 100);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param semgroup
	 */
	public Node(String id, String name, String semgroup) {
		this(id, name, semgroup, "add", 100, 100);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param semgroup
	 * @param x
	 * @param y
	 */
	public Node(String id, String name, String semgroup, Integer x, Integer y) {
		this(id, name, semgroup, "add", x, y);
	}
	
	/**
	 * 
	 * @param concept
	 */
	public Node(Concept concept) {
		this(new NodeData(concept.getAccessionId(), concept.getName()));
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see java.lang.reflect.ParameterizedType#getActualTypeArguments()
	 */
	public Type[] getActualTypeArguments() {
		return new Type[] { "".getClass(), data.getClass() };
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see java.lang.reflect.ParameterizedType#getRawType()
	 */
	public Type getRawType() {
		return this.getRawType();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see java.lang.reflect.ParameterizedType#getOwnerType()
	 */
	public Type getOwnerType() {
		return ae.getClass();
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractMap#hashCode()
	 */
	public int hashCode() {
		return Arrays.hashCode(new Object[] {
		           this.getData().getId(), 
		           this.getData().getName(),
		           this.getData().getSemgroup(),
		});
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractMap#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof Node) {
			Node otherNodeData = (Node) other;
			if (this.getData().getId().equals(otherNodeData.getData().getId())) {
				return true;
			}
		}
		return false;
	}

}
