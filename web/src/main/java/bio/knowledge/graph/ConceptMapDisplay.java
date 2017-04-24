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
package bio.knowledge.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.Notification;

import bio.knowledge.graph.jsonmodels.Edge;
import bio.knowledge.graph.jsonmodels.Edges;
import bio.knowledge.graph.jsonmodels.Elements;
import bio.knowledge.graph.jsonmodels.Layout;
import bio.knowledge.graph.jsonmodels.Node;
import bio.knowledge.graph.jsonmodels.Nodes;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.Concept;
import bio.knowledge.model.Statement;
import bio.knowledge.web.ui.DesktopUI;

@JavaScript({
		"https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js",
		"http://marvl.infotech.monash.edu/webcola/cola.v3.min.js",
		"https://cdn.rawgit.com/cpettitt/dagre/v0.7.4/dist/dagre.min.js",
		"https://cdnjs.cloudflare.com/ajax/libs/cytoscape/2.6.3/cytoscape.js",
		"https://cdn.rawgit.com/cytoscape/cytoscape.js-cola/1.1.1/cytoscape-cola.js",
		"https://cdn.rawgit.com/cytoscape/cytoscape.js-dagre/1.1.2/cytoscape-dagre.js",
		"https://cdn.rawgit.com/cytoscape/cytoscape.js-spread/1.0.9/cytoscape-spread.js", "conceptmap.js",
		"conceptmap-connector.js" 
		})
