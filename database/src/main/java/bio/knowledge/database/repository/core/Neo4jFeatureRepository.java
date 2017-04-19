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
package bio.knowledge.database.repository.core;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bio.knowledge.model.core.AnnotatedEntity;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.neo4j.Neo4jAbstractFeature;

@Repository
public interface Neo4jFeatureRepository extends GraphRepository<Neo4jAbstractFeature> {

	/**
	 * @param owner
	 * @param tagName
	 * @return
	 */
	@Query(" MATCH (owner:AnnotatedEntity) <-[:OWNER]-(feature:Feature)-[:TAG]->(tag:OntologyTerm) "
		 + "    WHERE id(owner) = {ownerId} AND tag.name = {tagName}"+
		   " RETURN feature"
	)
	List<Feature> findFeaturesByTagName( 
			@Param("ownerId") AnnotatedEntity owner, 
			@Param("tagName") String tagName 
	);

	/**
	 * @param concept
	 * @param strings
	 * @return
	 */
	@Query(" MATCH (owner:AnnotatedEntity) <-[:OWNER]-(feature:Feature)-[:TAG]->(tag:OntologyTerm) "
		 + "    WHERE id(owner) = {ownerId}"
		 + " AND ANY ( x IN {filter} WHERE UPPER(tag.name) = UPPER(x) )"+
		   " RETURN feature"
	)
	List<Feature> findFeatureByPrecedence( 
			@Param("ownerId") AnnotatedEntity owner, 
			@Param("filter") String[] filter
	);
}
