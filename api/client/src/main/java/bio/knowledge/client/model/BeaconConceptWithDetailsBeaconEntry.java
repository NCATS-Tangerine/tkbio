/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.12
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.model;

import java.util.Objects;
import bio.knowledge.client.model.BeaconConceptDetail;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconConceptWithDetailsBeaconEntry
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-18T11:21:57.544-07:00")
public class BeaconConceptWithDetailsBeaconEntry {
  @SerializedName("beacon")
  private Integer beacon = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("synonyms")
  private List<String> synonyms = new ArrayList<String>();

  @SerializedName("definition")
  private String definition = null;

  @SerializedName("details")
  private List<BeaconConceptDetail> details = new ArrayList<BeaconConceptDetail>();

  public BeaconConceptWithDetailsBeaconEntry beacon(Integer beacon) {
    this.beacon = beacon;
    return this;
  }

   /**
   * Index number of beacon providing these concept details 
   * @return beacon
  **/
  @ApiModelProperty(example = "null", value = "Index number of beacon providing these concept details ")
  public Integer getBeacon() {
    return beacon;
  }

  public void setBeacon(Integer beacon) {
    this.beacon = beacon;
  }

  public BeaconConceptWithDetailsBeaconEntry id(String id) {
    this.id = id;
    return this;
  }

   /**
   * CURIE identifying the specific beacon source concept being described. 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "CURIE identifying the specific beacon source concept being described. ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconConceptWithDetailsBeaconEntry synonyms(List<String> synonyms) {
    this.synonyms = synonyms;
    return this;
  }

  public BeaconConceptWithDetailsBeaconEntry addSynonymsItem(String synonymsItem) {
    this.synonyms.add(synonymsItem);
    return this;
  }

   /**
   * List of synonymous names or identifiers for the concept 
   * @return synonyms
  **/
  @ApiModelProperty(example = "null", value = "List of synonymous names or identifiers for the concept ")
  public List<String> getSynonyms() {
    return synonyms;
  }

  public void setSynonyms(List<String> synonyms) {
    this.synonyms = synonyms;
  }

  public BeaconConceptWithDetailsBeaconEntry definition(String definition) {
    this.definition = definition;
    return this;
  }

   /**
   * Concept definition provided by a given beacon 
   * @return definition
  **/
  @ApiModelProperty(example = "null", value = "Concept definition provided by a given beacon ")
  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  public BeaconConceptWithDetailsBeaconEntry details(List<BeaconConceptDetail> details) {
    this.details = details;
    return this;
  }

  public BeaconConceptWithDetailsBeaconEntry addDetailsItem(BeaconConceptDetail detailsItem) {
    this.details.add(detailsItem);
    return this;
  }

   /**
   * Get details
   * @return details
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<BeaconConceptDetail> getDetails() {
    return details;
  }

  public void setDetails(List<BeaconConceptDetail> details) {
    this.details = details;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConceptWithDetailsBeaconEntry beaconConceptWithDetailsBeaconEntry = (BeaconConceptWithDetailsBeaconEntry) o;
    return Objects.equals(this.beacon, beaconConceptWithDetailsBeaconEntry.beacon) &&
        Objects.equals(this.id, beaconConceptWithDetailsBeaconEntry.id) &&
        Objects.equals(this.synonyms, beaconConceptWithDetailsBeaconEntry.synonyms) &&
        Objects.equals(this.definition, beaconConceptWithDetailsBeaconEntry.definition) &&
        Objects.equals(this.details, beaconConceptWithDetailsBeaconEntry.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(beacon, id, synonyms, definition, details);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConceptWithDetailsBeaconEntry {\n");
    
    sb.append("    beacon: ").append(toIndentedString(beacon)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    synonyms: ").append(toIndentedString(synonyms)).append("\n");
    sb.append("    definition: ").append(toIndentedString(definition)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

