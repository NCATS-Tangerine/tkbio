package bio.knowledge.model;

import java.util.Objects;
import bio.knowledge.model.InlineResponse2001;
import bio.knowledge.model.StatementsEvidence;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
/**
 * InlineResponse2003
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-18T15:45:38.310-07:00")

public class InlineResponse2003   {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("subject")
  private InlineResponse2001 subject = null;

  @JsonProperty("predicate")
  private InlineResponse2001 predicate = null;

  @JsonProperty("object")
  private InlineResponse2001 object = null;

  @JsonProperty("evidence")
  private StatementsEvidence evidence = null;

  public InlineResponse2003 source(String source) {
    this.source = source;
    return this;
  }

   /**
   * Get source
   * @return source
  **/
  @ApiModelProperty(value = "")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public InlineResponse2003 id(String id) {
    this.id = id;
    return this;
  }

   /**
   * predicate identifiers
   * @return id
  **/
  @ApiModelProperty(value = "predicate identifiers")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InlineResponse2003 subject(InlineResponse2001 subject) {
    this.subject = subject;
    return this;
  }

   /**
   * Get subject
   * @return subject
  **/
  @ApiModelProperty(value = "")
  public InlineResponse2001 getSubject() {
    return subject;
  }

  public void setSubject(InlineResponse2001 subject) {
    this.subject = subject;
  }

  public InlineResponse2003 predicate(InlineResponse2001 predicate) {
    this.predicate = predicate;
    return this;
  }

   /**
   * Get predicate
   * @return predicate
  **/
  @ApiModelProperty(value = "")
  public InlineResponse2001 getPredicate() {
    return predicate;
  }

  public void setPredicate(InlineResponse2001 predicate) {
    this.predicate = predicate;
  }

  public InlineResponse2003 object(InlineResponse2001 object) {
    this.object = object;
    return this;
  }

   /**
   * Get object
   * @return object
  **/
  @ApiModelProperty(value = "")
  public InlineResponse2001 getObject() {
    return object;
  }

  public void setObject(InlineResponse2001 object) {
    this.object = object;
  }

  public InlineResponse2003 evidence(StatementsEvidence evidence) {
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
    InlineResponse2003 inlineResponse2003 = (InlineResponse2003) o;
    return Objects.equals(this.source, inlineResponse2003.source) &&
        Objects.equals(this.id, inlineResponse2003.id) &&
        Objects.equals(this.subject, inlineResponse2003.subject) &&
        Objects.equals(this.predicate, inlineResponse2003.predicate) &&
        Objects.equals(this.object, inlineResponse2003.object) &&
        Objects.equals(this.evidence, inlineResponse2003.evidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, id, subject, predicate, object, evidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2003 {\n");
    
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

