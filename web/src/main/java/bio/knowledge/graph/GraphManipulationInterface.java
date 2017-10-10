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

import bio.knowledge.graph.jsonmodels.Edge;
import bio.knowledge.graph.jsonmodels.Edges;
import bio.knowledge.graph.jsonmodels.Elements;
import bio.knowledge.graph.jsonmodels.Node;
import bio.knowledge.graph.jsonmodels.Nodes;

public interface GraphManipulationInterface {
	
	public enum SupportedFormat{
		KB(".kb"),
		PNG(".png"),
		SIF(".sif"),
		TSV(".tsv");
		
		String extension;
		
		SupportedFormat(String type){
			this.extension = type;
		}
		
		public String getExtension(){
			return extension;
		}
		
		public static SupportedFormat getSupportedFormat(String type){
			for(SupportedFormat format:SupportedFormat.values()){
				if(format.getExtension().equals(type)){
					return format;
				}
			}
			return SupportedFormat.KB; // default
		}
		
		
	}
	
	public Elements  getElements();
	public void setElements(Elements elements);
	
	public Nodes getNodes();
	public void setNodes();
	
	public Node getNode();
	public void setNode(Node node);
	
	public Edges getEdges();
	public void setEdges();
	
	public Edge getEdge();
	public void setEdge();
	
	public void hashEleData();
	
	public void clearGraph() ;
	
	public void exportConceptMap(SupportedFormat format);
	
	public void importConceptMap(String fileContent);
	void resizeGraphCanvas();

	
}
