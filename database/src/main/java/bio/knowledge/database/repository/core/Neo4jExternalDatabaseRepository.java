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

import bio.knowledge.model.core.neo4j.Neo4jAbstractExternalDatabase;

@Repository
public interface Neo4jExternalDatabaseRepository
	extends GraphRepository<Neo4jAbstractExternalDatabase> {

	@Query("MATCH (r:ExternalDatabase) RETURN r")
	Iterable<Neo4jAbstractExternalDatabase> getExternalDatabases() ;

	/**
	 * @param databaseName
	 * @return
	 */
	@Query("MATCH (db:ExternalDatabase)"
			+ " WHERE db.name = {databaseName}"
			+ " RETURN db"
		)
	Neo4jAbstractExternalDatabase findUniqueByNameEquals(@Param("databaseName")String databaseName);

	/**
	 * @param filter
	 * @return
	 */
	@Query("MATCH (database:ExternalDatabase)"
			+ " WHERE LOWER(database.name)        CONTAINS LOWER({filter}) OR"
		          + " LOWER(database.description) CONTAINS LOWER({filter})"
			+ " RETURN count(database)"
		)
	long countByNameLikeIgnoreCase(@Param("filter")String filter);

	/**
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query("MATCH (database:ExternalDatabase)"
			+ " WHERE LOWER(database.name)        CONTAINS LOWER({filter}) OR"
			      + " LOWER(database.description) CONTAINS LOWER({filter})"
			+ " RETURN database"
		)
	List<Neo4jAbstractExternalDatabase> findByNameLikeIgnoreCase(@Param("filter")String filter, Pageable pageable);
}
