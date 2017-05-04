/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software",false), to deal
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

import bio.knowledge.model.DomainModelException;

public enum ConceptDescriptor {

	// MyGene.Info tags
	ALIAS("alias", "Alias",false,false),
	DESCRIPTION("description", "Description",false,false),
	GENEID("ncbiGene", "Gene ID",false,false),
	HGNC("HGNC", "HGNC",false,false),
	MAPLOC("map_location", "Map Location",false,false),
	MIM("MIM", "MIM",false,false),
	SYMBOL("symbol", "Symbol",false,false),
	UNIGENE("unigene", "Unigene",false,false),
	UNIPROT("uniprot", "Uniprot",false,false),
	TAXID("taxid", "Tax ID",false,false),

	// skip rendering this field for now
//	PATHWAY("pathway", "Pathway",false),

	// these are already covered by DescriptionBuilder and selectedConcept
//	NAME("name", "Name",false),
//	SUMMARY("summary", "Summary",false),
//	Type("type_of_gene", "Gene Type",false),
	
	// WikiData property tags - no point yet in making them retrievable?
	P31("instance of","instance of",false,true),
	P279("subclass of","Subclass Of",false,true),

	/* Taxon associated property tags */
	
	P1843("taxon common name","Taxon Common Name",false,false),
	P171("parent taxon","Parent Taxon",true,true),
	P105("taxon rank","Taxon Rank",false,false),
	P685("NCBI Taxonomy ID","NCBI Taxonomy ID",true,false),
	P627("IUCN-ID","IUCN-ID",true,false),
	// P("GND ID","GND ID",true,false),
	// P("said to be the same as","",false,false),
	// P("ITIS TSN","ITIS TSN",false,false),
	// P("Dyntaxa ID","Dyntaxa ID",false,false),
	// P("minimum frequency of audible sound","",false,false),
	// P("topic's main category","",false,false),
	// P("New Zealand Organisms Register ID","",false,false),
	// P("Commons gallery","",false,false),
	// P("Encyclopædia Universalis Online ID","",false,false),
	// P("NE.se ID","",false,false),
	P830("Encyclopedia of Life ID","Encyclopedia of Life ID",true,false),
	P846("Global Biodiversity Information Facility ID","GBIF ID",true,false),
	// P("temporal range start","",false,false),
	// P("taxonomic type","Taxonomic Type",false,false),
	// P("taxon name","Taxon Name",false,false),
	// P("exact match","",false,false),
	// P("Catalogue of Life in Taiwan ID","",false,false),
	// P("AAT ID","",false,false),
	// P("heart rate","",false,false),
	// P("NBN System Key","",false,false),
	// P("taxon range map image","",false,false),
	// P("Commons category","",false,false),
	// P("MSW ID","MSW ID",false,false),
	// P("gait","",false,false),
	P3031("EPPO Code","EPPO Code",true,false),
	// P("Fossilworks ID","",false,false),
	// P("IUCN conservation status","",false,false),
	P1417("Encyclopædia Britannica Online ID","Encyclopædia Britannica Online ID",true,false),

	
	// TODO: Maybe need to merge this with the WikiDataPropertySemanticType enum
	
	P351("Entrez Gene ID","Entrez Gene ID",true,false),
	P703("found in taxon","Found in Taxon",true,true),
	P353("HGNC gene symbol","HGNC Gene Symbol",true,false),
	P354("HGNC ID","HGNC ID",true,false),
	P492("OMIM ID","OMIM ID",true,false),
	P1057("chromosome","Chromosome",true,true),
	P2548("strand orientation","Strand Orientation",false,false),
	P644("genomic start","Genomic Start",false,false),
	P645("genomic end","Genomic End",false,false),
	P594("Ensembl Gene ID","Ensembl Gene ID",true,false),
	P704("Ensembl Transcript ID","Ensembl Transcript ID",true,false),
	P639("RefSeq RNA ID","RefSeq RNA ID",true,false),
	P688("encodes","Encodes",true,true),
	P593("HomoloGene ID","HomoloGene ID",true,false),
	P684("ortholog","Ortholog",true,true),
	P686("Gene Ontology ID","Gene Ontology ID",true,false),
	P1916("gene substitution association with","Gene Substitution Association With",true,false),
	P692("Gene Atlas Image","Gene Atlas Image",true,false),
	//P646("Freebase ID","Freebase ID",false,false),
	P18("image","Image",true,false),

