/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API). 
 *
 * OpenAPI spec version: 1.0.4
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Predicate
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-10-09T14:15:32.358-07:00")
public class Predicate {
  @SerializedName("id")
  private String id = null;

  @SerializedName("name")
  private String name = null;

  public Predicate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * CURIE-encoded identifier of predicate resource 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "CURIE-encoded identifier of predicate resource ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Predicate name(String name) {
    this.name = name;
    return this;
  }

   /**
   * human readable label of concept
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "human readable label of concept")
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
    Predicate predicate = (Predicate) o;
    return Objects.equals(this.id, predicate.id) &&
        Objects.equals(this.name, predicate.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Predicate {\n");
    
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

