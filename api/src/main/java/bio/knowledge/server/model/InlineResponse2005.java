package bio.knowledge.server.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
/**
 * InlineResponse2005
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

public class InlineResponse2005   {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("publicationDate")
  private String publicationDate = null;

  public InlineResponse2005 source(String source) {
    this.source = source;
    return this;
  }

   /**
   * evidence source name e.g. pubmed 
   * @return source
  **/
  @ApiModelProperty(value = "evidence source name e.g. pubmed ")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public InlineResponse2005 id(String id) {
    this.id = id;
    return this;
  }

   /**
   * object identifier for the cited reference 
   * @return id
  **/
  @ApiModelProperty(value = "object identifier for the cited reference ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InlineResponse2005 name(String name) {
    this.name = name;
    return this;
  }

   /**
   * canonical human readable title of the cited reference 
   * @return name
  **/
  @ApiModelProperty(value = "canonical human readable title of the cited reference ")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public InlineResponse2005 publicationDate(String publicationDate) {
    this.publicationDate = publicationDate;
    return this;
  }

   /**
   * publication date of reference (yyyy-mm-dd) 
   * @return publicationDate
  **/
  @ApiModelProperty(value = "publication date of reference (yyyy-mm-dd) ")
  public String getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(String publicationDate) {
    this.publicationDate = publicationDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2005 inlineResponse2005 = (InlineResponse2005) o;
    return Objects.equals(this.source, inlineResponse2005.source) &&
        Objects.equals(this.id, inlineResponse2005.id) &&
        Objects.equals(this.name, inlineResponse2005.name) &&
        Objects.equals(this.publicationDate, inlineResponse2005.publicationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, id, name, publicationDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2005 {\n");
    
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    publicationDate: ").append(toIndentedString(publicationDate)).append("\n");
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