	/* Protein WikiData item */
	P352("UniProt ID","UniProt ID",true,false),
	//P373("Commons category","Commons Category",false,false),
	P527("has part","Has Part",true,true),
	P591("EC number","EC number",true,false),
	P637("RefSeq Protein ID","RefSeq Protein ID",true,false),
	P638("PDB ID","PDB ID",true,false),
	P680("molecular function","Molecular Function",true,true),
	P681("cell component","Cell Component",true,true),
	P682("biological process","Biological Process",true,true),
	P702("encoded by","Encoded By",true,true),
	P705("Ensembl Protein ID","Ensembl Protein ID",true,false),
		
	// Disease WikiData Item property tags
	
	P2892("UMLS CUI","UMLS CUI",true,false),
	P1461("Patientplus ID","Patientplus ID",true,false),
	P1995("medical specialty","Medical Specialty",true,true),
	P557("DiseasesDB","DiseasesDB",true,false),
	P668("GeneReviews ID","GeneReviews ID",true,false),
	P486("MeSH ID","MeSH ID",true,false),
	P493("ICD-9","ICD-9",true,false),
	P494("ICD-10","ICD-10",true,false),
	P604("MedlinePlus ID","MedlinePlus ID",true,false),
	P1748("NCI Thesaurus ID","NCI Thesaurus ID",true,false),
	P673("eMedicine","eMedicine",true,false),
	P699("Disease Ontology ID","Disease Ontology ID",true,false),
	
	// P2959("permanent duplicated item","",false,false),
	// P2888("exact match","",false,false),

	P2176("drug used for treatment","Drug Used for Treatment",true,true),
	
	// Chemical Item WikiData property tags?
	
	P2275("World Health Organisation International Nonproprietary Name","WHO Designation",false,false),
	P2175("medical condition treated","Medical Condition(s) Treated",true,true),
	P636("route of administration","Route of Administration",true,true),
	P2067("mass","Mass",false,false),
	P274("chemical formula","Chemical Formula",false,false),
	P665("KEGG ID","KEGG ID",true,false),
	P231("CAS registry number","CAS registry number",true,false),
	P267("ATC code","ATC code",true,false),
	P2115("NDF-RT ID","NDF-RT ID",true,false),
	P715("Drugbank ID","Drugbank ID",true,false),
	P662("PubChem ID (CID)","PubChem ID (CID)",true,false),
	P235("InChIKey","InChIKey",true,false),
	P592("ChEMBL ID","ChEMBL ID",true,false),
	P2566("ECHA InfoCard ID","ECHA InfoCard ID",true,false),
	P652("UNII","UNII",true,false),
	P661("ChemSpider ID","ChemSpider ID",true,false),
	
 	// P646("Freebase ID","Freebase ID",true,false),
	// P("EncyclopÃ¦dia Britannica Online ID","EncyclopÃ¦dia Britannica Online ID",true,false),
	// P("has part","has part",true,false),

	;

	
	private String key;
	private String description;
	private boolean retrievable;
	private boolean isWikiDatum;
	
	private ConceptDescriptor(String key, String description, boolean retrievable, boolean isWikiDatum) {
		this.key = key;
		this.description = description;
		this.retrievable = retrievable;
		this.isWikiDatum = isWikiDatum;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static ConceptDescriptor getByKey(String key) {
		for (ConceptDescriptor des: ConceptDescriptor.values()) {
			if (des.getKey().equals(key)) {
				return des;
			}
		}
		return null;
	}
	
    public static ConceptDescriptor lookUpById(String id) {
        	
        	// ignore any qualifier prefix when matching the id
        	int colonIdx = id.indexOf(':') ;
        	if(colonIdx!=-1) id = id.substring(colonIdx+1) ;
        	
        	for(ConceptDescriptor type : ConceptDescriptor.values()) {
        		if(type.name().equals(id.toUpperCase()))
        			return type ;
        	}
        	throw new DomainModelException("Unknown WikiData Property identifier: " + id) ;
        }
	
	/** 
	 * @return the (zero-based) ordered position of the ConceptDescriptor for display
	 */
	public int labelOrder() {
		int position = ordinal() ;
		if(position<P351.ordinal())
			return position ;
		else
			return position-P351.ordinal();
	}

	/**
	 * @return the qualified identifier for WikiData descriptors (but not MyGeneInfo)
	 */
	public String getQualifiedId() {
		return "wd:"+name();
	}

	/**
	 * @return true if the given property is retrievable from a 3rd party data source
	 */
	public Boolean isRetrievable() {
		return retrievable;
	}

	/**
	 * @return true if the given property is retrievable from a 3rd party data source
	 */
	public Boolean isWikiDatum() {
		return isWikiDatum;
	}
}
