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
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import bio.knowledge.graph.jsonmodels.Edge;
import bio.knowledge.graph.jsonmodels.Node;

public class ElementPredicates {
	
	public static Predicate<HashMap<String, HashMap<String,Serializable>>> hasPropertyWithValue(String key, Object value) {
		return node -> {
			return ((Node) node).getData().get(key) != null && ((Node) node).getData().get(key).equals(value);
		};
	}
	
	public static Predicate<HashMap<String, HashMap<String,Serializable>>> hasGeneSymbol() {
		return node -> {
			String possibleGeneName = ((Node) node).getData().getName().split(" ")[0];
			Pattern pattern = Pattern.compile(possibleGeneName+" ([Gg]ene|[Pp]rotein|[Mm]utation)");
			return pattern.matcher(((Node) node).getData().getName()).find();
		};
	}
	
    public static Predicate<Node> hasName(String nm) {
    	return n -> n.getData().getName() == nm;
    }
    public static Predicate<Node> hasSemtype(String sg) {
    	return n -> n.getData().getSemgroup() == sg;
    }

    public static Predicate<Node> hasNameOrSemtype(String nv) {
    	// nv = "node value"
    	return n -> n.getData().getSemgroup() == nv || n.getData().getName() == nv;
    }
    
    public static Predicate<Edge> hasSource(Node n) {
    	return e -> e.getData().getSource() == n.getData().getId();
    }
    public static Predicate<Edge> hasTarget(Node n) {
    	return e -> e.getData().getTarget() == n.getData().getId();
    }
    public static Predicate<Edge> hasSourceOrTarget(HashMap<String, HashMap<String, Serializable>>  n) {
    	return e -> ((Edge) e).getData().getTarget() == ((Node) n).getData().getId() || ((Edge) e).getData().getSource() == ((Node) n).getData().getId();
    }

    
}
