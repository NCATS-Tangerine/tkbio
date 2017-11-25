/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software",""), to deal
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

package bio.knowledge.model.wikidata;

import java.util.Optional;

import bio.knowledge.model.DomainModelException;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.model.umls.SemanticType;

/**
 * @author Richard
 *
 */
public enum WikiDataPropertySemanticType {

	P18(null,  "inpr",""),
	P279(null, "idcn","wd"),
	P351(null, "idcn","Entrez"),
	P353(null, "idcn","HGNC.Id"),
	P354(null, "idcn","HGNC.Symbol"),
	P492(Neo4jConcept.class, "dsyn","OMIM"),
	P593(Neo4jConcept.class, "gngm","Gene"),
	P594(null, "idcn","Ensembl"),
	P639(Neo4jConcept.class, "nusq","RefSeq"),
	P644(null, "idcn",""),
	P645(null, "idcn",""),
	P646(null, "idcn","Freebase"),
	P684(Neo4jConcept.class, "gngm","wd"),
	P688(Neo4jConcept.class, "aapp","wd"),
	P692(null, "inpr","GeneAtlas"),
	P703(null, "clas","wd"),
	P704(Neo4jConcept.class, "nusq","Ensembl"),
	P1057(null,"nusq","wd"),
	P1916(null,"idcn","wd"),
	P2548(null,"idcn","wd")
	;
	
	private SemanticType semanticType ;
	private Class<? extends Neo4jConcept> nodeType ;
	private String defaultQualifier ;
	
	private WikiDataPropertySemanticType( 
			Class<? extends Neo4jConcept> nodeType, 
			String semanticCode,
			String qualifier
	) {
		this.nodeType = nodeType ;
		semanticType = SemanticType.lookUpByCode(semanticCode) ;
		defaultQualifier = qualifier ;
	}
	
	public Optional<Class<? extends Neo4jConcept>> getNodeType() {
		if(nodeType==null)
			return Optional.empty();
		else
			return Optional.of(nodeType);
	}
	
	public SemanticType getSemanticType() {
		return semanticType ;
	}
	
	public String getDefaultQualifier() {
		if(!defaultQualifier.isEmpty())
			return defaultQualifier+":" ;
		else
			return "" ;
	}
	
    public static WikiDataPropertySemanticType lookUpByPropertyId(String id) {
    	
    	// ignore any qualifier prefix when matching the id
    	int colonIdx = id.indexOf(':') ;
    	if(colonIdx!=-1) id = id.substring(colonIdx+1) ;
    	
    	for(WikiDataPropertySemanticType type : WikiDataPropertySemanticType.values()) {
    		if(type.name().equals(id.toUpperCase()))
    			return type ;
    	}
    	throw new DomainModelException("Unknown WikiData Property identifier: " + id) ;
    }
}
