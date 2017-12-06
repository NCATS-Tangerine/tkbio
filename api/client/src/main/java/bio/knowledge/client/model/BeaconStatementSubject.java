/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.6
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
 * BeaconStatementSubject
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-12-06T00:37:22.296-08:00")
public class BeaconStatementSubject {
  @SerializedName("clique")
  private String clique = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("semanticGroup")
  private String semanticGroup = null;

  public BeaconStatementSubject clique(String clique) {
    this.clique = clique;
    return this;
  }

   /**
   * CURIE-encoded canonical identifier of \"equivalent concepts clique\" 
   * @return clique
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded canonical identifier of \"equivalent concepts clique\" ")
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
   * CURIE-encoded identifier of concept 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded identifier of concept ")
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
   * human readable label of subject concept
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "human readable label of subject concept")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BeaconStatementSubject semanticGroup(String semanticGroup) {
    this.semanticGroup = semanticGroup;
    return this;
  }

   /**
   * a semantic group for the subject concept (specified as a code CHEM, GENE, etc. - see [Semantic Groups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) 
   * @return semanticGroup
  **/
  @ApiModelProperty(example = "null", value = "a semantic group for the subject concept (specified as a code CHEM, GENE, etc. - see [Semantic Groups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) ")
  public String getSemanticGroup() {
    return semanticGroup;
  }

  public void setSemanticGroup(String semanticGroup) {
    this.semanticGroup = semanticGroup;
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
        Objects.equals(this.semanticGroup, beaconStatementSubject.semanticGroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clique, id, name, semanticGroup);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconStatementSubject {\n");
    
    sb.append("    clique: ").append(toIndentedString(clique)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    semanticGroup: ").append(toIndentedString(semanticGroup)).append("\n");
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

