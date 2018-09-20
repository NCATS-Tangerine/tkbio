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
 * BeaconStatementSubject
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-09-20T12:33:58.705-07:00")
public class BeaconStatementSubject {
  @SerializedName("clique")
  private String clique = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("categories")
  private List<String> categories = new ArrayList<String>();

  public BeaconStatementSubject clique(String clique) {
    this.clique = clique;
    return this;
  }

   /**
   * CURIE-encoded canonical identifier of \"equivalent concepts clique\" of the subject concept 
   * @return clique
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded canonical identifier of \"equivalent concepts clique\" of the subject concept ")
  public String getClique() {
    return clique;
  }

  public void setClique(String clique) {
    this.clique = clique;
  }

  public BeaconStatementSubject id(String id) {
    this.id = id;
    return this;
  }

   /**
   * CURIE-encoded identifier of the subject concept 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded identifier of the subject concept ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconStatementSubject name(String name) {
    this.name = name;
    return this;
  }

   /**
   * human readable label of the subject concept
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "human readable label of the subject concept")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BeaconStatementSubject categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public BeaconStatementSubject addCategoriesItem(String categoriesItem) {
    this.categories.add(categoriesItem);
    return this;
  }

   /**
   * Semantic categories of the subject concept ((see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of categories). 
   * @return categories
  **/
  @ApiModelProperty(example = "null", value = "Semantic categories of the subject concept ((see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of categories). ")
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
    BeaconStatementSubject beaconStatementSubject = (BeaconStatementSubject) o;
    return Objects.equals(this.clique, beaconStatementSubject.clique) &&
        Objects.equals(this.id, beaconStatementSubject.id) &&
        Objects.equals(this.name, beaconStatementSubject.name) &&
        Objects.equals(this.categories, beaconStatementSubject.categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clique, id, name, categories);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconStatementSubject {\n");
    
    sb.append("    clique: ").append(toIndentedString(clique)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

