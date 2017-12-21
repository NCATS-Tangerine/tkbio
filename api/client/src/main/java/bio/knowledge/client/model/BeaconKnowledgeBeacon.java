/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.7
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
 * BeaconKnowledgeBeacon
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-12-20T20:06:18.404-08:00")
public class BeaconKnowledgeBeacon {
  @SerializedName("id")
  private String id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("url")
  private String url = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("contact")
  private String contact = null;

  @SerializedName("wraps")
  private String wraps = null;

  @SerializedName("repo")
  private String repo = null;

  public BeaconKnowledgeBeacon id(String id) {
    this.id = id;
    return this;
  }

   /**
   * beacon ID 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "beacon ID ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconKnowledgeBeacon name(String name) {
    this.name = name;
    return this;
  }

   /**
   * beacon name 
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "beacon name ")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BeaconKnowledgeBeacon url(String url) {
    this.url = url;
    return this;
  }

   /**
   * URL used to execute API calls on the beacon 
   * @return url
  **/
  @ApiModelProperty(example = "null", value = "URL used to execute API calls on the beacon ")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public BeaconKnowledgeBeacon description(String description) {
    this.description = description;
    return this;
  }

   /**
   * beacon description 
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "beacon description ")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BeaconKnowledgeBeacon contact(String contact) {
    this.contact = contact;
    return this;
  }

   /**
   * name of the person responsible for this beacon 
   * @return contact
  **/
  @ApiModelProperty(example = "null", value = "name of the person responsible for this beacon ")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public BeaconKnowledgeBeacon wraps(String wraps) {
    this.wraps = wraps;
    return this;
  }

   /**
   * URL of this beacon's data source 
   * @return wraps
  **/
  @ApiModelProperty(example = "null", value = "URL of this beacon's data source ")
  public String getWraps() {
    return wraps;
  }

  public void setWraps(String wraps) {
    this.wraps = wraps;
  }

  public BeaconKnowledgeBeacon repo(String repo) {
    this.repo = repo;
    return this;
  }

   /**
   * URL of this beacon's repository 
   * @return repo
  **/
  @ApiModelProperty(example = "null", value = "URL of this beacon's repository ")
  public String getRepo() {
    return repo;
  }

  public void setRepo(String repo) {
    this.repo = repo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconKnowledgeBeacon beaconKnowledgeBeacon = (BeaconKnowledgeBeacon) o;
    return Objects.equals(this.id, beaconKnowledgeBeacon.id) &&
        Objects.equals(this.name, beaconKnowledgeBeacon.name) &&
        Objects.equals(this.url, beaconKnowledgeBeacon.url) &&
        Objects.equals(this.description, beaconKnowledgeBeacon.description) &&
        Objects.equals(this.contact, beaconKnowledgeBeacon.contact) &&
        Objects.equals(this.wraps, beaconKnowledgeBeacon.wraps) &&
        Objects.equals(this.repo, beaconKnowledgeBeacon.repo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, url, description, contact, wraps, repo);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconKnowledgeBeacon {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    wraps: ").append(toIndentedString(wraps)).append("\n");
    sb.append("    repo: ").append(toIndentedString(repo)).append("\n");
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

