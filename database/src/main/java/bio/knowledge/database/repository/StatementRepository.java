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
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.model.neo4j.Neo4jGeneralStatement;

/**
 * @author Richard Bruskiewich
 * 
 * Adapted from StatementRepository code containing significant code contributions from
 * @author Chandan Mishra
 *
 */
public interface StatementRepository extends GraphRepository<Neo4jGeneralStatement> {

	/**
	 * @return
	 */
	@Query("MATCH (statement:Statement) RETURN statement")
	Iterable<Neo4jGeneralStatement> getStatements() ;

	/**
	 * 
	 */
	@Query( "DROP CONSTRAINT ON (statement:Statement)"
	      + " ASSERT statement.statementId IS UNIQUE")
	public void dropUniqueConstraintOnStatementId() ;
	
	/**
	 * 
	 */
	@Query( "DROP INDEX ON :Statement(statementId)")
	public void dropIndexOnStatementId() ;

	/**
	 * @author Chandan Mishra (original Predication model queries)
	 * @author Richard Bruskiewich (modifications to KB 3.0)
	 * 
	 * @param filter
	 * @return
	 */
	@Query( "MATCH (seedConcept:Concept)<-[:SUBJECT|:OBJECT]-(statement:Statement) WHERE id(seedConcept) = {conceptId}"+
			" WITH statement,seedConcept"+
			" MATCH (subject:Concept)<-[:SUBJECT]-(statement)-[:OBJECT]->(object:Concept)"+
			"   WHERE ("+
			"     size({conceptTypeFilter}) = 0"+
			"     OR ANY ( x IN {conceptTypeFilter} WHERE id(subject) <> id(seedConcept) AND UPPER(subject.semanticGroup) CONTAINS UPPER(x)  )"+
			"     OR ANY ( x IN {conceptTypeFilter} WHERE id(object)  <> id(seedConcept) AND UPPER(object.semanticGroup)  CONTAINS UPPER(x) )"+
			"   )"+
			" WITH  statement, subject, object "+
			" MATCH (relation:Predicate)<-[:RELATION]-(statement)-[:EVIDENCE]->(evidence:Evidence)"+
			" WHERE ALL (x IN {filter} WHERE LOWER(subject.name)  CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(object.name)   CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(relation.name) CONTAINS LOWER(x))"+
			" OR toString(evidence.count) IN {filter}"+
			" RETURN count( DISTINCT statement)"
			)
	long countByNameLikeIgnoreCase(@Param("conceptId") Neo4jConcept concept, @Param("filter") String[] filter, @Param("conceptTypeFilter") ArrayList<String> conceptTypeFilter);
	
