/**
 * 
 * @author Kenneth Bruskiewicz
 * @author Rudy Kong
 *
 */

"use strict";
var cscape = cscape || {};

cscape.ConceptMap = function(element) {
	this.element = element;
	this.element.innerHTML = "<div id='concept-map' style='height: 100%; width: 100%; position: absolute; background: #333;'></div>";
	var me = this;

	$(function() {
		// not to be confused with this.element, the html tag hosting the
		// concept map canvas
		var elements = {};
		var nodeTracker = {};
		var layout = {};
		var layouts = {
			manual : {
				name : 'manual'
				/*
				name : 'preset',
				positions : undefined, // map of (node id) => (position obj); or function(node){ return somPos; }
				zoom : undefined, // the zoom level to set (prob want fit = false if set)
				pan : undefined, // the pan level to set (prob want fit = false if set)
				fit : true, // whether to fit to viewport
				padding : 10, // padding on fit
				animate : false, // whether to transition the node positions
				animationDuration : 0, // duration of animation in ms if enabled
				animationEasing : undefined, // easing of animation if enabled
				ready : undefined, // callback on layoutready
				stop : undefined // callback on layoutstop
				*/
			},
			grid : {
				name : 'grid',
				fit : true, // whether to fit the viewport to the graph
				padding : 10, // padding used on fit
				boundingBox : undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
				avoidOverlap : true, // prevents node overlap, may overflow boundingBox if not enough space
				avoidOverlapPadding : 10, // extra spacing around nodes when avoidOverlap: true
				condense : false, // uses all available space on false, uses minimal space on true
				rows : undefined, // force num of rows in the grid
				cols : undefined, // force num of columns in the grid
				position : function(node) {
				}, // returns { row, col } for element
				sort : undefined, // a sorting function to order the nodes; e.g. function(a, b){ return a.data('weight') - b.data('weight') }
				animate : false, // whether to transition the node positions
				animationDuration : 0, // duration of animation in ms if enabled
				animationEasing : undefined, // easing of animation if enabled
				ready : undefined, // callback on layoutready
				stop : undefined
			// callback on layoutstop
			},
			circle : {
				name : 'circle',
				fit : true, // whether to fit the viewport to the graph
				padding : 10, // the padding on fit
				boundingBox : undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
				avoidOverlap : true, // prevents node overlap, may overflow boundingBox and radius if not enough space
				radius : 10, // the radius of the circle
				startAngle : 3 / 2 * Math.PI, // where nodes start in radians
				sweep : undefined, // how many radians should be between the first and last node (defaults to full circle)
				clockwise : true, // whether the layout should go clockwise (true) or counterclockwise/anticlockwise (false)
				sort : undefined, // a sorting function to order the nodes; e.g. function(a, b){ return a.data('weight') - b.data('weight') }
				animate : false, // whether to transition the node positions
				animationDuration : 0, // duration of animation in ms if enabled
				animationEasing : undefined, // easing of animation if enabled
				ready : undefined, // callback on layoutready
				stop : undefined
			// callback on layoutstop
			},
			breadthfirst : {
				name : 'breadthfirst',
				fit : true, // whether to fit the viewport to the graph
				directed : false, // whether the tree is directed downwards (or edges can point in any direction if false)
				padding : 10, // padding on fit
				circle : false, // put depths in concentric circles if true, put depths top down if false
				spacingFactor : 1.75, // positive spacing factor, larger => more space between nodes (N.B. n/a if causes overlap)
				boundingBox : undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
				avoidOverlap : true, // prevents node overlap, may overflow boundingBox if not enough space
				roots : undefined, // the roots of the trees
				maximalAdjustments : 0, // how many times to try to position the nodes in a maximal way (i.e. no backtracking)
				animate : false, // whether to transition the node positions
				animationDuration : 0, // duration of animation in ms if enabled
				animationEasing : undefined, // easing of animation if enabled
				ready : undefined, // callback on layoutready
				stop : undefined
			// callback on layoutstop
			},
			cola : {
				name : 'cose',

				// Called on `layoutready`
				ready : function() {
				},

				// Called on `layoutstop`
				stop : function() {
				},

				// Whether to animate while running the layout
				animate : true,

				// The layout animates only after this many milliseconds
				// (prevents flashing on fast runs)
				animationThreshold : 250,

				// Number of iterations between consecutive screen positions update
				// (0 -> only updated on the end)
				refresh : 20,

				// Whether to fit the network view after when done
				fit : true,

				// Padding on fit
				padding : 10,

				// Constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
				boundingBox : undefined,

				// Randomize the initial positions of the nodes (true) or use existing positions (false)
				randomize : false,

				// Extra spacing between components in non-compound graphs
				componentSpacing : 100,

				// Node repulsion (non overlapping) multiplier
				nodeRepulsion : function(node) {
					return 400000;
				},

				// Node repulsion (overlapping) multiplier
				nodeOverlap : 10,

				// Ideal edge (non nested) length
				idealEdgeLength : function(edge) {
					return 10;
				},

				// Divisor to compute edge forces
				edgeElasticity : function(edge) {
					return 100;
				},

				// Nesting factor (multiplier) to compute ideal edge length for nested edges
				nestingFactor : 5,

				// Gravity force (constant)
				gravity : 80,

				// Maximum number of iterations to perform
				numIter : 1000,

				// Initial temperature (maximum node displacement)
				initialTemp : 200,

				// Cooling factor (how the temperature is reduced between consecutive iterations
				coolingFactor : 0.95,

				// Lower temperature threshold (below this point the layout will end)
				minTemp : 1.0,

				// Whether to use threading to speed up the layout
				useMultitasking : true
			},
			dagre : {
				name : "dagre"
			},
			spread : {
				name : "spread"
			},
			concentric : {
				name : 'concentric',
				fit : true, // whether to fit the viewport to the graph
				padding : 10, // the padding on fit
				startAngle : 3 / 2 * Math.PI, // where nodes start in radians
				sweep : undefined, // how many radians should be between the first and last node (defaults to full circle)
				clockwise : true, // whether the layout should go clockwise (true) or counterclockwise/anticlockwise (false)
				equidistant : false, // whether levels have an equal radial distance betwen them, may cause bounding box overflow
				minNodeSpacing : 10, // min spacing between outside of nodes (used for radius adjustment)
				boundingBox : undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
				avoidOverlap : true, // prevents node overlap, may overflow boundingBox if not enough space
				height : undefined, // height of layout area (overrides container height)
				width : undefined, // width of layout area (overrides container width)
				concentric : function(node) { // returns numeric value for each node, placing higher nodes in levels towards the centre
					return node.degree();
				},
				levelWidth : function(nodes) { // the variation of concentric values in each level
					return nodes.maxDegree() / 4;
				},
				animate : false, // whether to transition the node positions
				animationDuration : 500, // duration of animation in ms if enabled
				animationEasing : undefined, // easing of animation if enabled
				ready : undefined, // callback on layoutready
				stop : undefined
			// callback on layoutstop
			}
		};

		var newConceptMap = cytoscape({
			container : $("#concept-map"),
			zoom : 0.8,
			boxSelectionEnabled : false,
			autounselectify : true,
			elements : this.elements,
			motionBlur : false,
			wheelSensitivity : 0.1
		});

		me.getElements = function() {
			console.log("getElements->", elements);
			return elements;
		};

		me.setElements = function(newElements) {
			console.log("setElements->", elements, "to", newElements);
			elements = newElements;
		};

		me.resetEle = {
			node : function(nodeId, newValue, semgroup, state, position, cui,
					semtype, activeNode) {
				
				// as we're querying on an ID, existingNode should be an array of precisely one element, as IDs refer to a unique node.
				var existingNode = newConceptMap.nodes('[id="' + nodeId + '"]');
				
				//var currentNode = newConceptMap.$("#" + nodeId);
				var newNode = {
					group : "nodes",
					data : {
						id : nodeId,
						name : newValue,
						semgroup : semgroup,
						cui : cui,
						semtype : semtype,
						active_node : activeNode
					},
					position : position,
				};

				// if existing node and state is delete, then remove from graph
				// else return
				// NOTE: condition for dealing with a parent node being deleted
				if (existingNode.size() > 0) {
					if (state == "delete") {
						if (newConceptMap.$('[id="' + nodeId + '"]').isParent()) {
							
							console.log(newConceptMap.$('node[parent="'
									+ nodeId + '"]'));
							
							
							newConceptMap.$('node[parent="' + nodeId + '"]')
									.move({
										parent : null
									});
						}

						existingNode[0].remove();
						me.sweepNodes();

						return;
					}
					existingNode[0].data("active_node", activeNode);
					return;
				}

				newConceptMap.add(newNode);
			},
			edge : function(source, target, label, state, description, uri,
					activeEdge, statementId) {
				var existingEdge = newConceptMap.edges('[id="' + source + target + label + '"]');
				var newEdge = {
					data : {
						// making id as combination of source+target+label
						id : source + target + label,
						source : source,
						target : target,
						label : label,
						description : description,
						uri : uri,
						active_edge : activeEdge,
						statementId : statementId
					},
					group : "edges",
					classes : 'autorotate'
				};
				
				if (existingEdge.size() > 0) {
					if (state == "delete") {
						existingEdge[0].remove();
						me.sweepEdges();
						return;
					}
					existingEdge[0].data("active_edge", activeEdge);
					return;
				}
				newConceptMap.add(newEdge);
			},
			clear : function() {
				var allElements = newConceptMap.elements();
				allElements.remove();
			},
			all : function(newElements) {
				newConceptMap.add(newElements);
			},
			save : function(format) {
				var name = document.getElementById('filename').value;
				var content;
				if (format == ".kb") {
					content = newConceptMap.json();
				} else if (format == ".png") {
					content = newConceptMap.png();
					var url = content.replace(/^data:image\/png/,
							'data:application/octet-stream');
					var a = document.createElement('a');
					a.download = name + format;
					a.style = "display:none"
					a.href = url;
					document.getElementById('concept-map').appendChild(a);
					a.click();
					document.getElementById('concept-map').removeChild(a);
				}
				return content;
			},
			getJsonContent : function() {
				return newConceptMap.json();
			},
			getPngContent : function() {
				var content = newConceptMap.png();
				var url = content.replace(/^data:image\/png/,
						'data:application/octet-stream');
				return url;
			},
			savePng : function(url) {
				var a = document.createElement('a');
				a.download = name + format;
				a.style = "display:none"
				a.href = url;
				document.getElementById('concept-map').appendChild(a);
				a.click();
				document.getElementById('concept-map').removeChild(a);
			}

		}

		me.addEle = function(ele) {
			newConceptMap.add(ele);
		}

		// TODO
		me.runLayout = function(layout) {
			var givenLayout = layouts[layout];
			
			// we need to establish both cases as Javascript does an emulation of pass-by-reference; so we would have been editing the local object's fit parameter which would have propagated to another call of the initial layout.
			if (newConceptMap.nodes().length == 1) { // if only one node then dont fit to viewport
				givenLayout["fit"] = false;
			} else {
				givenLayout["fit"] = true;
			}
			
			newConceptMap.layout(givenLayout);
		}

		me.setStyleColor = function(color) {
			$.get("VAADIN/themes/kb2/graph/" + color.toLowerCase()
					+ "-style.ccss", function(data) {
				newConceptMap.style(data);
			}, "text");
			/*
			 * $.get("VAADIN/themes/kb2/graph/"+color.toLowerCase()+"-style-canvas.css",
			 * function (data) { $("#concept-map").css().css() }, "text");
			 */
			// TODO: Abstract away into stylesheet call and modification
			if (color === "light") {
				me.element.firstElementChild.style.background = "white";
				//				me.element.firstElementChild.style.border = "1px solid #333";
			} else if (color === "dark") {
				me.element.firstElementChild.style.background = "#333";
				//				me.element.firstElementChild.style.border = "1px solid white";
			}

		}
		// TODO: Fix masking-from-canvas-sizing problem
		me.forceRender = function() {
			newConceptMap.forceRender();
		}

		me.alignCenter = function() {
			newConceptMap.center();
			newConceptMap.fit();
		}

		me.showElementsAsJson = function() {
			console.log(newConceptMap.json().elements);
		}

		me.setParentForNodes = function(key, value, parentId) {
			newConceptMap.nodes().filterFn(function(ele) {
				return ele.data(key) == value;
			}).move({
				parent : parentId
			})
		}

		me.getClientNodes = function() {
        	return newConceptMap.json().elements.nodes;
        }

        me.getClientEdges = function() {
        	return newConceptMap.json().elements.edges;
        }
        
        me.setZoom = function(value) {
			newConceptMap.zoom(value / 100);
			newConceptMap.center();
		}

		function getAbsolutePosition(event) {
			var relativeX = event.cyRenderedPosition.x;
			var relativeY = event.cyRenderedPosition.y;
			console.log(relativeX);
			console.log(relativeY);

			var mapX = document.getElementById("concept-map")
					.getBoundingClientRect().left;
			var mapY = document.getElementById("concept-map")
					.getBoundingClientRect().top;

			console.log(mapX);
			console.log(mapY);

			var x = mapX + relativeX;
			var y = mapY + relativeY;

			if ((x + 200) > document.getElementById("concept-map")
					.getBoundingClientRect().right) {
				x = x - 400;
			} else {
				x = x - 200
			}
			var absoluteNodePosition = {
				"x" : x,
				"y" : y
			};
			return absoluteNodePosition;
		}

		newConceptMap.on("tap", "node", function(event) {
			var absoluteNodePosition = getAbsolutePosition(event);
			me.onNodeClick(event.cyTarget.data("id"), event.cyTarget
					.data("name"), absoluteNodePosition.x,
					absoluteNodePosition.y, true);
		});

		newConceptMap.on("tap", "edge", function(event) {
			var absoluteEdgePosition = getAbsolutePosition(event)
			me.onEdgeClick(event.cyTarget.data("source"), event.cyTarget
					.data("target"), event.cyTarget.data("label"),
					absoluteEdgePosition.x, absoluteEdgePosition.y,
					event.cyTarget.data("description"), event.cyTarget
							.data("uri"), event.cyTarget.data("statementId"));
		});

		newConceptMap.on('drag', function(event) {
			var evtTarget = event.cyTarget;
			if (evtTarget === newConceptMap) {
				console.log('dragging background');
			} else {
				console.log('dragging some element');
				me.onDragElement();
			}
		});

		newConceptMap.on('free', 'node', function(event) {
			var evtTarget = event.cyTarget;
			if (evtTarget === newConceptMap) {
				console.log("position of node changes");
			} else {
				console.log("position of node changes");
				me.updatePosition(evtTarget.data("id"), evtTarget.position("x"), evtTarget.position("y"));
			}
		});
		
		newConceptMap.on('zoom', function(event) {
			me.onZoom(newConceptMap.zoom() * 100);
		});
		
		// hack to force resize of canvas
		// see
		// http://stackoverflow.com/questions/1818474/how-to-trigger-the-window-resize-event-in-javascript/18693617#comment49783195_18693617
		var evt = document.createEvent('UIEvents');
		evt.initUIEvent('resize', true, false, window, 0);
		window.dispatchEvent(evt);

	});
	
}
