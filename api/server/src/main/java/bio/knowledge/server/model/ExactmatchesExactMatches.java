package bio.knowledge.server.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * ExactmatchesExactMatches
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-02T16:41:12.704-07:00")

public class ExactmatchesExactMatches   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("evidenceCode")
  private String evidenceCode = null;

  @JsonProperty("reference")
  private String reference = null;

  public ExactmatchesExactMatches id(String id) {
    this.id = id;
    return this;
  }

   /**
   * [CURIE](https://www.w3.org/TR/curie/) identifier of asserted exact matching concept 
   * @return id
  **/
  @ApiModelProperty(value = "[CURIE](https://www.w3.org/TR/curie/) identifier of asserted exact matching concept ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ExactmatchesExactMatches evidenceCode(String evidenceCode) {
    this.evidenceCode = evidenceCode;
    return this;
  }

   /**
   * identifier of [GENE Ontology Evidence Codes](http://geneontology.org/page/guide-go-evidence-codes) 
   * @return evidenceCode
  **/
  @ApiModelProperty(value = "identifier of [GENE Ontology Evidence Codes](http://geneontology.org/page/guide-go-evidence-codes) ")
  public String getEvidenceCode() {
    return evidenceCode;
  }

  public void setEvidenceCode(String evidenceCode) {
    this.evidenceCode = evidenceCode;
  }

  public ExactmatchesExactMatches reference(String reference) {
    this.reference = reference;
    return this;
  }

   /**
   * [CURIE](https://www.w3.org/TR/curie/) identifier of supporting reference 
   * @return reference
  **/
  @ApiModelProperty(value = "[CURIE](https://www.w3.org/TR/curie/) identifier of supporting reference ")
  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExactmatchesExactMatches exactmatchesExactMatches = (ExactmatchesExactMatches) o;
    return Objects.equals(this.id, exactmatchesExactMatches.id) &&
        Objects.equals(this.evidenceCode, exactmatchesExactMatches.evidenceCode) &&
        Objects.equals(this.reference, exactmatchesExactMatches.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, evidenceCode, reference);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExactmatchesExactMatches {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    evidenceCode: ").append(toIndentedString(evidenceCode)).append("\n");
    sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