	/**
	 * @author Chandan Mishra (original Predication model queries)
	 * @author Richard Bruskiewich (modifications to KB 3.0)
	 * 
	 * @param currentConcept 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query(
	"MATCH (concept:Concept) WHERE LOWER(concept.name) CONTAINS LOWER({filter})"+
	" MATCH (statement:Statement)-[:SUBJECT]->(concept)"+
	" RETURN statement"+
	" SKIP  {1}.pageNumber*{1}.pageSize"+
	" LIMIT {1}.pageSize"+
	" UNION"+
	" MATCH (concept:Concept) WHERE LOWER(concept.name) CONTAINS LOWER({filter})"+
	" MATCH (statement:Statement)-[:OBJECT] ->(concept)"+
	" RETURN statement"+
	" SKIP  {1}.pageNumber*{1}.pageSize"+
	" LIMIT {1}.pageSize" 
	)
	List<Neo4jGeneralStatement> findByNameLikeIgnoreCase( @Param("conceptId") Optional<Neo4jConcept> currentConcept, @Param("filter") String filter, Pageable pageable);

	/**
	 * @author Chandan Mishra
	 * @author Richard Bruskiewich (modifications to KB 3.0)
	 * 
	 * Method to get statement, subject and object associated with given concept.
	 * 
	 * @param concept
	 * @param conceptTypeFilter
	 * @param pageable
	 * @return List of map with key and value pair
	 */
	@Query(  "MATCH (seedConcept:Concept)<-[:SUBJECT|:OBJECT]-(statement:Statement) WHERE id(seedConcept) = {conceptId}"+
			" WITH statement, seedConcept"+
			" MATCH (subject:Concept)<-[:SUBJECT]-(statement)-[:OBJECT]->(object:Concept)"+
			"   WHERE ("+
			"     size({conceptTypeFilter}) = 0"+
			"     OR ANY ( x IN {conceptTypeFilter} WHERE id(subject) <> id(seedConcept) AND UPPER(subject.semanticGroup) CONTAINS UPPER(x)  )"+
			"     OR ANY ( x IN {conceptTypeFilter} WHERE id(object)  <> id(seedConcept) AND UPPER(object.semanticGroup)  CONTAINS UPPER(x) )"+
			"   )"+
			" WITH statement,subject,object"+
			" MATCH (relation:Predicate)<-[:RELATION]-(statement)-[:EVIDENCE]->(evidence:Evidence)"+
			" RETURN statement as statement, subject as subject, relation as relation, object as object, evidence as evidence,"+
			" CASE {2}['sort'][0]['property']" +
				" WHEN  'subject'   THEN subject.name"+
				" WHEN  'object'    THEN object.name"+
				" WHEN  'relation' THEN relation.name"+
				" WHEN  'evidence'  THEN evidence.count"+
				" ELSE  evidence.count END as byWhichProperty"+
			" ORDER BY CASE {2}['sort'][0]['direction'] "+
				" WHEN 'ASC'  THEN byWhichProperty  "+
				" END ASC,"+
				" CASE {2}['sort'][0]['direction']" +
				" WHEN 'DESC' then byWhichProperty "+
				" END DESC"+
			" SKIP  {2}.pageNumber*{2}.pageSize"+
			" LIMIT {2}.pageSize"
			)
	List<Map<String, Object>> findByConcept( @Param("conceptId") Neo4jConcept concept, @Param("conceptTypeFilter") ArrayList<String> conceptTypeFilter, Pageable pageable);

	/**
	 * @author Chandan Mishra (original Predication model queries)
	 * @author Richard Bruskiewich (modifications to KB 3.0)
	 * 
	 * @param concept
	 * @return long count of Statement relations with the given concept as either a subject or object
	 * 
	 * TODO Review if this method needs the conceptTypeFilter and should it be implemented.
	 */
	@Query(
			 "MATCH (seedConcept:Concept)<-[:SUBJECT|:OBJECT]-(statement:Statement) WHERE id(seedConcept) = {conceptId}"+
			" WITH statement"+
			" MATCH (subject:Concept)<-[:SUBJECT]-(statement)-[:OBJECT]->(object:Concept)"+
			" return count(DISTINCT statement)"
			)
	Long countByConcept( 
			 @Param("conceptId") Neo4jConcept concept, 
			 @Param("conceptTypeFilter") ArrayList<String> conceptTypeFilter
	);
	
	/**
	 * @author Chandan Mishra (original Predication model queries)
	 * @author Richard Bruskiewich (modifications to KB 3.0)
	 * 
	 * Method to get statement, subject and object associated with given concept and given filter
	 *
	 * @param concept
	 * @param filter : Search filter
	 * @param pageable
	 * @return List of map with key and value pair
	 */
	@Query(  "MATCH (seedConcept:Concept)<-[:SUBJECT|:OBJECT]-(statement:Statement) WHERE id(seedConcept) = {conceptId}"+
			" WITH statement, seedConcept"+
			" MATCH (subject:Concept)<-[:SUBJECT]-(statement)-[:OBJECT]->(object:Concept)"+
			"   WHERE ("+
			"     size({conceptTypeFilter}) = 0"+
			"     OR ANY ( x IN {conceptTypeFilter} WHERE id(subject) <> id(seedConcept) AND UPPER(subject.semanticGroup) CONTAINS UPPER(x)  )"+
			"     OR ANY ( x IN {conceptTypeFilter} WHERE id(object)  <> id(seedConcept) AND UPPER(object.semanticGroup)  CONTAINS UPPER(x) )"+
			"   )"+
			" WITH statement,subject,object"+
			" MATCH (relation:Predicate)<-[:RELATION]-(statement)-[:EVIDENCE]->(evidence:Evidence)"+
			" WHERE ALL (x IN {filter} WHERE LOWER(subject.name)  CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(object.name)   CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(relation.name) CONTAINS LOWER(x))"+
			" OR toString(evidence.count) IN {filter}"+
			" return DISTINCT statement as statement, subject as subject, relation as relation, object as object, evidence as evidence,"+
			" CASE {3}['sort'][0]['property']" +
				" WHEN  'subject'   THEN subject.name"+
				" WHEN  'object'    THEN object.name"+
				" WHEN  'relation'  THEN relation.name"+
				" WHEN  'evidence'  THEN evidence.count"+
				" ELSE  evidence.count END as byWhichProperty"+
			" ORDER BY CASE {3}['sort'][0]['direction'] "+
				" WHEN 'ASC'  then byWhichProperty  "+
				" END ASC,"+
				" CASE {3}['sort'][0]['direction']" +
				" WHEN 'DESC' then byWhichProperty "+
				" END DESC"+
			" SKIP  {3}.pageNumber*{3}.pageSize"+
			" LIMIT {3}.pageSize"
			)
	List<Map<String, Object>> findByConceptFiltered( 
			@Param("conceptId") Neo4jConcept concept, 
			@Param("conceptTypeFilter") ArrayList<String> conceptTypeFilter,
			@Param("filter")String[] filter, 
			Pageable pageable
	);
	
