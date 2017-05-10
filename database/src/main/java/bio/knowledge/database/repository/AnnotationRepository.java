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

import bio.knowledge.model.Annotation;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.Reference;
import bio.knowledge.model.neo4j.Neo4jAnnotation;

/**
 * @author Richard
 *
 */
public interface AnnotationRepository extends GraphRepository<Annotation> {

	/**
	 * @return
	 */
	@Query("MATCH (annotation:Annotation) RETURN annotation")
	List<Neo4jAnnotation> getAnnotations() ;
	
	/**
	 * @param reference
	 * @return unique Annotation associated with Reference(?)
	 */
	@Query( "MATCH (reference:Reference) WHERE id(reference) = {referenceId}"
         + " MATCH (annotation:Annotation)-[:REFERENCE]->(reference)"
         + " RETURN annotation" 
    )
	Neo4jAnnotation findByReference( @Param("referenceId") Reference reference );

	/**
	 * 
	 */
	@Query( "DROP CONSTRAINT ON (annotation:Annotation)"
		      + " ASSERT annotation.annotationId IS UNIQUE")
	void dropUniqueConstraintOnAnnotationAnnotationId();

	/**
	 * 
	 */
	@Query( "DROP INDEX ON :Annotation(annotationId)")
	void dropIndexOnAnnotationAnnotationId();

	/**
	 * 
	 * @param id of Annotation
	 * @return matching Annotation
	 */
	@Query( "MATCH ( annotation:Annotation { accessionId:{accessionId} } ) RETURN annotation")
	Neo4jAnnotation findById(  @Param("accessionId") String accessionId ) ;
	
	
	@Query( "MATCH (annotation:Annotation { accessionId:{accessionId} } )-[:REFERENCE]->(reference:Reference) RETURN reference LIMIT 1")
	Reference findReferenceByAnnotation(  @Param("accessionId") String accessionId ) ;

	/**
	 * @param filter
	 * @return
	 */
	@Query("MATCH (annotation:Annotation)"
			+ " WHERE LOWER(annotation.annotation) CONTAINS LOWER({filter})"
			+ " RETURN count(annotation)"
		)
	long countByNameLikeIgnoreCase( @Param("filter") String filter);
	
	/**
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query(
			"MATCH (annotation:Annotation)"
			+ " WHERE LOWER(annotation.annotation) CONTAINS LOWER({filter}) OR"
			+ " LOWER(annotation.description) CONTAINS LOWER({filter})"
			+ " RETURN annotation"
			+ " SKIP  {1}.pageNumber*{1}.pageSize"
			+ " LIMIT {1}.pageSize" 
		)
	List<Neo4jAnnotation> findByNameLikeIgnoreCase( @Param("filter") String filter, Pageable pageable);
	
	@Query( 
			" MATCH (evidence:Evidence) WHERE id(evidence) = {evidenceId}"+
			" MATCH (evidence)-[:ANNOTATION]->(annotation:Annotation)-[:REFERENCE]->(reference:Reference)"+ 
			" WHERE annotation.userId = {userId} AND annotation.visible = false"+
			" OR annotation.visible = true"+
			" OR annotation.visible IS NULL AND annotation.userId IS NULL"+
			" RETURN annotation as annotation, reference as reference, (reference.year*365+reference.month*31+reference.day) as publicationDate"+
			" ORDER BY CASE {1}['sort'][0]['direction'] "+
			" WHEN 'ASC' THEN publicationDate"+
			" END ASC,"+
			" CASE {1}['sort'][0]['direction']" +
			" WHEN 'DESC' THEN publicationDate "+
			" END DESC"+			
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize"
		  )
	List<Map<String, Object>> findByEvidence( @Param("evidenceId") Evidence evidence, Pageable pageable, @Param("userId") String userId);
	
	
	@Query( 
			 "MATCH (evidence:Evidence) WHERE id(evidence) = {evidenceId}"+
			" MATCH (evidence)-[:ANNOTATION]->(annotation:Annotation)-[:REFERENCE]->(reference:Reference)"+
			" WHERE annotation.userId = {userId} AND annotation.visible = false"+
			" OR annotation.visible = true"+
			" OR annotation.visible IS NULL AND annotation.userId IS NULL"+
			" AND LOWER(annotation.annotation) CONTAINS LOWER({filter}) OR" +
			      " LOWER(annotation.description) CONTAINS LOWER({filter})"	+
			" RETURN annotation as annotation, reference as reference, (reference.year*365+reference.month*31+reference.day) as publicationDate"+
			" ORDER BY  CASE {2}['sort'][0]['direction'] "+
			" WHEN 'ASC' THEN publicationDate"+
			" END ASC,"+
			" CASE {2}['sort'][0]['direction']" +
			" WHEN 'DESC' THEN publicationDate"+
			" END DESC"+
			" SKIP  {2}.pageNumber*{2}.pageSize"+
			" LIMIT {2}.pageSize" 
		  )
	List<Map<String, Object>> findByEvidenceFiltered( @Param("evidenceId") Evidence evidence, @Param("filter") String filter, @Param("pageable") Pageable pageable, @Param("userId") String userId);
	
	/**
	 * @param evidence
	 * @return
	 */
	@Query( 
			 "MATCH (evidence:Evidence) WHERE id(evidence) = {evidenceId}"+
			" MATCH (evidence)-[:ANNOTATION]->(annotation:Annotation)"+
		    " RETURN count(annotation)"
		  )
	long countByEvidence( @Param("evidenceId") Evidence evidence);
	
