/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.1.0
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * BeaconAnnotation
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-19T16:31:52.979-07:00")
public class BeaconAnnotation {
  @SerializedName("id")
  private String id = null;

  @SerializedName("label")
  private String label = null;

  @SerializedName("evidence_code")
  private String evidenceCode = null;

  @SerializedName("date")
  private String date = null;

  @SerializedName("beacon")
  private Integer beacon = null;

  public BeaconAnnotation id(String id) {
    this.id = id;
    return this;
  }

   /**
   * CURIE-encoded identifier to an associated external resources (e.g. PMID of a pubmed citation) 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded identifier to an associated external resources (e.g. PMID of a pubmed citation) ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconAnnotation label(String label) {
    this.label = label;
    return this;
  }

   /**
   * canonical human readable and searchable label of the annotation (i.e. comment, matched sentence, etc.) 
   * @return label
  **/
  @ApiModelProperty(example = "null", value = "canonical human readable and searchable label of the annotation (i.e. comment, matched sentence, etc.) ")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public BeaconAnnotation evidenceCode(String evidenceCode) {
    this.evidenceCode = evidenceCode;
    return this;
  }

   /**
   * Gene Ontology Evidence Code (http://www.geneontology.org/page/guide-go-evidence-codes) 
   * @return evidenceCode
  **/
  @ApiModelProperty(example = "null", value = "Gene Ontology Evidence Code (http://www.geneontology.org/page/guide-go-evidence-codes) ")
  public String getEvidenceCode() {
    return evidenceCode;
  }

  public void setEvidenceCode(String evidenceCode) {
    this.evidenceCode = evidenceCode;
  }

  public BeaconAnnotation date(String date) {
    this.date = date;
    return this;
  }

   /**
   * publication date of annotation (generally of format 'yyyy-mm-dd') 
   * @return date
  **/
  @ApiModelProperty(example = "null", value = "publication date of annotation (generally of format 'yyyy-mm-dd') ")
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public BeaconAnnotation beacon(Integer beacon) {
    this.beacon = beacon;
    return this;
  }

   /**
   * aggregator assigned beacon index identifier 
   * @return beacon
  **/
  @ApiModelProperty(example = "null", value = "aggregator assigned beacon index identifier ")
  public Integer getBeacon() {
    return beacon;
  }

  public void setBeacon(Integer beacon) {
    this.beacon = beacon;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconAnnotation beaconAnnotation = (BeaconAnnotation) o;
    return Objects.equals(this.id, beaconAnnotation.id) &&
        Objects.equals(this.label, beaconAnnotation.label) &&
        Objects.equals(this.evidenceCode, beaconAnnotation.evidenceCode) &&
        Objects.equals(this.date, beaconAnnotation.date) &&
        Objects.equals(this.beacon, beaconAnnotation.beacon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, label, evidenceCode, date, beacon);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconAnnotation {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    evidenceCode: ").append(toIndentedString(evidenceCode)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    beacon: ").append(toIndentedString(beacon)).append("\n");
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

