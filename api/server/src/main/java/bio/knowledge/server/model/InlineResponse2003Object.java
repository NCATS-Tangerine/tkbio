package bio.knowledge.server.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
/**
 * CURIE-encoded identifier 
 */
@ApiModel(description = "CURIE-encoded identifier ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

public class InlineResponse2003Object   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  public InlineResponse2003Object id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InlineResponse2003Object name(String name) {
    this.name = name;
    return this;
  }

   /**
   * human readable label of concept
   * @return name
  **/
  @ApiModelProperty(value = "human readable label of concept")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2003Object inlineResponse2003Object = (InlineResponse2003Object) o;
    return Objects.equals(this.id, inlineResponse2003Object.id) &&
        Objects.equals(this.name, inlineResponse2003Object.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2003Object {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

