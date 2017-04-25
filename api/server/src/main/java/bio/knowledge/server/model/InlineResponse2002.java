package bio.knowledge.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * InlineResponse2002
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T13:55:25.453-07:00")

public class InlineResponse2002   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("xrefs")
  private List<String> xrefs = new ArrayList<String>();

  public InlineResponse2002 id(String id) {
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

  public InlineResponse2002 xrefs(List<String> xrefs) {
    this.xrefs = xrefs;
    return this;
  }

  public InlineResponse2002 addXrefsItem(String xrefsItem) {
    this.xrefs.add(xrefsItem);
    return this;
  }

   /**
   * Get xrefs
   * @return xrefs
  **/
  @ApiModelProperty(value = "")
  public List<String> getXrefs() {
    return xrefs;
  }

  public void setXrefs(List<String> xrefs) {
    this.xrefs = xrefs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2002 inlineResponse2002 = (InlineResponse2002) o;
    return Objects.equals(this.id, inlineResponse2002.id) &&
        Objects.equals(this.xrefs, inlineResponse2002.xrefs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, xrefs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2002 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    xrefs: ").append(toIndentedString(xrefs)).append("\n");
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

