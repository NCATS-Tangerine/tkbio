package bio.knowledge.server.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
/**
 * InlineResponse2003Evidence
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

public class InlineResponse2003Evidence   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("count")
  private Integer count = null;

  public InlineResponse2003Evidence id(String id) {
    this.id = id;
    return this;
  }

   /**
   * local identifier to evidence record
   * @return id
  **/
  @ApiModelProperty(value = "local identifier to evidence record")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InlineResponse2003Evidence count(Integer count) {
    this.count = count;
    return this;
  }

   /**
   * Get count
   * @return count
  **/
  @ApiModelProperty(value = "")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2003Evidence inlineResponse2003Evidence = (InlineResponse2003Evidence) o;
    return Objects.equals(this.id, inlineResponse2003Evidence.id) &&
        Objects.equals(this.count, inlineResponse2003Evidence.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2003Evidence {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

