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

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * BeaconStatementsQueryBeaconStatus
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-18T11:46:52.023-07:00")
public class BeaconStatementsQueryBeaconStatus {
  @SerializedName("beacon")
  private Integer beacon = null;

  @SerializedName("status")
  private Integer status = null;

  @SerializedName("count")
  private Integer count = null;

  public BeaconStatementsQueryBeaconStatus beacon(Integer beacon) {
    this.beacon = beacon;
    return this;
  }

   /**
   * Index number of beacon providing these statements 
   * @return beacon
  **/
  @ApiModelProperty(example = "null", value = "Index number of beacon providing these statements ")
  public Integer getBeacon() {
    return beacon;
  }

  public void setBeacon(Integer beacon) {
    this.beacon = beacon;
  }

  public BeaconStatementsQueryBeaconStatus status(Integer status) {
    this.status = status;
    return this;
  }

   /**
   * Http code status of beacon API - 200 means 'data ready',  102 means 'query in progress', other codes (e.g. 500) are  server errors. Once a beacon has a '200' success code,  then the /statements/data endpoint may be used to retrieve it. 
   * @return status
  **/
  @ApiModelProperty(example = "null", value = "Http code status of beacon API - 200 means 'data ready',  102 means 'query in progress', other codes (e.g. 500) are  server errors. Once a beacon has a '200' success code,  then the /statements/data endpoint may be used to retrieve it. ")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public BeaconStatementsQueryBeaconStatus count(Integer count) {
    this.count = count;
    return this;
  }

   /**
   * When a 200 status code is returned, this integer designates  the number of statements matched by the query for the  given beacon. 
   * @return count
  **/
  @ApiModelProperty(example = "null", value = "When a 200 status code is returned, this integer designates  the number of statements matched by the query for the  given beacon. ")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconStatementsQueryBeaconStatus beaconStatementsQueryBeaconStatus = (BeaconStatementsQueryBeaconStatus) o;
    return Objects.equals(this.beacon, beaconStatementsQueryBeaconStatus.beacon) &&
        Objects.equals(this.status, beaconStatementsQueryBeaconStatus.status) &&
        Objects.equals(this.count, beaconStatementsQueryBeaconStatus.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(beacon, status, count);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconStatementsQueryBeaconStatus {\n");
    
    sb.append("    beacon: ").append(toIndentedString(beacon)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

