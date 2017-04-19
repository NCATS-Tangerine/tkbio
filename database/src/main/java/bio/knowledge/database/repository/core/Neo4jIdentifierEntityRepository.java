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

import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity; 

@Repository
public interface Neo4jIdentifierEntityRepository
	extends GraphRepository<Neo4jAbstractIdentifiedEntity> {

	@Query("MATCH (identifier:IdentifiedEntity) RETURN identifier")
	Iterable<Neo4jAbstractIdentifiedEntity> getIdentifiedEntities() ;

	/**
	 * 
	 * @param name of IdentifiedEntity to be matched
	 * @return List of IdentifiedEntity instances with exact, case sensitive matches to given name string.
	 */
	@Query("MATCH (identifier:IdentifiedEntity)"
			+ " WHERE identifier.name = {name})"
			+ " RETURN identifier"
		)
	List<Neo4jAbstractIdentifiedEntity> findByName(@Param("name")String name);

	/**
	 * @param filter string for approximate matching to name of IdentifiedEntity instances
	 * @return number of hits
	 */
	@Query("MATCH (identifier:IdentifiedEntity)"
			+ " WHERE LOWER(identifier.name)        CONTAINS LOWER({filter}) OR"
			      + " LOWER(identifier.description) CONTAINS LOWER({filter})"
			+ " RETURN count(identifier)"
		)
	long countByNameLikeIgnoreCase(@Param("filter")String filter);

	/**
	 * @param filter string for approximate matching to name of IdentifiedEntity instances
	 * @param pageable page range to constrain set of matches set back
	 * @return Page of IdentifiedEntity matches to filter string
	 */
	@Query("MATCH (identifier:IdentifiedEntity)"
			+ " WHERE LOWER(identifier.name)        CONTAINS LOWER({filter}) OR"
			      + " LOWER(identifier.description) CONTAINS LOWER({filter})"
			+ " RETURN identifier"
		)
	List<Neo4jAbstractIdentifiedEntity> findByNameLikeIgnoreCase(@Param("filter")String filter, Pageable pageable);
	
}
