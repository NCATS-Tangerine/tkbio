package bio.knowledge.server.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
/**
 * InlineResponse2001
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

public class InlineResponse2001   {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("semanticType")
  private String semanticType = null;

  @JsonProperty("synonyms")
  private List<String> synonyms = new ArrayList<String>();

  @JsonProperty("definition")
  private String definition = null;

  public InlineResponse2001 source(String source) {
    this.source = source;
    return this;
  }

   /**
   * knowledge source name
   * @return source
  **/
  @ApiModelProperty(value = "knowledge source name")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public InlineResponse2001 id(String id) {
    this.id = id;
    return this;
  }

   /**
   * object identifier for the concept in the specified knowledge source database 
   * @return id
  **/
  @ApiModelProperty(value = "object identifier for the concept in the specified knowledge source database ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InlineResponse2001 name(String name) {
    this.name = name;
    return this;
  }

   /**
   * canonical human readable name of the concept 
   * @return name
  **/
  @ApiModelProperty(value = "canonical human readable name of the concept ")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public InlineResponse2001 semanticType(String semanticType) {
    this.semanticType = semanticType;
    return this;
  }

   /**
   * concept semantic type 
   * @return semanticType
  **/
  @ApiModelProperty(value = "concept semantic type ")
  public String getSemanticType() {
    return semanticType;
  }

  public void setSemanticType(String semanticType) {
    this.semanticType = semanticType;
  }

  public InlineResponse2001 synonyms(List<String> synonyms) {
    this.synonyms = synonyms;
    return this;
  }

  public InlineResponse2001 addSynonymsItem(String synonymsItem) {
    this.synonyms.add(synonymsItem);
    return this;
  }

   /**
   * list of synonyms for concept 
   * @return synonyms
  **/
  @ApiModelProperty(value = "list of synonyms for concept ")
  public List<String> getSynonyms() {
    return synonyms;
  }

  public void setSynonyms(List<String> synonyms) {
    this.synonyms = synonyms;
  }

  public InlineResponse2001 definition(String definition) {
    this.definition = definition;
    return this;
  }

   /**
   * concept definition 
   * @return definition
  **/
  @ApiModelProperty(value = "concept definition ")
  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2001 inlineResponse2001 = (InlineResponse2001) o;
    return Objects.equals(this.source, inlineResponse2001.source) &&
        Objects.equals(this.id, inlineResponse2001.id) &&
        Objects.equals(this.name, inlineResponse2001.name) &&
        Objects.equals(this.semanticType, inlineResponse2001.semanticType) &&
        Objects.equals(this.synonyms, inlineResponse2001.synonyms) &&
        Objects.equals(this.definition, inlineResponse2001.definition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, id, name, semanticType, synonyms, definition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2001 {\n");
    
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    semanticType: ").append(toIndentedString(semanticType)).append("\n");
    sb.append("    synonyms: ").append(toIndentedString(synonyms)).append("\n");
    sb.append("    definition: ").append(toIndentedString(definition)).append("\n");
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

