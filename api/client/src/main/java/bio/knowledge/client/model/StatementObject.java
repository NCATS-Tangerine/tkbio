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
 * StatementObject
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-10-13T21:43:21.781-07:00")
public class StatementObject {
  @SerializedName("clique")
  private String clique = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("semgroup")
  private String semgroup = null;

  public StatementObject clique(String clique) {
    this.clique = clique;
    return this;
  }

   /**
   * CURIE-encoded cannonical identifier of \"equivalent concepts clique\" 
   * @return clique
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded cannonical identifier of \"equivalent concepts clique\" ")
  public String getClique() {
    return clique;
  }

  public void setClique(String clique) {
    this.clique = clique;
  }

  public StatementObject id(String id) {
    this.id = id;
    return this;
  }

   /**
   * CURIE-encoded identifier of object concept 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded identifier of object concept ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public StatementObject name(String name) {
    this.name = name;
    return this;
  }

   /**
   * human readable label of object concept
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "human readable label of object concept")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StatementObject semgroup(String semgroup) {
    this.semgroup = semgroup;
    return this;
  }

   /**
   * a semantic group for the subject concept (specified as a code CHEM, GENE, etc. - see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) 
   * @return semgroup
  **/
  @ApiModelProperty(example = "null", value = "a semantic group for the subject concept (specified as a code CHEM, GENE, etc. - see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) ")
  public String getSemgroup() {
    return semgroup;
  }

  public void setSemgroup(String semgroup) {
    this.semgroup = semgroup;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatementObject statementObject = (StatementObject) o;
    return Objects.equals(this.clique, statementObject.clique) &&
        Objects.equals(this.id, statementObject.id) &&
        Objects.equals(this.name, statementObject.name) &&
        Objects.equals(this.semgroup, statementObject.semgroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clique, id, name, semgroup);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatementObject {\n");
    
    sb.append("    clique: ").append(toIndentedString(clique)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    semgroup: ").append(toIndentedString(semgroup)).append("\n");
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

