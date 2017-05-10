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
package bio.knowledge.database.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import bio.knowledge.model.Concept;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.neo4j.Neo4jConcept;

/**
 * @author Richard
 *
 */
public interface ConceptRepository extends GraphRepository<Neo4jConcept> {
	
	@Query( "CREATE CONSTRAINT ON (concept:Concept)"
	      + " ASSERT concept.accessionId IS UNIQUE")
	public void createUniqueConstraintOnConceptId() ;
	
	/**
	 * @return
	 */
	@Query( "MATCH ( concept:Concept ) RETURN concept" )
	public Iterable<Neo4jConcept>  getConcepts();

	/**
	 * 
	 */
	@Query( "DROP CONSTRAINT ON (concept:Concept)"
	      + " ASSERT concept.accessionId IS UNIQUE")
	public void dropUniqueConstraintOnConceptId() ;
	
	/**
	 * 
	 */
	@Query( "DROP INDEX ON :Concept(accessionId)")
	public void dropIndexOnConceptId() ;
	
	/**
	 * @param accessionId
	 * @return Concept identified by the accessionId
	 */
	@Query( "MATCH ( concept:Concept ) WHERE concept.accessionId = {accessionId} RETURN concept")
	public Neo4jConcept findById( @Param("accessionId") String accessionId ) ;
	
	/**
	 * 
	 * @param id of concept 
	 * @return matching Concept
	 */
	@Query( "MATCH ( concept:Concept )"
			+ " WHERE concept.semMedDbConceptId = {id}"
		 + " RETURN concept")
	public Neo4jConcept findBySemMedDbConceptId( @Param("id")String id ) ;
	
	/**
	 * 
	 * @param id of implicitome concept
	 * @return matching Concept
	 */
	@Query( "MATCH ( concept:Concept ) WHERE concept.implicitomeConceptId = {id}"
		 + " RETURN concept")
	public Neo4jConcept findByImplicitomeConceptId( @Param("id") String id ) ;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Query( "MATCH ( concept:Concept )"
			+" WHERE concept.semMedDbConceptId = {id}"
			+" RETURN concept" 
			+" UNION"
			+" MATCH ( concept:Concept ) WHERE concept.implicitomeConceptId = {id}"
			+" RETURN concept"
		 )
	public Neo4jConcept findByConceptId(@Param("id") Long id);

	/**
	 * @param filter string to match (as an embedded substring, non-case-sensitive)
	 * @return count of Concepts entries with names matching the filter
	 */
	@Query(
			"MATCH (concept:Concept) "+
			"WHERE "+
			"    LOWER(concept.name)     CONTAINS LOWER({filter}) OR"+
			"    LOWER(concept.synonyms) CONTAINS LOWER({filter})"+
		    " RETURN count(concept)"
		)
	public long countByNameLikeIgnoreCase( @Param("filter") String filter);
	
	/**
	 * @param filter string to match (as an embedded substring, non-case-sensitive)
	 * @param pageable specification of what page and page size of Concept data entries to return
	 * @return
	 */
	@Query(
			 "MATCH (concept:Concept) "+
			" WHERE "+
			"    LOWER(concept.name)     CONTAINS LOWER({filter}) OR"+
			"    LOWER(concept.synonyms) CONTAINS LOWER({filter})"+
			" RETURN concept"+
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize"
	)

	public List<Neo4jConcept> findByNameLikeIgnoreCase( @Param("filter") String filter, Pageable pageable );
	
	@Query("MATCH (concept:Concept) WHERE concept.accessionId = {curieId} RETURN concept;")
	public Concept apiGetConceptById(@Param("curieId") String curieId);
	
	@Query(
			" MATCH (concept:Concept) " +
			" WHERE ( " +
			"     ANY (x IN {filter} WHERE LOWER(concept.name)     CONTAINS LOWER(x)) OR " +
			"     ANY (x IN {filter} WHERE LOWER(concept.synonyms) CONTAINS LOWER(x)) " +
			" ) AND (" +
			"     {semanticGroups} IS NULL OR SIZE({semanticGroups}) = 0 OR " +
			"     ANY (x IN {semanticGroups} WHERE LOWER(concept.semanticGroup) = LOWER(x)) " +
			" ) " +
			" RETURN concept " +
			" SKIP  ({pageNumber} - 1) * {pageSize} " +
			" LIMIT {pageSize} "
	)
	public List<Neo4jConcept> apiGetConcepts(
			@Param("filter") String[] filter,
			@Param("semanticGroups") String[] semanticGroups,
			@Param("pageNumber") Integer pageNumber,
			@Param("pageSize") Integer pageSize
	);


