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
package bio.knowledge.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bio.knowledge.model.RdfUtil;
import bio.knowledge.model.Statement;
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.KBQuery.RelationSearchMode;
import bio.knowledge.service.StatementService;

/**
 * 
 * This class will expose REST API for predication related operations.
 * 
 * @author Chandan Mishra
 *
 */
@RestController
@RequestMapping("/api/predications")
public class PredicationRestController {

	@Autowired
	private StatementService statementService;

	@Autowired
	ConceptService conceptService;

	@Autowired
	KBQuery query;

	/**
	 * To get list of predications associated with given Concept id.
	 * 
	 * @param conceptId
	 *            The Concept id (Database Id)
	 * @param pageNum
	 *            Page Number to display starting from 1,2...n
	 * @param pageSize
	 *            number of entries per page
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/data/{conceptId}")
	public Iterable<Statement> getStatements(@PathVariable String conceptId,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		// create pageable object
		Pageable pageable = new PageRequest(pageNum - 1, pageSize);
		
		// fetch and set concept in kbquery
		query.setCurrentQueryConceptById( conceptId );
		
		/*
		 *  TODO: need to discriminate here between SemMedDb and non-SemMedDb conceptId's?
		 */
		if(RdfUtil.getQualifier(conceptId).isEmpty())
			/*
			 *  Assume that non-URI qualified id's are are a Long Neo4j id
			 *  of a SemMedDb recorded CST, for look up in main database?
			 */
			query.setRelationSearchMode(RelationSearchMode.RELATIONS) ;
		else
			// This is a non-SemMedDb node,
			// which must be resolved elsewhere
			// (i.e. in WikiData?)
			query.setRelationSearchMode(RelationSearchMode.WIKIDATA) ;
		
		return statementService.findAll(pageable);
	}

	/**
	 * To get total count of predication associated with given Concept id.
	 * 
	 * @param conceptId
	 *            The Concept id (Database Id)
	 * @return Size of predications
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/count/{conceptId}")
	public Long count(@PathVariable String conceptId) {
		// setting kbquery
		query.setCurrentQueryConceptById( conceptId );
		return statementService.countEntries();
	}

}