	@Query( "MATCH (relation:Predicate)<-[:RELATION]-(statement:Statement)-[:EVIDENCE]->(evidence:Evidence)-[:ANNOTATION]->(annotation:Annotation)-[:REFERENCE]->(reference:Reference) "+
			" WHERE reference.accessionId = \"pmid:\"+{pmid} "+
			" WITH relation as relation, statement as statement, evidence as evidence"+
			" MATCH (subject:Concept)<-[:SUBJECT]-(statement)-[:OBJECT]->(object:Concept)"+
			" WHERE  ("+
						" size({conceptTypeFilter}) = 0"+
						" OR ANY ( x IN {conceptTypeFilter} WHERE UPPER(subject.semanticGroup) CONTAINS UPPER(x) )"+
						" OR ANY ( x IN {conceptTypeFilter} WHERE UPPER(object.semanticGroup)  CONTAINS UPPER(x) )"+
					 ")"+
			" WITH DISTINCT statement as statement, subject as subject, relation as relation, object as object, evidence as evidence"+
			" WHERE ALL (x IN {filter} WHERE LOWER(subject.name)  CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(object.name)   CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(relation.name) CONTAINS LOWER(x))"+
			" OR toString(evidence.count) IN {filter}"+
			" return DISTINCT statement as statement, subject as subject, relation as relation, object as object, evidence as evidence,"+
			" CASE {3}['sort'][0]['property']" +
				" WHEN  'subject'   THEN subject.name"+
				" WHEN  'object'    THEN object.name"+
				" WHEN  'relation'  THEN relation.name"+
				" WHEN  'evidence'  THEN evidence.count"+
				" ELSE  evidence.count END as byWhichProperty"+
			" ORDER BY CASE {3}['sort'][0]['direction'] "+
				" WHEN 'ASC'  then byWhichProperty  "+
				" END ASC,"+
				" CASE {3}['sort'][0]['direction']" +
				" WHEN 'DESC' then byWhichProperty "+
				" END DESC"+
			" SKIP  {3}.pageNumber*{3}.pageSize"+
			" LIMIT {3}.pageSize"
			)
	List<Map<String, Object>> findByPMID( 
			@Param("pmid") String PMID, 
			@Param("conceptTypeFilter") ArrayList<String> conceptTypeFilter,
			@Param("filter")String[] filter, 
			Pageable pageable
	);
	
