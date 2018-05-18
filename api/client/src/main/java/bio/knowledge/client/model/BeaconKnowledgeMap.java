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
import bio.knowledge.client.model.BeaconKnowledgeMapStatement;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconKnowledgeMap
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-18T11:21:57.544-07:00")
public class BeaconKnowledgeMap {
  @SerializedName("beacon")
  private Integer beacon = null;

  @SerializedName("statements")
  private List<BeaconKnowledgeMapStatement> statements = new ArrayList<BeaconKnowledgeMapStatement>();

  public BeaconKnowledgeMap beacon(Integer beacon) {
    this.beacon = beacon;
    return this;
  }

   /**
   * aggregator assigned beacon index identifier 
   * @return beacon
  **/
  @ApiModelProperty(example = "null", value = "aggregator assigned beacon index identifier ")
  public Integer getBeacon() {
    return beacon;
  }

  public void setBeacon(Integer beacon) {
    this.beacon = beacon;
  }

  public BeaconKnowledgeMap statements(List<BeaconKnowledgeMapStatement> statements) {
    this.statements = statements;
    return this;
  }

  public BeaconKnowledgeMap addStatementsItem(BeaconKnowledgeMapStatement statementsItem) {
    this.statements.add(statementsItem);
    return this;
  }

   /**
   * Get statements
   * @return statements
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<BeaconKnowledgeMapStatement> getStatements() {
    return statements;
  }

  public void setStatements(List<BeaconKnowledgeMapStatement> statements) {
    this.statements = statements;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconKnowledgeMap beaconKnowledgeMap = (BeaconKnowledgeMap) o;
    return Objects.equals(this.beacon, beaconKnowledgeMap.beacon) &&
        Objects.equals(this.statements, beaconKnowledgeMap.statements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(beacon, statements);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconKnowledgeMap {\n");
    
    sb.append("    beacon: ").append(toIndentedString(beacon)).append("\n");
    sb.append("    statements: ").append(toIndentedString(statements)).append("\n");
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

