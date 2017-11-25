package bio.knowledge.model;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Reference extends IdentifiedEntity {

	/**
	 * @return the PMID
	 */
	void setPmid(String pmid);

	/**
	 * @return the PMID
	 */
	String getPmid();

	/**
	 * @return the ISSN
	 */
	String getIssn();

	/**
	 * @param ISSN the ISSN to set
	 */
	void setIssn(String issn);

	void setYearPublished(int year);

	void setMonthPublished(int month);

	void setDayPublished(int day);

	/**
	 * @return the datePublished
	 */
	int getYearPublished();

	int getMonthPublished();

	int getDayPublished();

	/**
	 * @param datePublished the datePublished to set
	 */
	void setDatePublished(int year, int month, int dayOfMonth);

	// Setter not generally called?
	void setPublicationDate(String date);

	String getPublicationDate();

	String toString();

	String getUri();

	void setUri(String uri);

}