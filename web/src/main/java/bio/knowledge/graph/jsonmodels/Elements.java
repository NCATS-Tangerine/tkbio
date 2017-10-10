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
import java.util.Set;

public class Elements extends HashMap<String, Set<HashMap<String, HashMap<String, Serializable>>>> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -45349436271218390L;
	
	private Nodes nodes = new Nodes();
	private Edges edges = new Edges();
	
	public void update() {
		put("nodes", nodes);
		put("edges", edges);
	}
	
	public Elements getElements() {
		return this;
	}
	
	public void setElements(Elements elements) {
		setNodes(elements.getNodes().getNodesAsCollection());
		setEdges(elements.getEdges());
	}

	public void setElements(Nodes nodes, Edges edges) {
		setNodes(nodes.getNodesAsCollection());
		setEdges(edges);
	}
	
	public Edges getEdges() {
		return edges;
	}

	public void setEdges(java.util.Set<HashMap<String, HashMap<String, Serializable>>> edges) {
		this.edges.setEdges(edges);
		put("edges", edges);
	}

	public Nodes getNodes() {
		return nodes;
	}

	public void setNodes(java.util.Set<Node> nodes) {
		this.nodes.setNodes(nodes);
		put("nodes", this.nodes);
	}

	public Elements() {
		// set to empty nodes and edges
		setElements(new Nodes(), new Edges());
	}
	
	public Elements(Nodes nodes, Edges edges) {
		setElements(nodes, edges);
	}
	
}
