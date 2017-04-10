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
package bio.knowledge.model.umls;

import bio.knowledge.model.DomainModelException;

/**
 * @author Richard
 *
 */
public enum SemanticType {
	
	acty( Category.ACTI, "T052", "Activity"),
	bhvr( Category.ACTI, "T053", "Behavior"),
	dora( Category.ACTI, "T056", "Daily or Recreational Activity"),
	evnt( Category.ACTI, "T051", "Event"),
	gora( Category.ACTI, "T064", "Governmental or Regulatory Activity"),
	inbe( Category.ACTI, "T055", "Individual Behavior"),
	mcha( Category.ACTI, "T066", "Machine Activity"),
	ocac( Category.ACTI, "T057", "Occupational Activity"),
	socb( Category.ACTI, "T054", "Social Behavior"),
	anst( Category.ANAT, "T017", "Anatomical Structure"),
	bdsu( Category.ANAT, "T031", "Body Substance"),
	bdsy( Category.ANAT, "T022", "Body System"),
	blor( Category.ANAT, "T029", "Body Location or Region"),
	bpoc( Category.ANAT, "T023", "Body Part, Organ, or Organ Component"),
	bsoj( Category.ANAT, "T030", "Body Space or Junction"),
	celc( Category.ANAT, "T026", "Cell Component"),
	cell( Category.ANAT, "T025", "Cell"),
	emst( Category.ANAT, "T018", "Embryonic Structure"),
	ffas( Category.ANAT, "T021", "Fully Formed Anatomical Structure"), 
	tisu( Category.ANAT, "T024", "Tissue"), 
	aapp( Category.CHEM, "T116", "Amino Acid, Peptide, or Protein"), 
	antb( Category.CHEM, "T195", "Antibiotic"), 
	bacs( Category.CHEM, "T123", "Biologically Active Substance"), 
	bodm( Category.CHEM, "T122", "Biomedical or Dental Material"), 
	carb( Category.CHEM, "T118", "Carbohydrate"), 
	chem( Category.CHEM, "T103", "Chemical"), 
	chvf( Category.CHEM, "T120", "Chemical Viewed Functionally"), 
	chvs( Category.CHEM, "T104", "Chemical Viewed Structurally"), 
	clnd( Category.CHEM, "T200", "Clinical Drug"), 
	eico( Category.CHEM, "T111", "Eicosanoid"), 
	elii( Category.CHEM, "T196", "Element, Ion, or Isotope"), 
	enzy( Category.CHEM, "T126", "Enzyme"), 
	hops( Category.CHEM, "T131", "Hazardous or Poisonous Substance"), 
	horm( Category.CHEM, "T125", "Hormone"), 
	imft( Category.CHEM, "T129", "Immunologic Factor"), 
	inch( Category.CHEM, "T197", "Inorganic Chemical"), 
	irda( Category.CHEM, "T130", "Indicator, Reagent, or Diagnostic Aid"), 
	lipd( Category.CHEM, "T119", "Lipid"), 
	nnon( Category.CHEM, "T114", "Nucleic Acid, Nucleoside, or Nucleotide"), 
	nsba( Category.CHEM, "T124", "Neuroreactive Substance or Biogenic Amine"), 
	opco( Category.CHEM, "T115", "Organophosphorus Compound"), 
	orch( Category.CHEM, "T109", "Organic Chemical"), 
	phsu( Category.CHEM, "T121", "Pharmacologic Substance"), 
	rcpt( Category.CHEM, "T192", "Receptor"), 
	strd( Category.CHEM, "T110", "Steroid"), 
	vita( Category.CHEM, "T127", "Vitamin"), 
	clas( Category.CONC, "T185", "Classification"), 
	cnce( Category.CONC, "T077", "Conceptual Entity"), 
	ftcn( Category.CONC, "T169", "Functional Concept"), 
	grpa( Category.CONC, "T102", "Group Attribute"), 
	idcn( Category.CONC, "T078", "Idea or Concept"), 
	inpr( Category.CONC, "T170", "Intellectual Product"), 
	lang( Category.CONC, "T171", "Language"), 
	qlco( Category.CONC, "T080", "Qualitative Concept"), 
	qnco( Category.CONC, "T081", "Quantitative Concept"), 
	rnlw( Category.CONC, "T089", "Regulation or Law"), 
	spco( Category.CONC, "T082", "Spatial Concept"), 
	tmco( Category.CONC, "T079", "Temporal Concept"), 
	drdd( Category.DEVI, "T203", "Drug Delivery Device"), 
	medd( Category.DEVI, "T074", "Medical Device"), 
	resd( Category.DEVI, "T075", "Research Device"), 
	acab( Category.DISO, "T020", "Acquired Abnormality"), 
	anab( Category.DISO, "T190", "Anatomical Abnormality"), 
	cgab( Category.DISO, "T019", "Congenital Abnormality"), 
	comd( Category.DISO, "T049", "Cell or Molecular Dysfunction"), 
	dsyn( Category.DISO, "T047", "Disease or Syndrome"), 
	emod( Category.DISO, "T050", "Experimental Model of Disease"), 
	fndg( Category.DISO, "T033", "Finding"), 
	inpo( Category.DISO, "T037", "Injury or Poisoning"), 
	mobd( Category.DISO, "T048", "Mental or Behavioral Dysfunction"), 
	neop( Category.DISO, "T191", "Neoplastic Process"), 
	patf( Category.DISO, "T046", "Pathologic Function"), 
	sosy( Category.DISO, "T184", "Sign or Symptom"), 
	amas( Category.GENE, "T087", "Amino Acid Sequence"), 
	crbs( Category.GENE, "T088", "Carbohydrate Sequence"), 
	gngm( Category.GENE, "T028", "Gene or Genome"), 
	mosq( Category.GENE, "T085", "Molecular Sequence"), 
	nusq( Category.GENE, "T086", "Nucleotide Sequence"), 
	geoa( Category.GEOG, "T083", "Geographic Area"), 
	aggp( Category.LIVB, "T100", "Age Group"), 
	amph( Category.LIVB, "T011", "Amphibian"), 
	anim( Category.LIVB, "T008", "Animal"), 
	arch( Category.LIVB, "T194", "Archaeon"), 
	bact( Category.LIVB, "T007", "Bacterium"), 
	bird( Category.LIVB, "T012", "Bird"), 
	euka( Category.LIVB, "T204", "Eukaryote"), 
	famg( Category.LIVB, "T099", "Family Group"), 
	fish( Category.LIVB, "T013", "Fish"), 
	fngs( Category.LIVB, "T004", "Fungus"), 
	grup( Category.LIVB, "T096", "Group"), 
	humn( Category.LIVB, "T016", "Human"), 
	mamm( Category.LIVB, "T015", "Mammal"), 
	orgm( Category.LIVB, "T001", "Organism"), 
	plnt( Category.LIVB, "T002", "Plant"), 
	podg( Category.LIVB, "T101", "Patient or Disabled Group"), 
	popg( Category.LIVB, "T098", "Population Group"), 
	prog( Category.LIVB, "T097", "Professional or Occupational Group"), 
	rept( Category.LIVB, "T014", "Reptile"), 
	virs( Category.LIVB, "T005", "Virus"), 
	vtbt( Category.LIVB, "T010", "Vertebrate"), 
	enty( Category.OBJC, "T071", "Entity"), 
	food( Category.OBJC, "T168", "Food"), 
	mnob( Category.OBJC, "T073", "Manufactured Object"), 
	phob( Category.OBJC, "T072", "Physical Object"), 
	sbst( Category.OBJC, "T167", "Substance"), 
	bmod( Category.OCCU, "T091", "Biomedical Occupation or Discipline"), 
	ocdi( Category.OCCU, "T090", "Occupation or Discipline"), 
	hcro( Category.ORGA, "T093", "Health Care Related Organization"), 
	orgt( Category.ORGA, "T092", "Organization"), 
	pros( Category.ORGA, "T094", "Professional Society"), 
	shro( Category.ORGA, "T095", "Self-help or Relief Organization"), 
	biof( Category.PHEN, "T038", "Biologic Function"), 
	eehu( Category.PHEN, "T069", "Environmental Effect of Humans"), 
	hcpp( Category.PHEN, "T068", "Human-caused Phenomenon or Process"), 
	lbtr( Category.PHEN, "T034", "Laboratory or Test Result"), 
	npop( Category.PHEN, "T070", "Natural Phenomenon or Process"), 
	phpr( Category.PHEN, "T067", "Phenomenon or Process"), 
	celf( Category.PHYS, "T043", "Cell Function"), 
	clna( Category.PHYS, "T201", "Clinical Attribute"), 
	genf( Category.PHYS, "T045", "Genetic Function"), 
	menp( Category.PHYS, "T041", "Mental Process"), 
	moft( Category.PHYS, "T044", "Molecular Function"), 
	orga( Category.PHYS, "T032", "Organism Attribute"), 
	orgf( Category.PHYS, "T040", "Organism Function"), 
	ortf( Category.PHYS, "T042", "Organ or Tissue Function"), 
	phsf( Category.PHYS, "T039", "Physiologic Function"), 
	diap( Category.PROC, "T060", "Diagnostic Procedure"), 
	edac( Category.PROC, "T065", "Educational Activity"), 
	hlca( Category.PROC, "T058", "Health Care Activity"), 
	lbpr( Category.PROC, "T059", "Laboratory Procedure"), 
	mbrt( Category.PROC, "T063", "Molecular Biology Research Technique"), 
	resa( Category.PROC, "T062", "Research Activity"), 
	topp( Category.PROC, "T061", "Therapeutic or Preventive Procedure")
    ;

