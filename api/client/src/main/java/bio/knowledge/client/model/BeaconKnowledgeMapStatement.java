/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.1.0
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.model;

import java.util.Objects;
import bio.knowledge.client.model.BeaconKnowledgeMapObject;
import bio.knowledge.client.model.BeaconKnowledgeMapPredicate;
import bio.knowledge.client.model.BeaconKnowledgeMapSubject;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BeaconKnowledgeMapStatement
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-19T16:31:52.979-07:00")
public class BeaconKnowledgeMapStatement {
  @SerializedName("subject")
  private BeaconKnowledgeMapSubject subject = null;

  @SerializedName("predicate")
  private BeaconKnowledgeMapPredicate predicate = null;

  @SerializedName("object")
  private BeaconKnowledgeMapObject object = null;

  @SerializedName("frequency")
  private Integer frequency = null;

  @SerializedName("description")
  private String description = null;

  public BeaconKnowledgeMapStatement subject(BeaconKnowledgeMapSubject subject) {
    this.subject = subject;
    return this;
  }

   /**
   * Get subject
   * @return subject
  **/
  @ApiModelProperty(example = "null", value = "")
  public BeaconKnowledgeMapSubject getSubject() {
    return subject;
  }

  public void setSubject(BeaconKnowledgeMapSubject subject) {
    this.subject = subject;
  }

  public BeaconKnowledgeMapStatement predicate(BeaconKnowledgeMapPredicate predicate) {
    this.predicate = predicate;
    return this;
  }

   /**
   * Get predicate
   * @return predicate
  **/
  @ApiModelProperty(example = "null", value = "")
  public BeaconKnowledgeMapPredicate getPredicate() {
    return predicate;
  }

  public void setPredicate(BeaconKnowledgeMapPredicate predicate) {
    this.predicate = predicate;
  }

  public BeaconKnowledgeMapStatement object(BeaconKnowledgeMapObject object) {
    this.object = object;
    return this;
  }

   /**
   * Get object
   * @return object
  **/
  @ApiModelProperty(example = "null", value = "")
  public BeaconKnowledgeMapObject getObject() {
    return object;
  }

  public void setObject(BeaconKnowledgeMapObject object) {
    this.object = object;
  }

  public BeaconKnowledgeMapStatement frequency(Integer frequency) {
    this.frequency = frequency;
    return this;
  }

   /**
   * the frequency of statements of the specified relationship within the given beacon 
   * @return frequency
  **/
  @ApiModelProperty(example = "null", value = "the frequency of statements of the specified relationship within the given beacon ")
  public Integer getFrequency() {
    return frequency;
  }

  public void setFrequency(Integer frequency) {
    this.frequency = frequency;
  }

  public BeaconKnowledgeMapStatement description(String description) {
    this.description = description;
    return this;
  }

   /**
   * a description of the nature of the relationship 
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "a description of the nature of the relationship ")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconKnowledgeMapStatement beaconKnowledgeMapStatement = (BeaconKnowledgeMapStatement) o;
    return Objects.equals(this.subject, beaconKnowledgeMapStatement.subject) &&
        Objects.equals(this.predicate, beaconKnowledgeMapStatement.predicate) &&
        Objects.equals(this.object, beaconKnowledgeMapStatement.object) &&
        Objects.equals(this.frequency, beaconKnowledgeMapStatement.frequency) &&
        Objects.equals(this.description, beaconKnowledgeMapStatement.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, predicate, object, frequency, description);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconKnowledgeMapStatement {\n");
    
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    predicate: ").append(toIndentedString(predicate)).append("\n");
    sb.append("    object: ").append(toIndentedString(object)).append("\n");
    sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

