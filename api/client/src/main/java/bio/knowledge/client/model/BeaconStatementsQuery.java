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
 * BeaconStatementsQuery
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-04-24T10:05:48.013-07:00")
public class BeaconStatementsQuery {
  @SerializedName("queryId")
  private String queryId = null;

  @SerializedName("source")
  private String source = null;

  @SerializedName("relations")
  private List<String> relations = new ArrayList<String>();

  @SerializedName("target")
  private String target = null;

  @SerializedName("keywords")
  private String keywords = null;

  @SerializedName("types")
  private List<String> types = new ArrayList<String>();

  public BeaconStatementsQuery queryId(String queryId) {
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

  public BeaconStatementsQuery source(String source) {
    this.source = source;
    return this;
  }

   /**
   * 'source' string parameter to call, echoed back 
   * @return source
  **/
  @ApiModelProperty(example = "null", value = "'source' string parameter to call, echoed back ")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public BeaconStatementsQuery relations(List<String> relations) {
    this.relations = relations;
    return this;
  }

  public BeaconStatementsQuery addRelationsItem(String relationsItem) {
    this.relations.add(relationsItem);
    return this;
  }

   /**
   * 'relations' string parameter to call, echoed back 
   * @return relations
  **/
  @ApiModelProperty(example = "null", value = "'relations' string parameter to call, echoed back ")
  public List<String> getRelations() {
    return relations;
  }

  public void setRelations(List<String> relations) {
    this.relations = relations;
  }

  public BeaconStatementsQuery target(String target) {
    this.target = target;
    return this;
  }

   /**
   * 'target' string parameter to call, echoed back 
   * @return target
  **/
  @ApiModelProperty(example = "null", value = "'target' string parameter to call, echoed back ")
  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public BeaconStatementsQuery keywords(String keywords) {
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

  public BeaconStatementsQuery types(List<String> types) {
    this.types = types;
    return this;
  }

  public BeaconStatementsQuery addTypesItem(String typesItem) {
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
    BeaconStatementsQuery beaconStatementsQuery = (BeaconStatementsQuery) o;
    return Objects.equals(this.queryId, beaconStatementsQuery.queryId) &&
        Objects.equals(this.source, beaconStatementsQuery.source) &&
        Objects.equals(this.relations, beaconStatementsQuery.relations) &&
        Objects.equals(this.target, beaconStatementsQuery.target) &&
        Objects.equals(this.keywords, beaconStatementsQuery.keywords) &&
        Objects.equals(this.types, beaconStatementsQuery.types);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queryId, source, relations, target, keywords, types);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconStatementsQuery {\n");
    
    sb.append("    queryId: ").append(toIndentedString(queryId)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    relations: ").append(toIndentedString(relations)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
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

