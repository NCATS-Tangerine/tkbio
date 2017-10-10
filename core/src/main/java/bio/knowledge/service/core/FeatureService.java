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

package bio.knowledge.service.core;

import java.util.List;

import bio.knowledge.model.core.AnnotatedEntity;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.ModelException;
import bio.knowledge.model.core.OntologyTerm;

/**
 * @author Richard
 *
 */
public interface FeatureService {

	/**
	 * @param owner
	 * @param tag
	 * @param value
	 * @return
	 * @throws ModelException
	 */
	public Feature createFeature( AnnotatedEntity owner, String tag, String value) throws ModelException;

	/**
	 * @param owner
	 * @param accessionId
	 * @param tagName
	 * @param value
	 */
	public Feature createFeature( AnnotatedEntity owner, String accessionId, String tagName, String value );

	/**
	 * @param owner
	 * @param accessionId
	 * @param tagTerm
	 * @param value
	 */
	public Feature createFeature( AnnotatedEntity owner, String accessionId, OntologyTerm tagTerm, String value );

	/**
	 * 
	 * @param owner
	 * @param tagTerm
	 * @param value
	 * @return
	 */
	public Feature createFeature( AnnotatedEntity owner, OntologyTerm tagTerm, String value ) ;
	
	/**
	 * Method to retrieve Features in a given AnnotatedEntity, based on the tag name string
	 * @param owner AnnotatedEntity to be searched for features
	 * @param tagName String query (exact match)
	 * @return
	 */
	List<Feature> findFeaturesByTagName(  AnnotatedEntity owner, String tagName ) ;

	/**
	 * This method uses a stipulated set of 
	 * String filters, assumed to be ordered, 
	 * to find a matching feature.
	 * 
	 * @param concept
	 * @param strings
	 * @return
	 */
	public Feature findFeatureByPrecedence( AnnotatedEntity concept, String[] strings );

}