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

package bio.knowledge.model.core;

/**
 * @author Richard
 *
 */
public interface IdentifiedEntity 
	extends DatabaseEntity, Comparable<IdentifiedEntity> {

	//
	// Do we need UUID's in Knowledge.Bio?
	//
	// https://en.wikipedia.org/wiki/Universally_unique_identifier
	//
	// String getUuid() ;
	
	/**
	 * 
	 * @return a machine readable Uniform Resource Identifier of the identified entity
	 */
	String getUri();

	/**
	 * 
	 * @return a human readable canonical accession identifier of the identified entity
	 */
	String getId();

	/**
	 * 
	 * @return a human readable name or title of the identified entity
	 */
	String getName();

	/**
	 * 
	 * @return a String description of the identified entity
	 */
	String getDescription();
	
	/**
	 * 
	 * @return synonyms is a String of (pipe delimited?) alias identifiers of the entity
	 */
	String getSynonyms();

	/**
	 * @param uri is a machine readable Uniform Resource Identifier
	 */
	void setUri(String uri);
	
	/**
	 * Any prefix included in the Accession String should designate the Source ExternalDatabase namespace
	 * @param accessionId is a human readable canonical accession identifier of the identified entity
	 */
	void setId(String id);
	
	/**
	 * 
	 * @param name is a human readable String name or title of the identified entity
	 */
	void setName(String name);

	/**
	 * 
	 * @param description is a String description of the identified entity
	 */
	void setDescription(String description);
	
	/**
	 * 
	 * @param synonyms is a String of (pipe delimited?) alias identifiers of the entity
	 */
	void setSynonyms(String synonyms);

}