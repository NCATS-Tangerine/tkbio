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

import bio.knowledge.model.core.ModelException;
import bio.knowledge.model.core.Ontology;
import bio.knowledge.model.core.OntologyTerm;

/**
 * @author Richard
 *
 */
public interface OntologyTermService
    extends IdentifiedEntityService<OntologyTerm> {

	/**
	 * 
	 * @param accessionId
	 * @return
	 * @throws ModelException
	 */
	OntologyTerm getOntologyTermByAccessionId( String accessionId ) throws ModelException;

	/**
	 * 
	 * @param ontology
	 * @param termName
	 * @return
	 * @throws ModelException
	 */
	OntologyTerm getOntologyTermByName( Ontology ontology, String termName ) throws ModelException;

	/**
	 * 
	 * @param ontologyName
	 * @param termName
	 * @return
	 * @throws ModelException
	 */
	OntologyTerm getOntologyTermByName( String ontologyName, String termName ) throws ModelException;

	/**
	 * 
	 * @param termName
	 * @return
	 * @throws ModelException
	 */
	// This variant of the ontology term search assumes globally unique term names
	// for example, unique Feature tag names, scientific data set types, etc.
	OntologyTerm getOntologyTermByName( String termName ) throws ModelException;

	/**
	 * 
	 * @param ontology
	 * @param accessionId
	 * @param termName
	 * @param definition
	 * @return
	 * @throws ModelException
	 */
	OntologyTerm addOntologyTerm(Ontology ontology, String accessionId, String termName, String definition) throws ModelException;

	/**
	 * 
	 * @return
	 */
	List<OntologyTerm> getOntologyTerms();

}