	public enum Category {
		
		ACTI("Activities & Behaviors"), 
		ANAT("Anatomy"), 
		CHEM("Chemicals & Drugs"), 
		CONC("Concepts & Ideas"), 
		DEVI("Devices"), 
		DISO("Disorders"), 
		GENE("Genes & Molecular Sequences"), 
		GEOG("Geographic Areas"), 
		LIVB("Living Beings"), 
		OBJC("Objects"), 
		OCCU("Occupations"), 
		ORGA("Organizations"), 
		PHEN("Phenomena"), 
		PHYS("Physiology"), 
		PROC("Procedures")
		;
		
		private String description ;
		
		private Category( String description ) {
			this.description = description ;
		}
		
		public String getDescription() {
			return this.description ;
		}
		
		public static Category getCategoryByDescription(String description) {
			for (Category cat : Category.values()) {
				if (cat.getDescription().equals(description)) {
					return cat;
				}
			}
			return null;
		}
		
		@Override
		public String toString() {
			return description;
		}
	}
	
	private Category category ;
	private String id ;
	private String description ;
	
	private SemanticType( Category category, String id, String description) {
		this.category = category ;
		this.id = id ;
		this.description = description ;
	}
	
	public Category getCategory() {
		return category ;
	}
	
	public String getCode() {
		return id ;
	}
	
	public String getDescription() {
		return description ;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
    public static SemanticType lookUpByCode(String code) {
    	for(SemanticType type: SemanticType.values()) {
    		if(type.name().equals(code.toLowerCase()))
    			return type ;
    	}
    	throw new DomainModelException("Unknown Semantic Type code: " + code) ;
    }

    public static SemanticType lookUpByDescription(String description) {
    	for(SemanticType type: SemanticType.values()) {
    		if(type.getDescription().equals(description))
    			return type ;
    	}
    	return null;
    }
}