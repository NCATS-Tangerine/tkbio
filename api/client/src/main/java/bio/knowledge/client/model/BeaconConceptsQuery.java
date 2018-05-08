/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.10
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
 * BeaconConceptsQuery
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-04-24T10:05:48.013-07:00")
public class BeaconConceptsQuery {
  @SerializedName("queryId")
  private String queryId = null;

  @SerializedName("keywords")
  private String keywords = null;

  @SerializedName("types")
  private List<String> types = new ArrayList<String>();

  public BeaconConceptsQuery queryId(String queryId) {
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

  public BeaconConceptsQuery keywords(String keywords) {
    this.keywords = keywords;
    return this;
  }

   /**
   * 'keywords' string parameter to call, echoed back 
   * @return keywords
  **/
  @ApiModelProperty(example = "null", value = "'keywords' string parameter to call, echoed back ")
  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public BeaconConceptsQuery types(List<String> types) {
    this.types = types;
    return this;
  }

  public BeaconConceptsQuery addTypesItem(String typesItem) {
    this.types.add(typesItem);
    return this;
  }

   /**
   * 'types' string parameter to call, echoed back 
   * @return types
  **/
  @ApiModelProperty(example = "null", value = "'types' string parameter to call, echoed back ")
  public List<String> getTypes() {
    return types;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConceptsQuery beaconConceptsQuery = (BeaconConceptsQuery) o;
    return Objects.equals(this.queryId, beaconConceptsQuery.queryId) &&
        Objects.equals(this.keywords, beaconConceptsQuery.keywords) &&
        Objects.equals(this.types, beaconConceptsQuery.types);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queryId, keywords, types);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConceptsQuery {\n");
    
    sb.append("    queryId: ").append(toIndentedString(queryId)).append("\n");
    sb.append("    keywords: ").append(toIndentedString(keywords)).append("\n");
    sb.append("    types: ").append(toIndentedString(types)).append("\n");
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