public class ConceptMapDisplay extends AbstractJavaScriptComponent implements GraphManipulationInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2713309497798105325L;
	
	public interface ValueChangeListener extends Serializable {
		void valueChange();
	}

	ArrayList<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public ConceptMapDisplayState getState() {
		return (ConceptMapDisplayState) super.getState();
	}

	public ConceptMapDisplay() {
		initializeRPCFunctions();
	}

	private void initializeRPCFunctions() {
		// on node click
		addFunction("makeNodePopup", arguments -> {
			String id = arguments.get(0).asString();
			String name = arguments.get(1).asString();
			double x = arguments.get(2).asNumber();
			double y = arguments.get(3).asNumber();

			System.out.println(id);
			System.out.println(name);
			System.out.println(x);
			System.out.println(y);

			((DesktopUI) getUI()).getPredicatePopupWindow().conceptMapNodePopUp(id, name, (int) x, (int) y);
		});
		// on edge click
		addFunction("makeEdgePopup", arguments -> {
			String source = arguments.get(0).asString();
			String target = arguments.get(1).asString();
			String label = arguments.get(2).asString();
			double x = arguments.get(3).asNumber();
			double y = arguments.get(4).asNumber();
			String description = arguments.get(5).asString();
			String uri = arguments.get(6).asString();
			
			((DesktopUI) getUI()).getPredicatePopupWindow().conceptMapEdgePopUp(source, target, label, (int) x,
					(int) y, description, uri);
		});

		// on dragging node or edge, change layout to manual
		addFunction("onDrag", arguments -> {
			((DesktopUI) getUI()).getDesktop().getCmLayoutSelect().setValue(DesktopUI.MANUAL_CM_LAYOUT);
		});


		// node deletion on client-side requires server-side data, so the client
		// needs to tell the server that it is done with deletion and ask it to
		// finish the job
		// these nodes are marked with a deletion flag, so we just iterate over
		// them all and remove them using the iterator
		addFunction("sweepNodes", arguments -> {
			getElements().getNodes().sweepNodes();
		});
		
		addFunction("sweepEdges", arguments -> {
			getElements().getEdges().sweepEdges();
		});
		
		addFunction("exportContent", arguments -> {
			String jsonContent = arguments.get(0).toJson();
			String pngContent = arguments.get(1).asString();
			
			ContentRequester contentRequester = contentRequesterStack.pop();
			contentRequester.processRequestedContent(jsonContent, pngContent);
		});
		
		addFunction("updatePosition", arguments -> {
			String nodeId = arguments.get(0).asString();
			double newX = arguments.get(1).asNumber();
			double newY = arguments.get(2).asNumber();			
			
			System.out.println("Updating Position Ran");
			System.out.println(nodeId);
			System.out.println(newX);
			System.out.println(newY);

			Node updatingNode = getElements().getNodes().getNodeById(nodeId);
			
			if(updatingNode != null) {
				updatingNode.getPosition().setPosition(newX, newY);
			}
			
		});
		
		addFunction("updatePositionsFromLayout", arguments -> {
			ArrayList<ArrayList<String>> positions = arguments.get(0);
			
			positions.parallelStream().map(data -> {
				String nodeId = data.get(0);
				double newX = Double.parseDouble(data.get(1));
				double newY = Double.parseDouble(data.get(2));
				getElements().getNodes().getNodeById(nodeId).getPosition().setPosition(newX, newY);
				return null;
			});
			
		});
	};
	
	private final Stack<ContentRequester> contentRequesterStack = new Stack<ContentRequester>();
	
	public void requestContent(ContentRequester contentRequester) {
		contentRequesterStack.push(contentRequester);
		Page.getCurrent().getJavaScript().execute("getContent()");
	}

	public ConceptMapDisplay(Elements elements) {

		initializeRPCFunctions();

		setElements(elements);

	}

	@Override
	public Elements getElements() {
		return (Elements) getState().elements;
	}

	@Override
	public void setElements(Elements elements) {
		getState().elements = elements;
		markAsDirty();
	}
	
	@Override
	public Nodes getNodes() {
		return getElements().getNodes();
	}

	@Override
	public void setNodes() {
		// TODO Auto-generated method stub
		markAsDirty();
	}

	public boolean isEmpty() {
		Nodes nodes = getNodes();
		return nodes.size()<1;
	}

	/*
	 * @Override public Node getNode() { return (Node) getState().node; }
	 * 
	 * @Override public void setNode(Node node) { // try dereferencing the data
	 * into a regular hashmap getState().node = node; markAsDirty(); }
	 */

	@Override
	public Edges getEdges() {
		return getElements().getEdges();
	}

	@Override
	public void setEdges() {
		// TODO Auto-generated method stub
		markAsDirty();
	}

	@Override
	public Edge getEdge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEdge() {
		// TODO Auto-generated method stub
		markAsDirty();
	}

	@Override
	public void hashEleData() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNode(Node node) {
		// TODO Auto-generated method stub
		markAsDirty();
	}

	public Layout getLayout() {
		return (Layout) this.getState().layout;
	}

	public void setLayout(Layout layout) {
		this.getState().layout = layout;
		markAsDirty();
	}

	public void runLayout() {
		setLayout(getLayout());
	};

	public String getStyleColor() {
		return this.getState().styleColor;
	}

	public void setStyleColor(String color) {
		this.getState().styleColor = color;
		markAsDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bio.knowledge.graph.GraphManipulationInterface#clearGraph()
	 */
	@Override
	public void clearGraph() {
		this.setElements(new Elements());
		getState().nodeTracker = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		markAsDirty();
	}

	/**
	 * API to align conceptMapDisplay in center.
	 */
	public void alignToCenter() {
		Page.getCurrent().getJavaScript().execute("alignCenter()");
	}
	
	/**
	 * API to set the zoom in conceptMapDisplay.
	 * @param  
	 */
	public void setZoom(double value) {
		Page.getCurrent().getJavaScript().execute("setZoom(" + value + ")");
	}

	@Override
	public void exportConceptMap(SupportedFormat format) {
		if (format.getExtension() != SupportedFormat.KB.getExtension()
				|| format.getExtension() == SupportedFormat.PNG.getExtension()) {
			// if any format other than ".kb" or ".jpeg" return
			return;
		}
		// calling js method available in conceptmap-connector.js
		Page.getCurrent().getJavaScript().execute("save('" + format.extension + "')");

	}

	@Override
	public void importConceptMap(String fileContent) {
		// initialize clean graph and nodeTraacker
		clearGraph();
		
		String content = fileContent;

		String jsonString = Pattern.quote("<data>") + "(.*?)" + Pattern.quote("</data>");

		Pattern pattern = Pattern.compile(jsonString);
		// text contains the full text that you want to extract data
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			String textInBetween = matcher.group(1); // Since (.*?) is
														// capturing group 1
			JSONObject obj = new JSONObject(textInBetween);
			JSONObject elements = obj.getJSONObject("elements");
			if (elements.has("nodes")) {
				JSONArray nodes = elements.getJSONArray("nodes");
				// prepare nodes
				for (int i = 0; i < nodes.length(); i++) {

					int x = nodes.getJSONObject(i).getJSONObject("position").getInt("x");
					int y = nodes.getJSONObject(i).getJSONObject("position").getInt("y");

					JSONObject data = nodes.getJSONObject(i).getJSONObject("data");
					String nodeId = data.getString("id");
					String nodeName = data.getString("name");
					String nodeGroup = data.getString("semgroup");

					Node newNode = new Node(nodeId, nodeName, nodeGroup, x, y);	

					// TODO: Could I use an existing parent to avoid certain steps in the generation of the nodeTracker somehow?
//					String nodeParent = "";
//					if (data.has("parent"))
//						nodeParent = data.getString("parent");
//	
//					newNode.getData().setParent(nodeParent);
					
					// use local addNodeToConceptMap function to generate the tracker
					this.addNodeToConceptMap(newNode);
				}
			}
			if (elements.has("edges")) {
				JSONArray edges = elements.getJSONArray("edges");
				// prepare edges
				for (int i = 0; i < edges.length(); i++) {
					JSONObject data = edges.getJSONObject(i).getJSONObject("data");
					String edgeSource = data.getString("source");
					String edgeTarget = data.getString("target");
					String edgeLabel  = data.getString("label");
					String edgeDescription  = data.getString("description");
					String edgeUri  = data.getString("uri");
					
					Edge newEdge = new Edge(edgeSource, edgeTarget, edgeLabel, edgeDescription, edgeUri);
					this.addEdgeToConceptMap(newEdge);
				}
			}

		} else {
			new Notification("Invalid kb File, Could not able to parse concept map details.",
					Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		}

	}

	public String convertToSIF() {
		return converter(true);
	}

	public String converterToTSV() {
		return converter(false);
	}

	/**
	 * Utility to convert in .sif or .tsv
	 * 
	 * @param isSIF
	 * @return
	 */
	private String converter(boolean isSIF) {
		Map<String, String> nodeMap = new HashMap<>();
		Set<String> visitedNode = new HashSet<>();
		String resultToReturn = new String();
		// node iteration
		Iterator<HashMap<String, HashMap<String, Serializable>>> nodeItr = getElements().getNodes().iterator();
		while (nodeItr.hasNext()) {
			HashMap<String, HashMap<String, Serializable>> item = nodeItr.next();
			String id = (String) item.get("data").get("id");
			String name = (String) item.get("data").get("name");
			nodeMap.put(id, name);
		}
		// edge iteration
		Iterator<HashMap<String, HashMap<String, Serializable>>> edgeIterator = getElements().getEdges().iterator();
		while (edgeIterator.hasNext()) {
			HashMap<String, HashMap<String, Serializable>> item = edgeIterator.next();
			String source = (String) item.get("data").get("source");
			String target = (String) item.get("data").get("target");
			String label = (String) item.get("data").get("label");

			String sourceName = nodeMap.get(source);
			String targetName = nodeMap.get(target);
			visitedNode.add(source);
			visitedNode.add(target);
			if (isSIF)
				resultToReturn = resultToReturn + sourceName + "\t" + label + "\t" + targetName + "\n";
			else
				resultToReturn = resultToReturn + source + "\t" + sourceName + "\t" + label + "\t" + target + "\t"
						+ targetName + "\n";
		}

		// this is for exporting any orphan node, without having any edge.
		Set<String> temp = nodeMap.keySet();
		temp.removeAll(visitedNode);
		for (String s : temp) {
			if (isSIF)
				resultToReturn = resultToReturn + nodeMap.get(s) + "\n";
			else
				resultToReturn = resultToReturn + s + "\t" + nodeMap.get(s) + "\n";
		}

		return resultToReturn;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bio.knowledge.graph.GraphManipulationInterface#resizeGraph()
	 */
	@Override
	public void resizeGraphCanvas() {
		com.vaadin.ui.JavaScript.getCurrent().execute(
				"var evt = document.createEvent('UIEvents');evt.initUIEvent('resize', true, false, window, 0);window.dispatchEvent(evt);");
	}

	public Elements mergeElements(Elements additionalElements) {
		// look at all elements for Id checks;
		Elements elements = new Elements();
		Iterator<HashMap<String,HashMap<String,Serializable>>> itrAgN = 
				additionalElements.getNodes().iterator();
		while (itrAgN.hasNext()) {
			Node theirs = (Node) itrAgN.next();
			Iterator<HashMap<String,HashMap<String,Serializable>>> itrThisN = 
					this.getElements().getNodes().iterator();
			while (itrThisN.hasNext()) {
				Node mine = (Node) itrThisN.next();
				if (theirs.getData().getId() == mine.getData().getId()) {
					if (theirs.getData().getSemgroup() == mine.getData().getSemgroup()) {
						mine.getData().setName(theirs.getData().getName());
					}
					;
				}
				;
				elements.getNodes().addNode(mine);
			}
			;
		}
		;
		elements.setEdges(additionalElements.getEdges());
		
		return elements;
	}

	public boolean isNodeWithProperyInGraph(String key, String value) {
		System.out.println(key + " " + value);
		Set<HashMap<String, HashMap<String, Serializable>>> matchingNodes = this.getElements().getNodes()
				.parallelStream().filter(node -> (((Node) node).getData().get(key) == value)).map(node -> {
					System.out.println("there is node " + node + " with property " + key + "=" + value);
					System.out.println(((Node) node).getData().toString());
					return node;
				}).collect(Collectors.toSet());
		System.out.println("matching nodes are " + matchingNodes.toString());
		return matchingNodes.parallelStream().count() > 0;
	}

	private final Pattern genePattern = 
			Pattern.compile("^(\\S+)\\s+([Gg]ene|[Pp]rotein|[Mm]utation)( ([Mm]utation|[Aa]mplification))?(, ([Hh]uman|[Mm]ouse))?") ;
	
	private String getGeneSymbol(String name) {
		Matcher m = genePattern.matcher(name);
		String groupId = (m.find() ? m.group(1) : "");
		System.out.println();
		return groupId;
	}

	/**
	 * 
	 * @param concept
	 */
	public void addNodeToConceptMap(Concept concept) {
	
		// create the new node from the passed-in data
		Node newNode = new Node(concept.getAccessionId(), concept.getName(), concept.getSemanticGroup().name(), "add");
		
		this.addNodeToConceptMap(newNode);
	
	}
	
	public void addNodeToConceptMap(Node newNode) {
		
		HashMap<String, HashMap<String, HashMap<String, String>>> currentNodeTracker = getState().nodeTracker;
		String groupId = generateGroupId(newNode.getData().getName(), newNode.getData().getId());
		
		// if no groupId, it is a parent. Add directly.
		if(!groupId.isEmpty()) {
			String groupName = generateGroupName(groupId, newNode.getData().getName());
			updateNodeTracker(currentNodeTracker, groupId, newNode.getData().getId(), newNode.getData().getName());
	
			// adding compound node != moving compound node. that occurs in the connector iff a groupId in the tracker contains more than one node
			if(currentNodeTracker.get(groupId).size() > 1) {
				getElements().getNodes().addNode(new Node(groupId, groupName));
			}
			// to avoid a lengthy constructor I just manually set the parent property here once it's calculated
			newNode.getData().setParent(groupId);
		}
		// add node to graph
		getElements().getNodes().addNode(newNode);
		
	};
	
	private void updateNodeTracker(HashMap<String, HashMap<String, HashMap<String,String>>> currentNodeTracker, String groupId, String nodeId, String nodeName) {
		String sharedNodePropertyKey;
		String sharedNodePropertyValue;
		String groupType = groupId.split("_")[0];
		
		// initialize groupId hash
		currentNodeTracker.put(groupId, (currentNodeTracker.get(groupId) == null
				? new HashMap<String, HashMap<String, String>>() : currentNodeTracker.get(groupId)));
		// initialize node's tracking hash, which gets passed to the client-side to update the graphical representation of the compound node
		currentNodeTracker.get(groupId).put(nodeId, currentNodeTracker.get(groupId).get(nodeId) == null 
				? new HashMap<String, String>() : currentNodeTracker.get(groupId).get(nodeId));
		
		if(groupType.equals("GENE")) {
			sharedNodePropertyKey = "name";
			sharedNodePropertyValue = nodeName;
		} else if (groupType.equals("CUI")) {
			sharedNodePropertyKey = "cui";
			sharedNodePropertyValue = groupId.split("_")[1];
		} else {
			return;
		}
		
		// set the tracker data
			// shareNodePropertyKey => Key for cytoscape selector
			// shareNodePropertyValue => Value for cytoscape selector
		currentNodeTracker.get(groupId).get(nodeId)
			.put("sharedNodePropertyKey", sharedNodePropertyKey);
		currentNodeTracker.get(groupId).get(nodeId)
			.put("sharedNodePropertyValue",  sharedNodePropertyValue);
	}
	
	private String generateGroupName(String groupId, String nodeName) {
		String groupName;
		String groupType = groupId.split("_")[0];
		
		if(groupType.equals("GENE")) {
			groupName = getGeneSymbol(nodeName);
		} else if (groupType.equals("CUI")) {
			groupName = nodeName;
		} else {
			return null;
		}		
		return groupName;
	}
	
	private String generateGroupId(String conceptName, String accessionId) {
		// create groupId
		// by default search for a geneSymbol. If it doesn't have a valid geneSymbol, it must be sharing a CUI with another concept.
		// since nodes could potentially satisfy either case, we set the dominating groupId to be that of the geneSymbol.
		String groupId = getGeneSymbol(conceptName);		
		if (!groupId.isEmpty()) { 
			groupId = "GENE_"+accessionId;
		}

		return groupId;
	};

	/**
	 * 
	 * @param subject
	 * @param object
	 * @param relationLabel
	 */
	
	public void addEdgeToConceptMap(Statement statement) {
		// any statement pre-processing goes here
		Annotation annotation = statement.getEvidence().getAnnotations().stream().collect(Collectors.toList()).get(0);
		String description;
		String uri;
		if(annotation != null) {
			description = annotation.getName() != null ? annotation.getName() : "";
			uri = annotation.getReference().getUri() != null ? annotation.getReference().getUri() : "";
		} else {
			description = "";
			uri = "";
		}
		this.addEdgeToConceptMap(statement.getSubject(), statement.getObject(), statement.getRelation().getName(), description, uri);
	}
	
	public void addEdgeToConceptMap(Concept subject, Concept object, String relationLabel, String description, String uri) {
		Edge newEdge = new Edge( subject.getAccessionId(), object.getAccessionId(), relationLabel );
		// any edge pre-processing would go here.
		newEdge.getData().setDescription(description);
		newEdge.getData().setUri(uri);
		newEdge.getData().update();
		this.addEdgeToConceptMap(newEdge);
	}
	
	// created to make the API consistent with that of the nodes
	public void addEdgeToConceptMap(Edge newEdge) {
		getElements().getEdges().addEdge(newEdge);
	}

	public void deleteNodefromConceptMap(String id) {
		
		HashMap<String, HashMap<String, HashMap<String, String>>> currentNodeTracker = getState().nodeTracker;
		Edges edges = getElements().getEdges();
		
		String groupId = getElements().getNodes().deleteNode(id, edges);

		if (!groupId.isEmpty()) {
			// I'm a regular node
			if (currentNodeTracker.get(groupId).size() > 0) {
				// remove me from tracking
				currentNodeTracker.get(groupId).remove(id);
			}
			if (currentNodeTracker.get(groupId).size() == 0) {
				// Tracker completely empty, remove group entirely
				currentNodeTracker.remove(groupId);
				getElements().getNodes().deleteNode(groupId, edges);
			}
		} else {
			currentNodeTracker.remove(id);
		}
		
	}

	public void deleteEdgefromConceptMap(String sourceId, String targetId, String label) {
		getElements().getEdges().deleteEdge(sourceId, targetId, label);
	}

	class Tuple<T> {
		T l;
		T r;

		Tuple(T l, T r) {
			this.l = l;
			this.r = r;
		}
	}

}