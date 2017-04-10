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

package bio.knowledge.datasource.wikidata;

/**
 * @author Richard
 *
 */
public interface WikiDataServicesDefinitions {

	public static final String WIKIDATA_SPARQL_ENDPOINT = "https://query.wikidata.org/sparql" ;

	public static final String WD_SDS_1_ID   = "WikiData_EntrezIdbyGeneSymbol" ;
	public static final String WD_SDS_1_NAME = "WikiData: first simple query by gene "
			+ "symbol string to retrieve NCBI gene identifiers" ;
	public static final String WD_SDS_1_SPARQL_QUERY    = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT ?entrezgene ?gene WHERE {\n"
			+ "   ?gene wdt:P353 ?input .      # P353 HGNC_Gene_Symbol\n"
			+ "   ?gene wdt:P351 ?entrezgene . # P351 Entrez Gene ID\n"
			+ "}" ;

	public static final String WD_SDS_2_ID   = "WikiData_AllPropertiesById" ;
	public static final String WD_SDS_2_NAME = "WikiData: second simple query by WikiData ID "
			+ "to retrieve all properties of a WikiData entry" ;
	public static final String WD_SDS_2_SPARQL_QUERY = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "SELECT distinct ?prop ?propLabel\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?input ?p ?value .\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language \"en\" .\n"
			+ "  }\n"
			+ "}\n" ;

	public static final String WD_SDS_3_ID   = "WikiData_GenePropertiesByGeneSymbol" ;
	public static final String WD_SDS_3_NAME = "WikiData: third simple query by WikiData ID "
			+ "to retrieve all properties of a WikiData entry associated with a gene" ;
	public static final String WD_SDS_3_SPARQL_QUERY = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT distinct ?prop ?propLabel ?any\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?gene wdt:P353 ?input .      # P353 HGNC_Gene_Symbol\n"
			+ "  ?gene wdt:P351 ?entrezID .   # P351 Entrez Gene ID\n"
			+ "  ?gene ?p ?any .\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language \"en\" .\n"
			+ "  }\n"
			+ "}\n" ;

	public static final String WD_SDS_3_COUNTING_ID   = "WikiData_CountGenePropertiesByGeneSymbol" ;
	public static final String WD_SDS_3_COUNTING_NAME = "WikiData: counting results of third simple query by WikiData ID "
			+ "to retrieve all properties of a WikiData entry associated with a gene" ;
	public static final String WD_SDS_3_COUNTING_SPARQL_QUERY = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT (COUNT(?propValue) AS ?count)\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?gene wdt:P353 ?input .      # P353 HGNC_Gene_Symbol\n"
			+ "  ?gene wdt:P351 ?entrezID .   # P351 Entrez Gene ID\n"
			+ "  ?gene ?p ?propValue .\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language \"en\" .\n"
			+ "  }\n"
			+ "}\n" ;

	public static final String WD_SDS_4_ID   = "WikiData_getFormatterURL" ;
	public static final String WD_SDS_4_NAME = "WikiData: retrieve the formatter URL associated with a WikiData property id" ;
	public static final String WD_SDS_4_SPARQL_QUERY    = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "SELECT ?url WHERE {\n"
			+ "   OPTIONAL {?input wdt:P1630 ?url } # P1630 Formatter URL\n"
			+ "   OPTIONAL {?input wdt:P1896 ?url } # P1896 source website for the property\n"
			+ "}" ;

	public static final String WD_SDS_5_ID   = "WikiData_AllPropertiesValuesById" ;
	public static final String WD_SDS_5_NAME = "WikiData: fifth simple query by WikiData ID "
			+ "to retrieve all property values of a WikiData entry" ;
	public static final String WD_SDS_5_SPARQL_QUERY = 
		   	  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "SELECT distinct ?prop ?propLabel ?value\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?input ?p ?value .\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language \"en\" .\n"
			+ "  }\n"
			+ "}\n" ;

	public static final String WD_SDS_6_ID   = "WikiData_WikiData_Item_English_Label" ;
	public static final String WD_SDS_6_NAME = "WikiData: sixth simple query by WikiData ID "
			+ "to retrieve the English label of a WikiData item" ;
	public static final String WD_SDS_6_SPARQL_QUERY = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT distinct ?name\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?input rdfs:label ?name filter (lang(?name) = \"en\") .\n"
			+ "}\n" ;

	public static final String WD_SDS_7_ID   = "About_WikiData_Item_Associated_Wikipedia_Article" ;
	public static final String WD_SDS_7_NAME = 
			"WikiData: simple query by WikiData ID to retrieve the link to "
			+ "an associated English language Wikipedia article" ;
	public static final String WD_SDS_7_SPARQL_QUERY = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX schema: <http://schema.org/>\n"
			+ "SELECT DISTINCT ?wikipedia_article_url\n"
			+ "WHERE {\n"
			+ "?wikipedia_article_url schema:about ?input .\n"
			+ "?wikipedia_article_url schema:inLanguage \"en\" .\n"
			+ "FILTER (SUBSTR(str(?wikipedia_article_url), 1, 25) = \"https://en.wikipedia.org/\") .\n"
			+ "}\n" ;

	public static final String WD_SDS_8_ID   = "WikiData_WikiData_Item_English_Label_&_Concept_Type" ;
	public static final String WD_SDS_8_NAME = "WikiData: eight simple query by WikiData ID "
											 + "to retrieve the English label and Concept Type of a WikiData item" ;
	public static final String WD_SDS_8_SPARQL_QUERY = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT distinct ?name ?value ?valueLabel ?p ?pLabel\n"
			+ "WHERE {\n"
			+ "   ?input rdfs:label ?name filter (lang(?name) = \"en\") ."
			+ "   OPTIONAL {\n"
			+ "      ?input ?p ?value .\n"
			+ "      wd:P279 wikibase:directClaim ?p .\n"
			+ "   }\n"
			+ "   OPTIONAL {\n"
			+ "      ?input ?p ?value .\n"
			+ "      wd:P31 wikibase:directClaim ?p .\n"
			+ "   }\n"
			+ "   SERVICE wikibase:label {\n"
			+ "		 bd:serviceParam wikibase:language \"en\" .\n"
			+ "   }\n"
			+ "}\n" ;

	public static final String WD_CDS_1_ID   = "WikiData_ComplexAllPropertiesById" ;
	public static final String WD_CDS_1_NAME = "WikiData: first complex query by WikiData ID "
			+ "to retrieve all properties of a WikiData entry "
			+ "assuming a given language to be specified" ;
	public static final String WD_CDS_1_SPARQL_QUERY  =  
		  	  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "SELECT distinct ?prop ?propLabel\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?input ?p ?any .\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language ?language .\n"
			+ "  }\n"
			+ "}\n" ;

	public static final String WD_CDS_3_ID   = "WikiData_PagedGenePropertiesByGeneSymbol" ;
	public static final String WD_CDS_3_NAME = "WikiData: third complex query by WikiData ID "
			+ "to retrieve a specified page of properties of a "
			+ "WikiData entry associated with a gene" ;
	public static final String WD_CDS_3_SPARQL_QUERY  = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT distinct ?prop ?propLabel ?propDescription ?propValue\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?gene wdt:P353 ?input .      # P353 HGNC_Gene_Symbol\n"
			+ "  ?gene wdt:P351 ?entrezID .   # P351 Entrez Gene ID\n"
			+ "  ?gene ?p ?propValue .\n"
			+ "  FILTER regex(?propValue, ?filter, \"i\")\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language \"en\" .\n"
			+ "  }\n"
			+ "}\n"
			+ "LIMIT ?limit\n"
			+ "OFFSET ?offset\n" ;

	public static final String WD_CDS_4_ID   = "WikiData_GenePropertiesByNcbiGeneIdentifier" ;
	public static final String WD_CDS_4_NAME = "WikiData: third simple query by NCBI Gene Identifier "
			+ "to retrieve gene annotation for the gene" ;
	public static final String WD_CDS_4_SPARQL_QUERY = 
			  "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT distinct ?prop ?propLabel ?value ?valueLabel\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?gene wdt:P351 ?geneid . # P351 Entrez Gene ID\n"
			+ "  ?gene ?p ?value .\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language \"en\" .\n"
			+ "  }\n"
			+ "}\n" ;

	public static final String WD_CDS_5_ID   = "WikiData_getItemDetails" ;
	public static final String WD_CDS_5_NAME = "WikiData: fifth complex query by WikiData ID "
			+ "to retrieve all property values of an arbitrary WikiData item" ;
	public static final String WD_CDS_5_SPARQL_QUERY = 
			  "PREFIX wd: <http://www.wikidata.org/entity/>\n"
			+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
			+ "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
			+ "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "SELECT distinct ?prop ?propLabel ?value ?valueLabel\n"
			+ "WHERE\n"
			+ "{\n"
			+ "  ?wikiDataId ?p ?value .\n"
			+ "  ?prop wikibase:directClaim ?p .\n"
			+ "  SERVICE wikibase:label {\n"
			+ "  	bd:serviceParam wikibase:language \"en\" .\n"
			+ "  }\n"
			+ "}\n" ;

	/**
	 * Retrieve English language Wikipedia link... 
	 * where ?input is a qualified Wikidata item object identifier
	 * 
	 *  PREFIX wd: <http://www.wikidata.org/entity/>
		SELECT distinct ?article
		WHERE
		{
		 	?article schema:about ?input .
		    ?article schema:inLanguage "en" .
		    FILTER (SUBSTR(str(?article), 1, 25) = "https://en.wikipedia.org/") . 
		}
	 */

}
