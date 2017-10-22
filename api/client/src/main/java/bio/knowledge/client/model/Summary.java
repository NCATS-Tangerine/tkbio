/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.4
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
 * Summary
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-10-21T20:46:51.565-07:00")
public class Summary {
  @SerializedName("id")
  private String id = null;

  @SerializedName("idmap")
  private String idmap = null;

  @SerializedName("frequency")
  private Integer frequency = null;

  @SerializedName("beacon")
  private String beacon = null;

  public Summary id(String id) {
    this.id = id;
    return this;
  }

   /**
   * the concept type (semantic group) 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "the concept type (semantic group) ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Summary idmap(String idmap) {
    this.idmap = idmap;
    return this;
  }

   /**
   * The URL to execute the exactmatches API call on the id
   * @return idmap
  **/
  @ApiModelProperty(example = "null", value = "The URL to execute the exactmatches API call on the id")
  public String getIdmap() {
    return idmap;
  }

  public void setIdmap(String idmap) {
    this.idmap = idmap;
  }

  public Summary frequency(Integer frequency) {
    this.frequency = frequency;
    return this;
  }

   /**
   * the number of instances of the specified type 
   * @return frequency
  **/
  @ApiModelProperty(example = "null", value = "the number of instances of the specified type ")
  public Integer getFrequency() {
    return frequency;
  }

  public void setFrequency(Integer frequency) {
    this.frequency = frequency;
  }

  public Summary beacon(String beacon) {
    this.beacon = beacon;
    return this;
  }

   /**
   * beacon ID 
   * @return beacon
  **/
  @ApiModelProperty(example = "null", value = "beacon ID ")
  public String getBeacon() {
    return beacon;
  }

  public void setBeacon(String beacon) {
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
    Summary summary = (Summary) o;
    return Objects.equals(this.id, summary.id) &&
        Objects.equals(this.idmap, summary.idmap) &&
        Objects.equals(this.frequency, summary.frequency) &&
        Objects.equals(this.beacon, summary.beacon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idmap, frequency, beacon);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Summary {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idmap: ").append(toIndentedString(idmap)).append("\n");
    sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
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

