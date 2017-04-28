package bio.knowledge.server.model;

import java.util.Objects;
import bio.knowledge.server.model.ExactMatchesExactMatches;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
/**
 * InlineResponse2002
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

public class InlineResponse2002   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("exactMatches")
  private List<ExactMatchesExactMatches> exactMatches = new ArrayList<ExactMatchesExactMatches>();

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

  public InlineResponse2002 exactMatches(List<ExactMatchesExactMatches> exactMatches) {
    this.exactMatches = exactMatches;
    return this;
  }

  public InlineResponse2002 addExactMatchesItem(ExactMatchesExactMatches exactMatchesItem) {
    this.exactMatches.add(exactMatchesItem);
    return this;
  }

   /**
   * Get exactMatches
   * @return exactMatches
  **/
  @ApiModelProperty(value = "")
  public List<ExactMatchesExactMatches> getExactMatches() {
    return exactMatches;
  }

  public void setExactMatches(List<ExactMatchesExactMatches> exactMatches) {
    this.exactMatches = exactMatches;
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
        Objects.equals(this.exactMatches, inlineResponse2002.exactMatches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, exactMatches);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2002 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    exactMatches: ").append(toIndentedString(exactMatches)).append("\n");
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