	@Query( 
			 "MATCH (evidence:Evidence) WHERE id(evidence) = {evidenceId}"+
			" MATCH (evidence)-[:ANNOTATION]->(annotation:Annotation)"+
			" WHERE LOWER(annotation.name) CONTAINS LOWER({filter}) OR" +
			" LOWER(annotation.description) CONTAINS LOWER({filter})"	+
		    " RETURN count(annotation)"
		  )
	long countByEvidenceFiltered( @Param("evidenceId") Evidence evidence, @Param("filter")String filter);
	
	@Query( "MATCH (annotation:Annotation)-[:REFERENCE]->(reference:Reference) WHERE reference.pmid = {pmid}"+
			" RETURN annotation as annotation, reference as reference,(reference.year*365+reference.month*31+reference.day) as publicationDate"+
			" ORDER BY  CASE {1}['sort'][0]['direction'] "+
			" WHEN 'ASC' THEN publicationDate"+
			" END ASC,"+
			" CASE {1}['sort'][0]['direction']" +
			" WHEN 'DESC' THEN publicationDate "+
			" END DESC"+			
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize" 
		  )
	List<Map<String, Object>> findByPMID( @Param("pmid") String pmid, Pageable pageable);
	
	
	@Query( "MATCH (annotation:Annotation)-[:REFERENCE]->(reference:Reference) WHERE reference.pmid = {pmid}"+
			" RETURN count(annotation)"
		  )
	Long countByPMID( @Param("pmid") String pmid);
	
	
	@Query( "MATCH (annotation:Annotation)-[:REFERENCE]->(reference:Reference) WHERE reference.pmid = {pmid}"+
			" AND (LOWER(annotation.annotation) CONTAINS LOWER({filter}) OR" +
			" LOWER(annotation.description) CONTAINS LOWER({filter}))"	+
			" RETURN annotation as annotation, reference as reference,(reference.year*365+reference.month*31+reference.day) as publicationDate"+
			" ORDER BY  CASE {2}['sort'][0]['direction'] "+
			" WHEN 'ASC' THEN publicationDate"+
			" END ASC,"+
			" CASE {2}['sort'][0]['direction']" +
			" WHEN 'DESC' THEN publicationDate "+
			" END DESC"+			
			" SKIP  {2}.pageNumber*{2}.pageSize"+
			" LIMIT {2}.pageSize" 
		  )
	List<Map<String, Object>> findByPMIDFiltered( @Param("pmid") String pmid, @Param("filter")String filter, Pageable pageable);
	
	
	@Query( "MATCH (annotation:Annotation)-[:REFERENCE]->(reference:Reference) WHERE reference.pmid = {pmid}"+
			" AND (LOWER(annotation.annotation) CONTAINS LOWER({filter}) OR" +
			" LOWER(annotation.description) CONTAINS LOWER({filter}))"	+
			" RETURN count(annotation)"
		  )
	Long countByPMIDFiltered( @Param("pmid") String pmid, @Param("filter")String filter);
}