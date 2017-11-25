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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author Kenneth Bruskiewicz
 * @author Rudy Kong
 *
 */
public class Nodes extends HashSet<HashMap<String, HashMap<String, Serializable>>> implements ElementDataInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4008554567634247124L;

	private Set<Node> nodes;
	
	/**
	 * 
	 * @return
	 */
	public Set<Node> getNodesAsCollection() {
		return this.nodes;
	}
	
	/**
	 * 
	 * @param list
	 */
	public void setNodes(java.util.Set<Node> list) {
		this.nodes = list;
		update();
	}
	
	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	public Node getNodeById(String nodeId) {
		
		ArrayList<Node> match = (ArrayList<Node>) this.nodes.parallelStream().filter(node -> {
			String itemId = (String) node.get("data").get("id");
			return itemId.equals(nodeId);
		}).collect(Collectors.toList());
		
		if(match.size() == 0) {
			return null;
		} else {
			return (Node) match.get(0);	
		}		
	}
	
	/**
	 * 
	 * @param node
	 */
	public void addNode(Node node, Edges edges) {
		
		String id = node.getData().getId();
		edges.restoreEdges(id);
		
		if (this.nodes.contains(node)) {
			// if it exists then just update state
			updateNode(node);
		} else {
			// else add new node
			this.nodes.add(node);
			update();
		}
	}
	
	/**
	 * 
	 * @param node
	 */
	private void updateNode(Node node) {
		Iterator<Node> itr = this.nodes.iterator();
		while (itr.hasNext()) {
			HashMap<String,HashMap<String,Serializable>> item = itr.next();
			String itemId = (String) item.get("data").get("id");
			if (itemId.equals(node.getData().get("id"))) {
				// changing state from "delete" to "add" , which I will use in JavaScript to delete this node.
				item.get("data").put("state", "add");
				// there must be single matched node with same id, so return or break loop
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param edges
	 * @return
	 */
	public String deleteNode(String id, Edges edges) {
		
		edges.deleteEdge(id);
		
		Iterator<Node> itr = this.nodes.iterator();
		String groupId = "" ;
		
		while (itr.hasNext()) {
			HashMap<String,HashMap<String,Serializable>> item = itr.next();
			String itemId = (String) item.get("data").get("id");
			String itemParent = (String) item.get("data").get("parent");

			if(itemParent.equals(id)){
				// Radical solution to parent compound node deletion: 
				// recursively delete the children and their edges too!
				deleteNode(itemId,edges);
			}
			
			if(itemId.equals(id)){
				// changing state from "add" to "delete", which I will use in JavaScript to delete this node.
				item.get("data").put("state", "delete");
				groupId = itemParent;
			}
		}
		
		return groupId ;
	}
	
	/**
	 * 
	 */
	public void sweepNodes() {
		Iterator<HashMap<String, HashMap<String, Serializable>>> itr1 = this.iterator();
		Iterator<Node> itr2 = this.nodes.iterator();
		while (itr1.hasNext() && itr2.hasNext() && this.nodes.size() == this.size()) {
			HashMap<String, HashMap<String, Serializable>> item1 = itr1.next();
			Node item2 = itr2.next();
				if(item1.get("data").get("state").equals("delete") && item1.equals(item2)) {
					itr1.remove();
					itr2.remove();

					// TODO: make this work
					// if we're deleting a node, we're only deleting one at a time.
					// so we may return
					
					return;
				}
		}
	};
	
	/**
	 * 
	 */
	public Nodes() {
		// empty list
		nodes = new HashSet<Node>();
		update();
	}
	
	/**
	 * 
	 * @param list
	 */
	public Nodes(java.util.Set<Node> list) {
		setNodes(list);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see bio.knowledge.graph.jsonmodels.ElementDataInterface#update()
	 */
	// Does update contain a notion of deletion?
	public void update() {
		for (Object x : this.nodes){
		   if (!contains(x))
		      add(extracted(x));
		}
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, HashMap<String, Serializable>> extracted(Object x) {
		return (HashMap<String, HashMap<String, Serializable>>) x;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractSet#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		return result;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractSet#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nodes other = (Nodes) obj;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		return true;
	}

}
