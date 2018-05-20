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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Single local concept categories from a given beacon 
 */
@ApiModel(description = "Single local concept categories from a given beacon ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-19T16:31:52.979-07:00")
public class BeaconBeaconConceptCategory {
  @SerializedName("id")
  private String id = null;

  @SerializedName("uri")
  private String uri = null;

  @SerializedName("category")
  private String category = null;

  @SerializedName("frequency")
  private Integer frequency = null;

  public BeaconBeaconConceptCategory id(String id) {
    this.id = id;
    return this;
  }

   /**
   * the 'local' CURIE-encoded identifier of the given concept category, as published by the given beacon 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "the 'local' CURIE-encoded identifier of the given concept category, as published by the given beacon ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconBeaconConceptCategory uri(String uri) {
    this.uri = uri;
    return this;
  }

   /**
   * the 'local' URI of the given concept category,  as published by the given beacon 
   * @return uri
  **/
  @ApiModelProperty(example = "null", value = "the 'local' URI of the given concept category,  as published by the given beacon ")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public BeaconBeaconConceptCategory category(String category) {
    this.category = category;
    return this;
  }

   /**
   * the 'local' human readable label of the given concept category, as published by the given beacon 
   * @return category
  **/
  @ApiModelProperty(example = "null", value = "the 'local' human readable label of the given concept category, as published by the given beacon ")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public BeaconBeaconConceptCategory frequency(Integer frequency) {
    this.frequency = frequency;
    return this;
  }

   /**
   * the number of instances of the specified concept category is used in statements within the given beacon 
   * @return frequency
  **/
  @ApiModelProperty(example = "null", value = "the number of instances of the specified concept category is used in statements within the given beacon ")
  public Integer getFrequency() {
    return frequency;
  }

  public void setFrequency(Integer frequency) {
    this.frequency = frequency;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconBeaconConceptCategory beaconBeaconConceptCategory = (BeaconBeaconConceptCategory) o;
    return Objects.equals(this.id, beaconBeaconConceptCategory.id) &&
        Objects.equals(this.uri, beaconBeaconConceptCategory.uri) &&
        Objects.equals(this.category, beaconBeaconConceptCategory.category) &&
        Objects.equals(this.frequency, beaconBeaconConceptCategory.frequency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uri, category, frequency);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconBeaconConceptCategory {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
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

