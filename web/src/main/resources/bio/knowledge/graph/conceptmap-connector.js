/**
 * 
 * @author Kenneth Bruskiewicz
 * @author Rudy Kong
 *
 */

window.bio_knowledge_graph_ConceptMapDisplay = function() {
        // Create the component
        var conceptMap = new cscape.ConceptMap(this.getElement());
        // variables to keep track of any change in map and accordingly apply layout,
        // if no change then do not apply same layout
        var oldElements = {};
        var oldLayout = "";
        var num_of_old_nodes = 0;
        var num_of_old_edges = 0;
        var nodeTracker;
        
        // Handle changes from the server-side
        this.onStateChange = function() {
        	var elements = this.getState().elements;
            nodeTracker = this.getState().nodeTracker;
            conceptMap.nodeTracker = nodeTracker;
            setStyleColor(this.getState().styleColor);

        	if (elements.nodes.length == 0) {
        		// clearMap
        		clearAll();
        		return;
        	}
        	
        	var nodes = elements.nodes;
        	updateEles("nodes", nodes, nodeUpdater);
        	
            var edges = elements.edges;
            updateEles("edges", edges, edgeUpdater);
            
            runLayout(this.getState().layout, nodes.length, edges.length);

        
            // To keep the nodes in sync with their parents, 
            for(var groupId in nodeTracker) {
				if (nodeTracker.hasOwnProperty(groupId)) {
		            if(Object.keys(nodeTracker[groupId]).length > 1) {
		            	for(var nodeId in nodeTracker[groupId]) {
		    				if (nodeTracker[groupId].hasOwnProperty(nodeId)) {
		    					setParentForNodes(nodeTracker[groupId][nodeId]["sharedNodePropertyKey"], nodeTracker[groupId][nodeId]["sharedNodePropertyValue"], groupId);
		    				}
		            	}
		    		}
				}
			}
		}

	// alias so that it can be executed locally

	// save concept map in different format, .kb and .PNG,
	// this method is called from ConceptMapDisplay.java
	save = function(format) {
		var content = conceptMap.resetEle.save(format);
		connector.export(content, format);
	};

	getContent = function() {
		var jsonContent = conceptMap.resetEle.getJsonContent();
		var pngContent = conceptMap.resetEle.getPngContent();
		connector.exportContent(jsonContent, pngContent);
	};

	// This method is called by ExportWindow's button click listener,
	// when the file-type is .png. I'm doing this because the method
	// used to save all other file types will result in corrupted
	// images when trying to save .png files.
	savePng = function(filename, url) {
		var a = document.createElement('a');
		a.download = filename + ".png";
		a.style = "display:none"
		a.href = url;
		document.getElementById('concept-map').appendChild(a);
		a.click();
		document.getElementById('concept-map').removeChild(a);
	};

	downloadContent = function(fileName, fileExtention, content) {
		var a = document.createElement('a');
		a.download = fileName + fileExtention;
		a.style = "display:none"
		a.href = content;
		document.getElementById('concept-map').appendChild(a);
		a.click();
		document.getElementById('concept-map').removeChild(a);
	}

        
	// this will align map to center
	alignCenter = function() {
     	conceptMap.alignCenter();
 	};
	
    setParentForNodes = function(key, value, parent) {
    	// we need to move nodes to new parent
    	conceptMap.setParentForNodes(key, value, parent);
    };

    showClientNodes = function() {
    	console.log(conceptMap.getClientNodes());
    };

    showClientEdges = function() {
    	console.log(conceptMap.getClientEdges());
    };
    
    // this will set the zoom of the map
    setZoom = function(value) {
     	conceptMap.setZoom(value);

    };

    // Pass user interaction to the server-side
	var connector = this;
	conceptMap.onNodeClick = function(id, name, x, y) {
	    connector.makeNodePopup(id, name, x, y);
	};
    
	conceptMap.onEdgeClick = function(source, target, label, x, y, description, uri, statementId) {
	    connector.makeEdgePopup(source, target, label, x, y, description, uri, statementId);
	};
	
	conceptMap.onDragElement = function() {
		connector.onDrag();
	}
	
    conceptMap.sweepNodes = function() {
    	connector.sweepNodes();
    }
    
    conceptMap.sweepEdges = function() {
    	connector.sweepEdges();
    }
    
    conceptMap.updatePosition = function(id, x, y) {
    	connector.updatePosition(id, x, y);
    } 
    
	conceptMap.onZoom = function(value) {
		connector.onZoom(value);
	}
    
    var updateEles = function(group_name, eles, updater) {
        //console.log(group_name, eles);
        for (var i = 0; i< eles.length; i++) {
        	var tempEle = eles[i];
        	//console.log(tempEle.toString());
        	tempEle["group"] = group_name;  
        	updater(tempEle);
        }
    }
    
    var nodeUpdater = function(ele) {
		conceptMap.resetEle.node(
				ele["data"]["id"],
				ele["data"]["name"],
				ele["data"]["semgroup"],
				ele["data"]["state"],
				ele["position"], 
				ele["data"]["cui"],
				ele["data"]["semtype"],
				ele["data"]["active_node"]
		);
    }

    var edgeUpdater = function(ele) {
    	conceptMap.resetEle.edge(
    			ele["data"]["source"],
    			ele["data"]["target"],
    			ele["data"]["label"],
    			ele["data"]["state"],
    			ele["data"]["description"],
    			ele["data"]["uri"],
    			ele["data"]["active_edge"],
			ele["data"]["statementId"]
    	);
    }

    
    var clearAll = function() {
    	conceptMap.resetEle.clear();
    }
    
    
    var setStyleColor = function(color) {
    	console.log("setStyleColor", color);
    	conceptMap.setStyleColor(color);
    }

    var runLayout = function(layout, nodeSize, edgeSize) {
		// apply layout only if it is different from previous map
		if (oldLayout == layout["name"] && num_of_old_nodes == nodeSize
				&& num_of_old_edges == edgeSize) {
			return;
		}
		
		conceptMap.runLayout(layout["name"]);
		oldLayout = layout["name"];
		num_of_old_nodes = nodeSize;
		num_of_old_edges = edgeSize;
		
		// create an array of (id, x, y)
		nodePositionCollection = []
		conceptMap.nodes().forEach(function(ele) {
			nodePositionCollection.push([ele.position("id"), ele.position("x"), ele.position("y")])
		});
		connector.updatePositionsFromLayout(nodePositionCollection);
	}
    
    var setStyleColor = function(color) {
    	conceptMap.setStyleColor(color);
    }

};

