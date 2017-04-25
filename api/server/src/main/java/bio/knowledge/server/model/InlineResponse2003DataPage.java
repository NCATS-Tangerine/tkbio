package bio.knowledge.server.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * InlineResponse2003DataPage
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T13:55:25.453-07:00")

public class InlineResponse2003DataPage   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("subject")
  private InlineResponse2003Subject subject = null;

  @JsonProperty("predicate")
  private InlineResponse2003Subject predicate = null;

  @JsonProperty("object")
  private InlineResponse2003Subject object = null;

  @JsonProperty("evidence")
  private InlineResponse2003Evidence evidence = null;

  public InlineResponse2003DataPage id(String id) {
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

  public InlineResponse2003DataPage subject(InlineResponse2003Subject subject) {
    this.subject = subject;
    return this;
  }

   /**
   * Get subject
   * @return subject
  **/
  @ApiModelProperty(value = "")
  public InlineResponse2003Subject getSubject() {
    return subject;
  }

  public void setSubject(InlineResponse2003Subject subject) {
    this.subject = subject;
  }

  public InlineResponse2003DataPage predicate(InlineResponse2003Subject predicate) {
    this.predicate = predicate;
    return this;
  }

   /**
   * Get predicate
   * @return predicate
  **/
  @ApiModelProperty(value = "")
  public InlineResponse2003Subject getPredicate() {
    return predicate;
  }

  public void setPredicate(InlineResponse2003Subject predicate) {
    this.predicate = predicate;
  }

  public InlineResponse2003DataPage object(InlineResponse2003Subject object) {
    this.object = object;
    return this;
  }

   /**
   * Get object
   * @return object
  **/
  @ApiModelProperty(value = "")
  public InlineResponse2003Subject getObject() {
    return object;
  }

  public void setObject(InlineResponse2003Subject object) {
    this.object = object;
  }

  public InlineResponse2003DataPage evidence(InlineResponse2003Evidence evidence) {
    this.evidence = evidence;
    return this;
  }

   /**
   * Get evidence
   * @return evidence
  **/
  @ApiModelProperty(value = "")
  public InlineResponse2003Evidence getEvidence() {
    return evidence;
  }

  public void setEvidence(InlineResponse2003Evidence evidence) {
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
    InlineResponse2003DataPage inlineResponse2003DataPage = (InlineResponse2003DataPage) o;
    return Objects.equals(this.id, inlineResponse2003DataPage.id) &&
        Objects.equals(this.subject, inlineResponse2003DataPage.subject) &&
        Objects.equals(this.predicate, inlineResponse2003DataPage.predicate) &&
        Objects.equals(this.object, inlineResponse2003DataPage.object) &&
        Objects.equals(this.evidence, inlineResponse2003DataPage.evidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, subject, predicate, object, evidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2003DataPage {\n");
    
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

