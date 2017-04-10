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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * 
 * @author Kenneth Bruskiewicz
 * @author Rudy Kong
 *
 */
public class Edge extends AbstractElement implements ParameterizedType {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5824957586864035523L;
	
	private Class<AbstractElement> ae;
	private EdgeData data;
	
	/**
	 * update data
	 */
	public void update() {
		data.update();
		put("data", data);
	}
	
	/**
	 * 
	 * @return
	 */
	public EdgeData getData() {
		return data;
	}
	
	/**
	 * 
	 * @param newData
	 */
	public void setData(EdgeData newData) {
		this.data = newData;
		newData.update();
		put("data", newData);
	}
	
	/**
	 * 
	 * @param data
	 */
	public Edge(EdgeData data) {
		setData(data);
	}
	
	/**
	 * 
	 * @param startNode
	 * @param endNode
	 */
	public Edge(String startNode, String endNode) {
		this(startNode, endNode, "");
	}
	
	/**
	 * 
	 * @param startNode
	 * @param endNode
	 * @param label
	 */
	public Edge(String startNode, String endNode, String label) {
		this(startNode, endNode, label, "add");
	}
	
	/**
	 * 
	 * @param startNode
	 * @param endNode
	 * @param label
	 * @param status
	 */
	public Edge(String startNode, String endNode, String label, String status) {
		this(startNode, endNode, label, status, "", "");
	}
	
	/**
	 * 
	 * @param startNode
	 * @param endNode
	 * @param label
	 * @param description
	 * @param uri
	 */
	public Edge(String startNode, String endNode, String label, String description, String uri) {
		this(startNode, endNode, label, "add", description, uri);
	}
	
	/**
	 * 
	 * @param startNode
	 * @param endNode
	 * @param label
	 * @param status
	 * @param description
	 * @param uri
	 */
	public Edge(String startNode, String endNode, String label, String status, String description, String uri) {
		setData(new EdgeData(startNode, endNode, label, status, description, uri));
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.AbstractElement#getActualTypeArguments()
	 */
	public Type[] getActualTypeArguments() {
		return new Type[] {"".getClass(), data.getClass()};
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.AbstractElement#getRawType()
	 */
	public Type getRawType() {
		return this.getRawType();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.AbstractElement#getOwnerType()
	 */
	public Type getOwnerType() {
		return ae.getClass();
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractMap#hashCode()
	 */
	public int hashCode(){
		return Arrays.hashCode(new Object[] {
	           this.getData().getSource(),    
	           this.getData().getTarget(),
	           this.getData().getLabel(),
	    });}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractMap#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof Edge) {
			EdgeData otherEdgeData = ((Edge) other).getData();
			if (this.getData().getSource().equals(otherEdgeData.getSource())
					&& this.getData().getTarget().equals(otherEdgeData.getTarget())
					&& this.getData().getLabel().equals(otherEdgeData.getLabel()))
				return true;
		}
		return false;
	}
}
