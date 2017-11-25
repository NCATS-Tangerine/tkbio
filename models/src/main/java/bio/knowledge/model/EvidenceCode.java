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

/**
 * @author Richard
 *
 */
public enum EvidenceCode {

    // Experimental Evidence Codes
     
	EXP("Inferred from Experiment","exp-inferred-experiment"),
	IDA("Inferred from Direct Assay","ida-inferred-direct-assay"),
	IPI("Inferred from Physical Interaction","ipi-inferred-physical-interaction"),
	IMP("Inferred from Mutant Phenotype","imp-inferred-mutant-phenotype"),
	IGI("Inferred from Genetic Interaction","igi-inferred-genetic-interaction"),
	IEP("Inferred from Expression Pattern","iep-inferred-expression-pattern"),

    // Computational Evidence Codes

    ISS("Inferred from Sequence or Structural Similarity","iss-inferred-sequence-or-structural-similarity"),
    ISA("Inferred from Sequence Alignment","isa-inferred-sequence-alignment"),
    ISO("Inferred from Sequence Orthology","iso-inferred-sequence-orthology"),
    ISM("Inferred from Sequence Model","ism-inferred-sequence-model"),
    IGC("Inferred from Genomic Context","igc-inferred-genomic-context"),
    IBA("Inferred from Biological aspect of Ancestor","iba-inferred-biological-aspect-ancestor"),
    IBD("Inferred from Biological aspect of Descendent","ibd-inferred-biological-aspect-descendent"),
    IKR("Inferred from Key Residues","ikr-inferred-key-residues"),
    IRD("Inferred from Rapid Divergence","ird-inferred-rapid-divergence"),
    RCA("inferred from Reviewed Computational Analysis","rca-inferred-reviewed-computational-analysis"),

    // Author Statement Evidence Codes

    TAS("Traceable Author Statement","tas-traceable-author-statement"),
    NAS("Non-traceable Author Statement","nas-non-traceable-author-statement"),

    // Curatorial Statement Evidence Codes

    IC("Inferred by Curator","ic-inferred-curator"),
    ND("No Biological Data Available","nd-no-biological-data-available"),
	
	// Automatically-assigned Evidence Codes
	
	IEA("Inferred from Electronic Annotation","automatically-assigned-evidence-codes")
	;
	
	private String label;
	private String docpage ;
	
	private EvidenceCode( String label, String docpage ) {
		this.setLabel(label) ;
		this.setDocpage(docpage) ;
	}
	
	public String getLabel() {
		return label;
	}

	private void setLabel(String label) {
		this.label = label;
	}

	public static final String GENEONTOLOGY_BASE_URL = "http://geneontology.org/page/" ;
	
	public String getDocumentation() {
		return GENEONTOLOGY_BASE_URL+docpage;
	}

	private void setDocpage(String docpage) {
		this.docpage = docpage;
	}

	@Override
	public String toString() {
		return getLabel();
	}
}
