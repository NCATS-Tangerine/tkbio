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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * BeaconStatementsQuery
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-09-20T12:33:58.705-07:00")
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
  private List<String> keywords = new ArrayList<String>();

  @SerializedName("categories")
  private List<String> categories = new ArrayList<String>();

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

  public BeaconStatementsQuery keywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }

  public BeaconStatementsQuery addKeywordsItem(String keywordsItem) {
    this.keywords.add(keywordsItem);
    return this;
  }

   /**
   * 'keywords' string filter parameter to call, echoed back 
   * @return keywords
  **/
  @ApiModelProperty(example = "null", value = "'keywords' string filter parameter to call, echoed back ")
  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public BeaconStatementsQuery categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public BeaconStatementsQuery addCategoriesItem(String categoriesItem) {
    this.categories.add(categoriesItem);
    return this;
  }

   /**
   * 'categories' string parameter to call, echoed back 
   * @return categories
  **/
  @ApiModelProperty(example = "null", value = "'categories' string parameter to call, echoed back ")
  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
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
        Objects.equals(this.categories, beaconStatementsQuery.categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queryId, source, relations, target, keywords, categories);
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
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
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

