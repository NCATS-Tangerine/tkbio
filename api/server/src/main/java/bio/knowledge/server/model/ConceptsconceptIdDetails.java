package bio.knowledge.server.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
/**
 * ConceptsconceptIdDetails
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

public class ConceptsconceptIdDetails   {
  @JsonProperty("tag")
  private String tag = null;

  @JsonProperty("value")
  private String value = null;

  public ConceptsconceptIdDetails tag(String tag) {
    this.tag = tag;
    return this;
  }

   /**
   * property name 
   * @return tag
  **/
  @ApiModelProperty(value = "property name ")
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public ConceptsconceptIdDetails value(String value) {
    this.value = value;
    return this;
  }

   /**
   * property value 
   * @return value
  **/
  @ApiModelProperty(value = "property value ")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConceptsconceptIdDetails conceptsconceptIdDetails = (ConceptsconceptIdDetails) o;
    return Objects.equals(this.tag, conceptsconceptIdDetails.tag) &&
        Objects.equals(this.value, conceptsconceptIdDetails.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tag, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConceptsconceptIdDetails {\n");
    
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

