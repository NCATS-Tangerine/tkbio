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

package bio.knowledge.service.organization.neo4j;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.organization.Neo4jContactFormRepository;
import bio.knowledge.model.organization.ContactForm;
import bio.knowledge.model.organization.neo4j.Neo4jContactForm;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.organization.ContactFormService;

@Service("ContactFormService")
public class Neo4jContactFormService 
	extends IdentifiedEntityServiceImpl<ContactForm> 
	implements ContactFormService {
	
	@Autowired
	private Neo4jContactFormRepository contactFormRepository ;

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	public ContactForm createInstance(Object... arg0) {
		// See recordContactForm() below instead...
		return new Neo4jContactForm();
	}
	
 	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#findAll(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<ContactForm> findByNameLike(String filter, Pageable pageable){
		return (Page<ContactForm>)(Page)new PageImpl<Neo4jContactForm>( contactFormRepository.findByNameLikeIgnoreCase(filter, pageable ) );
	}
	
    private List<ContactForm> forms = new ArrayList<ContactForm>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
    private Stream<ContactForm> getContactFormStream() {
    	List<ContactForm> forms = new ArrayList<ContactForm>() ;
    	// 
    	// SDN 4.0 Doesn't appear to instantiate a GraphDatabaseService instance?
    	//
    	// Accessing a collection from the repository 
    	// needs to be explicitly wrapped in a Neo4j Transaction?
    	// see http://stackoverflow.com/questions/11485090/org-neo4j-graphdb-notintransactionexception
    	//try (Transaction tx = graphDb.beginTx()) {
	    	for(ContactForm c : contactFormRepository.getContactForms()) {
	    		forms.add(c) ;
	    	}
	    //	tx.success() ;
    	//}
    	return forms.stream() ;
    }
    
    public List<ContactForm> getContactForms() {
    	if(forms.isEmpty()) {
    		forms = getContactFormStream().sorted().collect(toList()) ;
    	}
    	return forms ;
    }

    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<ContactForm> getIdentifiers() {
		return getContactForms();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<ContactForm> getIdentifiers(Pageable pageable) {
		return (Page<ContactForm>)(Page)contactFormRepository.findAll(pageable) ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#countEntries()
	 */
	@Override
	public long countEntries() {
		return contactFormRepository.count() ;
	}
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ContactForm> getContactMessages() {
		return (List<ContactForm>)(List)contactFormRepository.findAll(new Sort(Direction.ASC,"name")) ;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<ContactForm> findAll(Pageable pageable) {
		return (Page<ContactForm>)(Page)contactFormRepository.findAll(pageable);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.general.IdentifiedEntityService#countHits(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		return contactFormRepository.countByNameLikeIgnoreCase(filter);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.organization.ContactFormService#recordContactForm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ContactForm recordContactForm(
			String username,
			String name,
			String email,
			String affiliation,
			String client_country,
			String subject,
			String message
	) {
		ContactForm form = 
				new Neo4jContactForm(
						username, 
						name, 
						email, 
						affiliation, 
						client_country, 
						subject, 
						message
				) ;
		
		return contactFormRepository.save((Neo4jContactForm)form) ;
	}

}
