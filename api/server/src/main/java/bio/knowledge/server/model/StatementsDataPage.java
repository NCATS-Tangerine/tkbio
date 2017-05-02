package bio.knowledge.server.model;

import java.util.Objects;
import bio.knowledge.server.model.StatementsEvidence;
import bio.knowledge.server.model.StatementsObject;
import bio.knowledge.server.model.StatementsPredicate;
import bio.knowledge.server.model.StatementsSubject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
/**
 * StatementsDataPage
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-02T09:47:16.640-07:00")

public class StatementsDataPage   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("subject")
  private StatementsSubject subject = null;

  @JsonProperty("predicate")
  private StatementsPredicate predicate = null;

  @JsonProperty("object")
  private StatementsObject object = null;

  @JsonProperty("evidence")
  private StatementsEvidence evidence = null;

  public StatementsDataPage id(String id) {
    this.id = id;
    return this;
  }

   /**
   * local statement identifiers
   * @return id
  **/
  @ApiModelProperty(value = "local statement identifiers")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public StatementsDataPage subject(StatementsSubject subject) {
    this.subject = subject;
    return this;
  }

   /**
   * Get subject
   * @return subject
  **/
  @ApiModelProperty(value = "")
  public StatementsSubject getSubject() {
    return subject;
  }

  public void setSubject(StatementsSubject subject) {
    this.subject = subject;
  }

  public StatementsDataPage predicate(StatementsPredicate predicate) {
    this.predicate = predicate;
    return this;
  }

   /**
   * Get predicate
   * @return predicate
  **/
  @ApiModelProperty(value = "")
  public StatementsPredicate getPredicate() {
    return predicate;
  }

  public void setPredicate(StatementsPredicate predicate) {
    this.predicate = predicate;
  }

  public StatementsDataPage object(StatementsObject object) {
    this.object = object;
    return this;
  }

   /**
   * Get object
   * @return object
  **/
  @ApiModelProperty(value = "")
  public StatementsObject getObject() {
    return object;
  }

  public void setObject(StatementsObject object) {
    this.object = object;
  }

  public StatementsDataPage evidence(StatementsEvidence evidence) {
    this.evidence = evidence;
    return this;
  }

   /**
   * Get evidence
   * @return evidence
  **/
  @ApiModelProperty(value = "")
  public StatementsEvidence getEvidence() {
    return evidence;
  }

  public void setEvidence(StatementsEvidence evidence) {
    this.evidence = evidence;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatementsDataPage statementsDataPage = (StatementsDataPage) o;
    return Objects.equals(this.id, statementsDataPage.id) &&
        Objects.equals(this.subject, statementsDataPage.subject) &&
        Objects.equals(this.predicate, statementsDataPage.predicate) &&
        Objects.equals(this.object, statementsDataPage.object) &&
        Objects.equals(this.evidence, statementsDataPage.evidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, subject, predicate, object, evidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatementsDataPage {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    predicate: ").append(toIndentedString(predicate)).append("\n");
    sb.append("    object: ").append(toIndentedString(object)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
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

