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
package bio.knowledge.web.view;

public interface ViewName { 
	
	// View for simple display of data listing of one data type
	static final public String LIST_VIEW = "list" ;

	// 10-2-2016: New singleton concepts-by-name-text results table view
	static final public String CONCEPTS_VIEW  = "concepts";
	
	// New view for listing Library of Archived Concept Maps
	static final public String LIBRARY_VIEW  = "concept_map_library";
	
	static final public String RELATIONS_TAB = "Relations";
	static final public String EVIDENCE_TAB  = "Evidence";
	static final public String REFERENCE_TAB = "Reference";

	static final public String RELATIONS_VIEW   = "relations";
	static final public String EVIDENCE_VIEW    = "evidence";
	static final public String ANNOTATIONS_VIEW = "user_searching";
}
