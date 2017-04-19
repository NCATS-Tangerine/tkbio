package bio.knowledge.server.model;

import java.util.Objects;
import bio.knowledge.server.model.InlineResponse2002;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
/**
 * InlineResponse2003
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T13:50:33.049-07:00")

public class InlineResponse2003   {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("xrefs")
  private List<InlineResponse2002> xrefs = new ArrayList<InlineResponse2002>();

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

  public InlineResponse2003 xrefs(List<InlineResponse2002> xrefs) {
    this.xrefs = xrefs;
    return this;
  }

  public InlineResponse2003 addXrefsItem(InlineResponse2002 xrefsItem) {
    this.xrefs.add(xrefsItem);
    return this;
  }

   /**
   * Get xrefs
   * @return xrefs
  **/
  @ApiModelProperty(value = "")
  public List<InlineResponse2002> getXrefs() {
    return xrefs;
  }

  public void setXrefs(List<InlineResponse2002> xrefs) {
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
    InlineResponse2003 inlineResponse2003 = (InlineResponse2003) o;
    return Objects.equals(this.source, inlineResponse2003.source) &&
        Objects.equals(this.id, inlineResponse2003.id) &&
        Objects.equals(this.xrefs, inlineResponse2003.xrefs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, id, xrefs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2003 {\n");
    
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

