/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.1.1
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
 * Local predicate definition from a given beacon 
 */
@ApiModel(description = "Local predicate definition from a given beacon ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-09-20T12:33:58.705-07:00")
public class BeaconBeaconPredicate {
  @SerializedName("id")
  private String id = null;

  @SerializedName("uri")
  private String uri = null;

  @SerializedName("relation")
  private String relation = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("frequency")
  private Integer frequency = null;

  public BeaconBeaconPredicate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * the 'local' CURIE-encoded identifier of a maximal predicate relation, as published by  the given beacon 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "the 'local' CURIE-encoded identifier of a maximal predicate relation, as published by  the given beacon ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconBeaconPredicate uri(String uri) {
    this.uri = uri;
    return this;
  }

   /**
   * the 'local' URI of a maximal predicate relation,  as published by the given beacon 
   * @return uri
  **/
  @ApiModelProperty(example = "null", value = "the 'local' URI of a maximal predicate relation,  as published by the given beacon ")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public BeaconBeaconPredicate relation(String relation) {
    this.relation = relation;
    return this;
  }

   /**
   * the human readable 'relation of the 'maximal' predicate (see the [Biolink Model](https://biolink.github.io/biolink-model) for a list of Biolink maximal predicates; this field may map onto beacon-specific non-Biolink relations 
   * @return relation
  **/
  @ApiModelProperty(example = "null", value = "the human readable 'relation of the 'maximal' predicate (see the [Biolink Model](https://biolink.github.io/biolink-model) for a list of Biolink maximal predicates; this field may map onto beacon-specific non-Biolink relations ")
  public String getRelation() {
    return relation;
  }

  public void setRelation(String relation) {
    this.relation = relation;
  }

  public BeaconBeaconPredicate description(String description) {
    this.description = description;
    return this;
  }

   /**
   * human readable definition of the given  predicate relation 
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "human readable definition of the given  predicate relation ")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BeaconBeaconPredicate frequency(Integer frequency) {
    this.frequency = frequency;
    return this;
  }

   /**
   * the number of instances of the specified predicate relation is used in statements within the given beacon 
   * @return frequency
  **/
  @ApiModelProperty(example = "null", value = "the number of instances of the specified predicate relation is used in statements within the given beacon ")
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
    BeaconBeaconPredicate beaconBeaconPredicate = (BeaconBeaconPredicate) o;
    return Objects.equals(this.id, beaconBeaconPredicate.id) &&
        Objects.equals(this.uri, beaconBeaconPredicate.uri) &&
        Objects.equals(this.relation, beaconBeaconPredicate.relation) &&
        Objects.equals(this.description, beaconBeaconPredicate.description) &&
        Objects.equals(this.frequency, beaconBeaconPredicate.frequency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uri, relation, description, frequency);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconBeaconPredicate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    relation: ").append(toIndentedString(relation)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

