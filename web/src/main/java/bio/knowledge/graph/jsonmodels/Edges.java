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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Kenneth Bruskiewicz
 * @author Rudy Kong
 *
 */
public class Edges extends HashSet<HashMap<String, HashMap<String, Serializable>>> implements ElementDataInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2559013533127187892L;
	
	private Set<HashMap<String, HashMap<String, Serializable>>> edges;
	
	/**
	 * 
	 * @return
	 */
	public Set<HashMap<String, HashMap<String, Serializable>>> getEdges() {
		return this.edges;
	}
	
	/**
	 * 
	 * @param list
	 */
	public void setEdges(java.util.Set<HashMap<String, HashMap<String, Serializable>>> list) {
		this.edges = list;
		update();
	}
	
	/**
	 * 
	 * @param edgeId
	 * @return
	 */
	public Edge getEdgeById(int edgeId) {
		Iterator<HashMap<String,HashMap<String,Serializable>>> itr = this.edges.iterator();
		while (itr.hasNext()) {
			Edge item = (Edge) itr.next();
			Integer itemId = (Integer) item.get("data").get("id");
			if (itemId.equals(edgeId)) {
				return (Edge) item;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param edge
	 */
	public void addEdge(Edge edge) {
		if (this.edges.contains(edge)) {
			// if it exists then just update state
			updateEdge(edge);
		} else {
			// else add new
			this.edges.add(edge);
			update();
		}
	}
	
	/**
	 * 
	 * @param edge
	 */
	private void updateEdge(Edge edge) {
		Iterator<HashMap<String, HashMap<String, Serializable>>> itr = this.edges.iterator();
		while (itr.hasNext()) {
			HashMap<String, HashMap<String, Serializable>> item = itr.next();
			String source = (String) item.get("data").get("source");
			String target = (String) item.get("data").get("target");
			String label = (String) item.get("data").get("label");

			if (source.equals(edge.getData().getSource()) && target.equals(edge.getData().getTarget())
					&& label.equals(edge.getData().getLabel())) {
				// changing state from "delete" to "add", there must be single edge so return
				item.get("data").put("state", "add");
				return;
			}
		}

	}
	
	/**
	 * 
	 * @param id
	 */
	public void deleteEdge(String id) {
		Iterator<HashMap<String, HashMap<String, Serializable>>> itr = this.edges.iterator();
		while (itr.hasNext()) {
			HashMap<String, HashMap<String, Serializable>> item = itr.next();
			String sourceId = (String) item.get("data").get("source");
			String targetId = (String) item.get("data").get("target");

			if ((sourceId.equals(id) || targetId.equals(id))) {
				// changing state from "add" to "delete", there can be more than one edge so don't break loop
				item.get("data").put("state", "delete");
			}
		}
	}
	
	/**
	 * 
	 * @param sourceId
	 * @param targetId
	 * @param edgeLabel
	 */
	public void deleteEdge(String sourceId, String targetId, String edgeLabel) {
		Iterator<HashMap<String, HashMap<String, Serializable>>> itr = this.edges.iterator();
		while (itr.hasNext()) {
			HashMap<String, HashMap<String, Serializable>> item = itr.next();
			String source = (String) item.get("data").get("source");
			String target = (String) item.get("data").get("target");
			String label = (String) item.get("data").get("label");

			if (source.equals(sourceId) && target.equals(targetId) && label.equals(edgeLabel)) {
				// changing state from "add" to "delete", there must be single edge so return
				item.get("data").put("state", "delete");
				return;
			}
		}
	}
	
	/**
	 * 
	 */
	public Edges() {
		// empty list
		edges = new HashSet<HashMap<String, HashMap<String, Serializable>>>();
		// update();
	}
	
	/**
	 * 
	 * @param list
	 */
	public Edges(java.util.Set<HashMap<String, HashMap<String, Serializable>>> list) {
		setEdges(list);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.ElementDataInterface#update()
	 */
	public void update() {
		for (Object x : this.edges) {
			if (!contains(x))
				add(extracted(x));
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param x
	 * @return
	 */
	private HashMap<String, HashMap<String, Serializable>> extracted(Object x) {
		return (HashMap<String, HashMap<String, Serializable>>) x;
	}

	public void sweepEdges() {
		Iterator<HashMap<String, HashMap<String, Serializable>>> itr1 = this.iterator();
		Iterator<HashMap<String, HashMap<String, Serializable>>> itr2 = this.edges.iterator();
		while (itr1.hasNext() && itr2.hasNext() && this.edges.size() == this.size()) {
			HashMap<String, HashMap<String, Serializable>> item1 = itr1.next();
			HashMap<String, HashMap<String, Serializable>> item2 = itr2.next();
			if(item1.get("data").get("state").equals("delete") && item1.equals(item2)) {
				itr1.remove();
				itr2.remove();
				return;
			}
		}		
	}

}
