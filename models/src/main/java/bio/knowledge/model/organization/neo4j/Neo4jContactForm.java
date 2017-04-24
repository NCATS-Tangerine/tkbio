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

package bio.knowledge.model.organization.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;
import bio.knowledge.model.organization.ContactForm;

@NodeEntity(label="ContactForm")
public class Neo4jContactForm 
	extends Neo4jAbstractIdentifiedEntity implements ContactForm {
	
	public Neo4jContactForm() {
		super() ;
	}
	
	public Neo4jContactForm(
			String username, 
			String name, 
			String email,
			String affiliation,
			String country,
			String subject,
			String message
			) {
		super(name,subject) ;
		this.username    = username ;
		this.email       = email ;
		this.affiliation = affiliation ;
		this.country     = country ;
		this.message     = message ;
	}


    /**
     */
    private String username;

    /**
     */
    private String email;

    /**
     */
    private String affiliation;

    /**
     * Simpler here to store the country name as a String
     */
    private String country;

    /**
     */
    private String message;

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#getEmail()
	 */
	@Override
	public String getEmail() {
        return this.email;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
        this.email = email;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#getAffiliation()
	 */
	@Override
	public String getAffiliation() {
        return this.affiliation;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#setAffiliation(java.lang.String)
	 */
	@Override
	public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#getCountry()
	 */
	@Override
	public String getCountry() {
        return this.country;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#setCountry(java.lang.String)
	 */
	@Override
	public void setCountry(String country) {
        this.country = country;
    }

	/* Subject is the description */
	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#getSubject()
	 */
	@Override
	public String getSubject() {
        return this.getDescription() ;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#setSubject(java.lang.String)
	 */
	@Override
	public void setSubject(String subject) {
        this.setDescription( subject );
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#getMessage()
	 */
	@Override
	public String getMessage() {
        return this.message;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.organization.ContactForm#setMessage(java.lang.String)
	 */
	@Override
	public void setMessage(String message) {
        this.message = message;
    }
}
