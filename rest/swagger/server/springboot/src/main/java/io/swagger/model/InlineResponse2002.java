package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.InlineResponse2001;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
/**
 * InlineResponse2002
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-17T22:13:20.980-07:00")

public class InlineResponse2002   {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("xrefs")
  private List<InlineResponse2001> xrefs = new ArrayList<InlineResponse2001>();

  public InlineResponse2002 source(String source) {
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

  public InlineResponse2002 xrefs(List<InlineResponse2001> xrefs) {
    this.xrefs = xrefs;
    return this;
  }

  public InlineResponse2002 addXrefsItem(InlineResponse2001 xrefsItem) {
    this.xrefs.add(xrefsItem);
    return this;
  }

   /**
   * Get xrefs
   * @return xrefs
  **/
  @ApiModelProperty(value = "")
  public List<InlineResponse2001> getXrefs() {
    return xrefs;
  }

  public void setXrefs(List<InlineResponse2001> xrefs) {
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
    return Objects.equals(this.source, inlineResponse2002.source) &&
        Objects.equals(this.id, inlineResponse2002.id) &&
        Objects.equals(this.xrefs, inlineResponse2002.xrefs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, id, xrefs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2002 {\n");
    
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