	/**
	 * @author Chandan Mishra (original Predication model queries)
	 * @author Richard Bruskiewich (modifications to KB 3.0)
	 * 
	 * @param filter
	 * @return
	 */
	@Query( "MATCH (relation:Predicate)<-[:RELATION]-(statement:Statement)-[:EVIDENCE]->(evidence:Evidence)-[:ANNOTATION]->(annotation:Annotation)-[:REFERENCE]->(reference:Reference) "+
			" WHERE reference.accessionId = \"pmid:\"+{pmid} "+
			" MATCH (subject:Concept)<-[:SUBJECT]-(statement)-[:OBJECT]->(object:Concept)"+
			" WHERE  ("+
						" size({conceptTypeFilter}) = 0"+
						" OR ANY ( x IN {conceptTypeFilter} WHERE UPPER(subject.semanticGroup) CONTAINS UPPER(x) )"+
						" OR ANY ( x IN {conceptTypeFilter} WHERE UPPER(object.semanticGroup)  CONTAINS UPPER(x) )"+
					 ")"+
			" WITH DISTINCT statement as statement, subject as subject, object as object, evidence as evidence"+
			" WHERE ALL (x IN {filter} WHERE LOWER(subject.name)        CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(object.name)         CONTAINS LOWER(x))"+
			   " OR ALL (x IN {filter} WHERE LOWER(statement.predicate) CONTAINS LOWER(x))"+
			" OR toString(evidence.count) IN {filter}"+
			" RETURN count(DISTINCT statement)"
			)
	Long countByPMID(
			@Param("pmid") String PMID, 
			@Param("conceptTypeFilter") ArrayList<String> conceptTypeFilter, 
			@Param("filter") String[] filter
	);
	
	
	/**
	 * @author Chandan Mishra (original Predication model queries)
	 * @author Richard Bruskiewich (modifications to KB 3.0)
	 * 
	 * @param sourceCstId - Source accessionId
	 * 		  targetCstId - target accessionId
	 *        predicate   - predicate name
	 *        
	 * @return matching Statement
	 */
	@Query(   "MATCH ( subject:Concept {accessionId : {source}} )<-[:SUBJECT]-( statement:Statement )-[:OBJECT]->( object:Concept {accessionId : {target}} ) "
			+ "WITH statement AS statement, subject AS subject, object AS object "
			   + "MATCH ( relation:Predicate { name: {relation}} )<-[:RELATION]-( statement:Statement )-[:EVIDENCE]->( evidence:Evidence ) "
			+ "RETURN DISTINCT "
				+ "statement AS statement, "
				+ "subject   AS subject, "
				+ "relation  AS relation, "
				+ "object    AS object, "
				+ "evidence  AS evidence "
			+ "LIMIT 1")
	List<Map<String, Object>> findBySourceTargetAndRelation( 
			@Param("source")   String sourceId, 
			@Param("target")   String targetId, 
			@Param("relation") String relation
	) ;
	
	@Query(
			" MATCH (concept:Concept)<-[:SUBJECT|:OBJECT]-(statement:Statement) " + 
			" WHERE ANY(x in {curieIds} WHERE LOWER(concept.accessionId) = LOWER(x)) " +
			" WITH statement as statement, ID(concept) as id " +
			
			" MATCH (subject:Concept)<-[:SUBJECT]-(statement)-[:OBJECT]->(object:Concept) " +
			" WHERE (ID(subject) = id OR ID(object) = id) AND " +
			"    {semanticGroups} IS NULL OR SIZE({semanticGroups}) = 0 OR " +
			"    ANY (x IN {semanticGroups} WHERE ( " +
			"       LOWER(object.semanticGroup)  CONTAINS LOWER(x) OR " +
			"       LOWER(subject.semanticGroup) CONTAINS LOWER(x) " +
			"    )) " +
			" WITH statement, subject, object " +
			
			" MATCH (relation:Predicate)<-[:RELATION]-(statement)-[:EVIDENCE]->(evidence:Evidence) " +
			" WHERE {filter} IS NULL OR SIZE({filter}) = 0 " +
			" OR ANY (x IN {filter} WHERE ( " +
			"    LOWER(object.name)   CONTAINS LOWER(x) OR " +
			"    LOWER(subject.name)  CONTAINS LOWER(x) OR " +
			"    LOWER(relation.name) CONTAINS LOWER(x) " +
			" )) " +
			" RETURN DISTINCT statement as statement, subject as subject, relation as relation, object as object, evidence as evidence" +
			" ORDER BY evidence.count DESC " +
			" SKIP ({pageNumber} - 1) * {pageSize} " +
			" LIMIT {pageSize} "
	)
	List<Map<String, Object>> apiFindById(
			@Param("curieIds") String[] curieIds,
			@Param("filter") String[] filter,
			@Param("semanticGroups") String[] semanticGroups,
			@Param("pageNumber") Integer pageNumber,
			@Param("pageSize") Integer pageSize
	);
}
