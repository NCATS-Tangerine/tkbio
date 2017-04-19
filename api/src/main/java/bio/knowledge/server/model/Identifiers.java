package bio.knowledge.server.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
/**
 * Identifiers
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

public class Identifiers   {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("id")
  private String id = null;

  public Identifiers source(String source) {
    this.source = source;
    return this;
  }

   /**
   * Get source
   * @return source
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Identifiers id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Identifiers identifiers = (Identifiers) o;
    return Objects.equals(this.source, identifiers.source) &&
        Objects.equals(this.id, identifiers.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Identifiers {\n");
    
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

