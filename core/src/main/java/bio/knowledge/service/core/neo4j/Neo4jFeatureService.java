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
package bio.knowledge.service.core.neo4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.model.core.AnnotatedEntity;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.ModelException;
import bio.knowledge.model.core.OntologyTerm;
import bio.knowledge.model.core.neo4j.Neo4jAbstractAnnotatedEntity;
import bio.knowledge.model.core.neo4j.Neo4jAbstractFeature;
import bio.knowledge.service.core.FeatureService;

@Service("FeatureService")
public class Neo4jFeatureService implements FeatureService {
	
	@Autowired
	private Neo4jOntologyTermService ontologyTermService ;
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.FeatureService#createFeature(bio.knowledge.model.core.AnnotatedEntity, java.lang.String, java.lang.String)
	 */
	@Override
	public Feature createFeature( AnnotatedEntity owner, String tag, String value ) throws ModelException {
		return createFeature( owner, "", tag, value ) ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.FeatureService#createFeature(bio.knowledge.model.core.AnnotatedEntity, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Feature createFeature( AnnotatedEntity owner, String accessionId, String tag, String value ) {
		OntologyTerm tagTerm = ontologyTermService.getOntologyTermByName(tag) ;
		Feature feature = createFeature( owner, accessionId, tagTerm, value ) ;
		return feature ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.FeatureService#createFeature(bio.knowledge.model.core.AnnotatedEntity, java.lang.String, bio.knowledge.model.core.OntologyTerm, java.lang.String)
	 */
	@Override
	public Feature createFeature( AnnotatedEntity owner, String accessionId, OntologyTerm tagTerm, String value ) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.FeatureService#createFeature(bio.knowledge.model.core.AnnotatedEntity, bio.knowledge.model.core.OntologyTerm, java.lang.String)
	 */
	public Feature createFeature( AnnotatedEntity owner, OntologyTerm tagTerm, String value ) {
		return createFeature( owner, "", tagTerm, value ) ;
	}
	
	public Feature save(Feature feature) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.FeatureService#findFeaturesByTagName(bio.knowledge.model.core.AnnotatedEntity, java.lang.String)
	 */
	@Override
	public List<Feature> findFeaturesByTagName( AnnotatedEntity owner, String tagName ) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.FeatureService#findFeatureByPrecedence(bio.knowledge.model.Concept, java.lang.String[])
	 */
	@Override
	public Feature findFeatureByPrecedence( AnnotatedEntity owner, String[] filters ) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

}
