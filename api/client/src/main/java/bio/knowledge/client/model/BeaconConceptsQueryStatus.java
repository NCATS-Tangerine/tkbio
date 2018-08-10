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
import bio.knowledge.client.model.BeaconConceptsQueryBeaconStatus;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconConceptsQueryStatus
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-18T18:21:45.192Z")
public class BeaconConceptsQueryStatus {
  @SerializedName("queryId")
  private String queryId = null;

  @SerializedName("status")
  private List<BeaconConceptsQueryBeaconStatus> status = new ArrayList<BeaconConceptsQueryBeaconStatus>();

  public BeaconConceptsQueryStatus queryId(String queryId) {
    this.queryId = queryId;
    return this;
  }

   /**
   * session identifier of a query previously initiated by /concepts 
   * @return queryId
  **/
  @ApiModelProperty(example = "null", value = "session identifier of a query previously initiated by /concepts ")
  public String getQueryId() {
    return queryId;
  }

  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  public BeaconConceptsQueryStatus status(List<BeaconConceptsQueryBeaconStatus> status) {
    this.status = status;
    return this;
  }

  public BeaconConceptsQueryStatus addStatusItem(BeaconConceptsQueryBeaconStatus statusItem) {
    this.status.add(statusItem);
    return this;
  }

   /**
   * array of beacon-specific query status reports 
   * @return status
  **/
  @ApiModelProperty(example = "null", value = "array of beacon-specific query status reports ")
  public List<BeaconConceptsQueryBeaconStatus> getStatus() {
    return status;
  }

  public void setStatus(List<BeaconConceptsQueryBeaconStatus> status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConceptsQueryStatus beaconConceptsQueryStatus = (BeaconConceptsQueryStatus) o;
    return Objects.equals(this.queryId, beaconConceptsQueryStatus.queryId) &&
        Objects.equals(this.status, beaconConceptsQueryStatus.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queryId, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConceptsQueryStatus {\n");
    
    sb.append("    queryId: ").append(toIndentedString(queryId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

