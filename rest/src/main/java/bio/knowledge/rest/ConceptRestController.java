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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bio.knowledge.database.repository.ConceptRepository;

import bio.knowledge.model.Concept;
import bio.knowledge.service.AuthenticationState;


/**
 * @author Richard
 * @author Chandan Mishra
 *
 */
@RestController
@RequestMapping("/api/concept/")
public class ConceptRestController {
	
	@Autowired
	private ConceptRepository conceptRepository;

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Concept> getAll() {
		return conceptRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "{term}")
	public Iterable<Concept> getOne(@PathVariable String term,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Pageable pageable = new PageRequest(pageNum - 1, pageSize);
		return conceptRepository.findByNameLikeIgnoreCase(term, pageable);
	}

	/**
	 * Method to get all Concept matching given initial search term.
	 * 
	 * @param searchTerm
	 *            initial search term
	 * @param pageNum
	 *            Page Number to display starting from 1,2...n
	 * @param pageSize
	 *            number of entries per page
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "data/{searchTerm}")
	public Iterable<Concept> search(@PathVariable String searchTerm,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Pageable pageable = new PageRequest(pageNum - 1, pageSize);
		String SEPARATOR = " ";
		String[] words = { "" };

		if (searchTerm != null && !searchTerm.isEmpty()) {
			words = searchTerm.split(SEPARATOR);
		}

		// TODO: User authentication for REST.
		/**
		 * From findByInitialSearch() documentation:
		 * 
		 * "Right now accountId and groupIds are only being used to count the
		 * number of concept maps attached to the library that are visible to
		 * the user (i.e., either are public, created by the user, or shared
		 * with a group that the user belongs to)."
		 * 
		 * So with accountId and groupIds being set to null, only public concept
		 * maps will be counted.
		 */
		List<Concept> result = conceptRepository.findByInitialSearch(words, pageable, null, null);
		return result;
	}

	/**
	 * Method to get total number of Concept Matching given Initial search term.
	 * 
	 * @param searchTerm
	 *            initial search term
	 * @return count of total matching concept
	 */
	@RequestMapping(method = RequestMethod.GET, value = "count/{searchTerm}")
	public Long count(@PathVariable String searchTerm) {
		String SEPERATOR = " ";
		String[] words = { "" };

		if (searchTerm != null && !searchTerm.isEmpty()) {
			words = searchTerm.split(SEPERATOR);
		}
		return conceptRepository.countByInitialSearch(words);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Concept create(@RequestBody Concept concept) {
		return conceptRepository.save(concept);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "{id}")
	public void delete(@PathVariable Long id) {
		conceptRepository.delete(id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public Concept update(@PathVariable Long id, @RequestBody Concept concept) {
		Concept update = conceptRepository.findOne(id);
		update.setAccessionId(concept.getAccessionId());
		update.setName(concept.getName());
		update.setSemanticGroup(concept.getSemanticGroup());
		return conceptRepository.save(update);
	}
}
