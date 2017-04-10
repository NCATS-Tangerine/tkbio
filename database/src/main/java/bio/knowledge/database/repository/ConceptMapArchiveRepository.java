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

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.Library;

/**
 * @author Richard
 * @author Chandan Mishra
 *
 */
@Repository
public interface ConceptMapArchiveRepository extends GraphRepository<ConceptMapArchive> {
	public static String conceptMapIsPermitted =
			" ( "+
				" ( NOT cm.isPublic IS NULL AND cm.isPublic = true ) OR " +
				" ( NOT cm.authorsAccountId IS NULL AND cm.authorsAccountId = {accountId} ) OR "+
				" ( NOT cm.groupId IS NULL AND cm.groupId IN {groupIds} ) "+
			" ) ";

	@Query( " MATCH (cm:ConceptMap {name: {conceptMapName}}) WHERE " +
			conceptMapIsPermitted +
			" RETURN cm")
	public List<Map<String, Object>> getConceptMapArchiveByName(
			@Param("conceptMapName") String conceptMapName,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);
	
	@Query( " MATCH (concept:Concept { accessionId : {accessionId} })"+
			" MERGE (concept)-[:LIBRARY]->(library:Library) "+
			" ON CREATE SET library.numberOfArchivedMaps = 0"+
			" WITH library"+
			" MATCH (cm:ConceptMap) WHERE id(cm) = {archiveId} "+
			" MERGE (library)-[:ASSOCIATED_MAP]->(cm)"+
					" ON CREATE SET library.numberOfArchivedMaps = library.numberOfArchivedMaps + 1"
			)
	void attachLibraryToConcept(@Param("archiveId") Long archiveId, @Param("accessionId") String accessionId);
		
	@Query( " MATCH (library:Library) WHERE id(library) = {libraryId} "+
			" MATCH (library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)" +
			" OPTIONAL MATCH (cm:ConceptMap)-[:PARENTS]->(parents:Library)"+
			" RETURN DISTINCT cm as conceptMap, parents as parents"+
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize"
			)
	public List<Map<String, Object>> getConceptMapArchiveByLibrary(@Param("libraryId") Library library, Pageable pageable);
	
	@Query( " MATCH (library:Library) WHERE id(library) = {libraryId} "+
			" MATCH (library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)" +
			" WHERE ALL (x IN {filter} WHERE LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" AND " + ConceptMapArchiveRepository.conceptMapIsPermitted +
			" OPTIONAL MATCH (cm:ConceptMap)-[:PARENTS]->(parents:Library)"+
			" RETURN DISTINCT cm as conceptMap, parents as parents"+
			" SKIP  {2}.pageNumber*{2}.pageSize"+
			" LIMIT {2}.pageSize"
			)
	public List<Map<String, Object>> getConceptMapArchiveByLibraryFiltered(
			@Param("libraryId") Library library,
			@Param("filter") String[] filter,
			Pageable pageable,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);
	
	@Query("MATCH (library:Library) WHERE id(library) = {libraryId}"+
			" MATCH (library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)" +
			" RETURN count(cm)"
			)
	public Long countConceptMapArchiveByLibrary(@Param("libraryId") Library library );

	@Query("MATCH (library:Library) WHERE id(library) = {libraryId}"+
			" MATCH (library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)" +
			" WHERE ALL (x IN {filter} WHERE LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" RETURN count(cm)"
			)
	public Long countConceptMapArchiveByLibraryFiltered(@Param("libraryId") Library library, @Param("filter") String[] filter);

	
	@Query(  
			" MATCH (concept:Concept)-[:LIBRARY]->(library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap) "+
			" WHERE ALL (x IN {filter} WHERE LOWER(concept.name) CONTAINS LOWER(x) OR LOWER(cm.name) CONTAINS LOWER(x) ) "+
//			We want to be sure to capture those concept maps that were created prior to the creator or isPublic properties
//			were added, or for concept maps who were made by anonymous users. For such concept maps these properties may be null.
			" AND ( cm.authorsAccountId = {accountId} OR cm.isPublic = true OR (cm.authorsAccountId IS NULL) OR (cm.isPublic IS NULL) "+
			" OR (cm.groupId IN {groupIds}) ) "+
			" OPTIONAL MATCH (cm:ConceptMap)-[:PARENTS]->(parents:Library)"+
			" RETURN DISTINCT cm as conceptMap, parents as parents"+
			" ORDER BY cm.versionDate DESC " +
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize"
			)
	public List<Map<String, Object>> findConceptMapArchive(
			@Param("filter") String[] filter, Pageable pageable, @Param("accountId") String accountId, @Param("groupIds") String[] groupIds);
	
	@Query(
			" MATCH (concept:Concept)-[:LIBRARY]->(library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap) "+
			" WHERE ALL (x IN {filter} WHERE LOWER(concept.name) CONTAINS LOWER(x) OR LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" AND cm.authorsAccountId = {accountId} "+
			" OPTIONAL MATCH (cm:ConceptMap)-[:PARENTS]->(parents:Library)"+
			" RETURN DISTINCT cm as conceptMap, parents as parents"+
			" ORDER BY cm.versionDate DESC " +
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize"
			)
	public List<Map<String, Object>> findConceptMapArchivesByAuthorAccountId(
			@Param("filter") String[] filter, Pageable pageable, @Param("accountId") String accountId);
	

	@Query( 
			" MATCH (concept:Concept)-[:LIBRARY]->(library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE ALL (x IN {filter} WHERE LOWER(concept.name) CONTAINS LOWER(x) OR LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" MATCH (cst:ConceptSemanticType)-[:LIBRARY]->(library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap) "+
			" WHERE ALL (x IN {filter} WHERE LOWER(cst.name) CONTAINS LOWER(x) OR LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" AND (cm.groupId IN {groupIds}) "+
			" OPTIONAL MATCH (cm:ConceptMap)-[:PARENTS]->(parents:Library)"+
			" RETURN DISTINCT cm as conceptMap, parents as parents"+
			" ORDER BY cm.versionDate DESC " +
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize"
			)
	public List<Map<String, Object>> findConceptMapArchivesByGroupId(
			@Param("filter") String[] filter, Pageable pageable, @Param("groupIds") String[] groupIds);
	
	
	@Query( " MATCH (cst:ConceptSemanticType)-[:LIBRARY]->(library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE ALL (x IN {filter} WHERE LOWER(cst.name) CONTAINS LOWER(x) OR LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" AND ( (cm.authorsAccountId = {accountId}) OR (cm.isPublic <> FALSE) )" +
			" RETURN count(DISTINCT cm)")
	public Long countAllConceptMapArchive(@Param("filter") String[] filter, @Param("accountId") String accountId);
	
	@Query( " MATCH (cst:ConceptSemanticType)-[:LIBRARY]->(library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE ALL (x IN {filter} WHERE LOWER(cst.name) CONTAINS LOWER(x) OR LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" AND ( cm.authorsAccountId = {accountId} ) " +
			" RETURN count(DISTINCT cm)")
	public Long countAuthoredConceptMapArchive(@Param("filter") String[] filter, @Param("accountId") String accountId);
	
	@Query( " MATCH (cst:ConceptSemanticType)-[:LIBRARY]->(library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE ALL (x IN {filter} WHERE LOWER(cst.name) CONTAINS LOWER(x) OR LOWER(cm.name) CONTAINS LOWER(x) ) "+
			" AND (cm.groupId IN {groupIds}) "+
			" RETURN count(DISTINCT cm)")
	public Long countSharedConceptMapArchive(@Param("filter") String[] filter, @Param("groupIds") String[] groupIds);
}
