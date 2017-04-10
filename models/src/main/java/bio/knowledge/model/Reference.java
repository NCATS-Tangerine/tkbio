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
package bio.knowledge.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;

import bio.knowledge.model.core.neo4j.Neo4jAnnotatedEntity;

/**
 * @author Richard
 *
 */
@NodeEntity(label="Reference")
public class Reference extends Neo4jAnnotatedEntity {

	private final String PUBMED_BASE_URI = "https://www.ncbi.nlm.nih.gov/pubmed/" ;
	
    private String pmid = "" ;

	private String issn = "" ;
  
	// Neo4j doesn't really support dates 
    // storing publication date as separate year, month and day
    private int year ;
    private int month;
    private int day;
    
    /**
     * Empty constructor for Reference.
     * Using the various identifier setters
     * may have the side effect of also
     * setting the URI and Accession Id of the reference.
     */
	public Reference() {}
	
	/**
	 * 
	 * @param pmid PubMed Identifier
	 */
	public Reference(String name) {
		super(name) ;
	}

	/**
	 * @return the PMID
	 */
	public void setPmid(String pmid) {
		this.pmid = pmid;
		setUri(PUBMED_BASE_URI+pmid);
		setAccessionId("pubmed:"+pmid);
	}

	/**
	 * @return the PMID
	 */
	public String getPmid() {
		return pmid;
	}

	/**
	 * @return the ISSN
	 */
	public String getIssn() {
		return issn;
	}

	/**
	 * @param ISSN the ISSN to set
	 */
	public void setIssn(String issn) {
		this.issn = issn;
	}

	
	public void setYearPublished(int year) {
		 this.year = year;
	}
	
	public void setMonthPublished(int month) {
		 this.month= month ;
	}
	public void  setDayPublished(int day) {
		 this.day = day;
	}
	
	
	/**
	 * @return the datePublished
	 */
	public int getYearPublished() {
		return  year;
	}
	
	public int getMonthPublished() {
		return  month;
	}
	public int getDayPublished() {
		return  day;
	}
	
	/**
	 * @param datePublished the datePublished to set
	 */
	public void setDatePublished(int year, int month, int dayOfMonth) {
		this.year  = year;
		this.month = month;
		this.day   = dayOfMonth;
	}

	@Transient
	private String publicationDate = "" ;
	
	// Setter not generally called?
	public  void setPublicationDate(String date) {
		this.publicationDate = date ;
	}
	
	private static final String[] monthAcronym = 
			new String[]{
				"Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
			} ;
	
	public String getPublicationDate() {
		if( year > 0 && month>=1 && month<=12 ) {
			if( publicationDate == null || publicationDate.isEmpty()) {
				// date not yet set? compose it!
				publicationDate = 
						new Integer(day).toString()+" "+
								monthAcronym[month-1] +" "+
						new Integer(year).toString() ;
			}
		} else {
			publicationDate = "Unknown";
		}
		return publicationDate ;
	}
 
	public String toString() { return getName() ; }
}
