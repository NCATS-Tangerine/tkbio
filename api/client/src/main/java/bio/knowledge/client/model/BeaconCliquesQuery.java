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
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconCliquesQuery
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-18T18:21:45.192Z")
public class BeaconCliquesQuery {
  @SerializedName("queryId")
  private String queryId = null;

  @SerializedName("ids")
  private List<String> ids = new ArrayList<String>();

  public BeaconCliquesQuery queryId(String queryId) {
    this.queryId = queryId;
    return this;
  }

   /**
   * session identifier of initiated query 
   * @return queryId
  **/
  @ApiModelProperty(example = "null", value = "session identifier of initiated query ")
  public String getQueryId() {
    return queryId;
  }

  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  public BeaconCliquesQuery ids(List<String> ids) {
    this.ids = ids;
    return this;
  }

  public BeaconCliquesQuery addIdsItem(String idsItem) {
    this.ids.add(idsItem);
    return this;
  }

   /**
   * 'ids' string parameter to call, echoed back 
   * @return ids
  **/
  @ApiModelProperty(example = "null", value = "'ids' string parameter to call, echoed back ")
  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconCliquesQuery beaconCliquesQuery = (BeaconCliquesQuery) o;
    return Objects.equals(this.queryId, beaconCliquesQuery.queryId) &&
        Objects.equals(this.ids, beaconCliquesQuery.ids);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queryId, ids);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconCliquesQuery {\n");
    
    sb.append("    queryId: ").append(toIndentedString(queryId)).append("\n");
    sb.append("    ids: ").append(toIndentedString(ids)).append("\n");
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

