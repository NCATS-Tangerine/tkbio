package bio.knowledge.server.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * InlineResponse2004DataPage
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T13:55:25.453-07:00")

public class InlineResponse2004DataPage   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("publicationDate")
  private String publicationDate = null;

  public InlineResponse2004DataPage id(String id) {
    this.id = id;
    return this;
  }

   /**
   * qualified object evidence identifier for the cited reference 
   * @return id
  **/
  @ApiModelProperty(value = "qualified object evidence identifier for the cited reference ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InlineResponse2004DataPage name(String name) {
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

  public InlineResponse2004DataPage publicationDate(String publicationDate) {
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
    InlineResponse2004DataPage inlineResponse2004DataPage = (InlineResponse2004DataPage) o;
    return Objects.equals(this.id, inlineResponse2004DataPage.id) &&
        Objects.equals(this.name, inlineResponse2004DataPage.name) &&
        Objects.equals(this.publicationDate, inlineResponse2004DataPage.publicationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, publicationDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2004DataPage {\n");
    
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