	/**
	 * @param name
	 * @return
	 */
	@Query(
			 "MATCH (concept:Concept) "+
			" WHERE "+
			"    LOWER(concept.name) = LOWER({name}) AND "+
			"    concept.semanticGroup = {semanticGroup}"+
			" RETURN concept"
	)
	public List<Neo4jConcept> findConceptByNameAndType(
						@Param("name") String name,
						@Param("semanticGroup") SemanticGroup semanticGroup
					);
	/**
	 * @param filter
	 * @return
	 */
	@Query( "MATCH (concept:Concept)"+
			"WHERE "+
			"   concept.usage > 0 AND"+
			"   ( "+
			"     ALL (x IN {filter} WHERE LOWER(concept.name)     CONTAINS LOWER(x)) OR "+
			"     ALL (x IN {filter} WHERE LOWER(concept.synonyms) CONTAINS LOWER(x)) "+
			"   ) "+
			"RETURN count(concept)" 
		)
	public long countByInitialSearch(@Param("filter") String[] filter);


	/**
	 * 
	 */
	@Query( "MATCH (concept:Concept) "+
			"  WHERE concept.usage > 0 "+
			"RETURN count(concept)")
	public long countAll();
	
	/**
	 * 
	 */
	@Query( "MATCH (n:Concept) " +
			  "WHERE NOT n.semanticGroup IS NULL "+
			  "RETURN n.semanticGroup AS type, COUNT(n.semanticGroup) AS frequency")
	public List<Map<String,Object>> countAllGroupBySemanticGroup();
	
	/**
	 * Right now accountId and groupId are only being used to count the number
	 * of concept maps attached to the library that are visible to the user
	 * (i.e., are public, created by the user, or shared with a group that
	 * the user belongs to).
	 * 
	 * @param filter
	 *            string to match (as an embedded substring, non-case-sensitive)
	 * @param pageable
	 *            specification of what page and page size of data to return
	 * @return
	 */
	@Query( "MATCH path = (concept:Concept)-[:LIBRARY]->(library:Library)"+
			" WHERE"+
			"    concept.usage > 0 AND"+
			"   ( "+
			"     ALL (x IN {filter} WHERE LOWER(concept.name)     CONTAINS LOWER(x)) OR"+
			"     ALL (x IN {filter} WHERE LOWER(concept.synonyms) CONTAINS LOWER(x))"+
			"   )"+
			" OPTIONAL MATCH (library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE "+ ConceptMapArchiveRepository.conceptMapIsPermitted +
			" WITH COUNT(cm) as c, path AS path, concept AS concept, library AS library" +
			" SET library.numberOfVisibleMaps = c" +
			
			" RETURN path"+			
			"    ORDER BY concept.usage DESC"+
			"    SKIP  {1}.pageNumber*{1}.pageSize"+
			"    LIMIT {1}.pageSize"
		)
	public List<Neo4jConcept> findByInitialSearch(
			@Param("filter") String[] filter,
			Pageable pageable,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);
	
	/**
	 * 
	 * @param semanticGroups
	 * @param filter string to match (as an embedded substring, non-case-sensitive)
	 * @param pageable specification of what page and page size of data to return
	 * @return
	 */
	@Query( " MATCH path = (concept:Concept)-[:LIBRARY]->(library:Library)"+
			" WHERE"+
			"   concept.usage > 0 AND"+
			"   ( size({semanticGroups}) = 0"+
			"     OR ANY ( x IN {semanticGroups} WHERE LOWER(concept.semanticGroup) CONTAINS LOWER(x) )"+
			"   ) AND"+
			"   ( "+
			"     ALL (x IN {filter} WHERE LOWER(concept.name)     CONTAINS LOWER(x)) OR"+
			"     ALL (x IN {filter} WHERE LOWER(concept.synonyms) CONTAINS LOWER(x))"+
			"   )"+
			" OPTIONAL MATCH (library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE "+ ConceptMapArchiveRepository.conceptMapIsPermitted +
			" WITH COUNT(cm) as c, path AS path, concept AS concept, library AS library" +
			" SET library.numberOfVisibleMaps = c" +
			" RETURN path"+
			" ORDER BY concept.usage DESC"+
			" SKIP  {2}.pageNumber*{2}.pageSize"+
			" LIMIT {2}.pageSize"
		)
	public List<Neo4jConcept> findByNameLikeIgnoreCase(
			@Param("semanticGroups") ArrayList<String> semanticGroups, 
			@Param("filter") String[] filter, 
			Pageable pageable,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);

	/**
	 * @param pageable
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	@Query( "MATCH path = (concept:Concept)-[:LIBRARY]->(library:Library)"+
			" WHERE concept.usage > 0"+
			" OPTIONAL MATCH (library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE "+ ConceptMapArchiveRepository.conceptMapIsPermitted +
			" WITH COUNT(cm) as c, path AS path, concept AS concept, library AS library" +
			" SET library.numberOfVisibleMaps = c" +
			" RETURN path"+			
			"    ORDER BY concept.usage DESC"+
			"    SKIP  {0}.pageNumber*{0}.pageSize"+
			"    LIMIT {0}.pageSize"
		)
	public List<Neo4jConcept> findAllByPage(
			Pageable pageable,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);
	
}
