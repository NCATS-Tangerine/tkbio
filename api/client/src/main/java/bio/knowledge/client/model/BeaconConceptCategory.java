/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.1.1
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.model;

import java.util.Objects;
import bio.knowledge.client.model.BeaconConceptCategoriesByBeacon;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconConceptCategory
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-18T18:21:45.192Z")
public class BeaconConceptCategory {
  @SerializedName("id")
  private String id = null;

  @SerializedName("uri")
  private String uri = null;

  @SerializedName("category")
  private String category = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("beacons")
  private List<BeaconConceptCategoriesByBeacon> beacons = new ArrayList<BeaconConceptCategoriesByBeacon>();

  public BeaconConceptCategory id(String id) {
    this.id = id;
    return this;
  }

   /**
   * the CURIE of the concept category (see [Biolink Model Classes](https://biolink.github.io/biolink-model)
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "the CURIE of the concept category (see [Biolink Model Classes](https://biolink.github.io/biolink-model)")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconConceptCategory uri(String uri) {
    this.uri = uri;
    return this;
  }

   /**
   * the URI of the concept category (see [Biolink Model Classes](https://biolink.github.io/biolink-model)  for the full list of URI)
   * @return uri
  **/
  @ApiModelProperty(example = "null", value = "the URI of the concept category (see [Biolink Model Classes](https://biolink.github.io/biolink-model)  for the full list of URI)")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public BeaconConceptCategory category(String category) {
    this.category = category;
    return this;
  }

   /**
   * the human readable label of the concept category (see [Biolink Model Classes](https://biolink.github.io/biolink-model) for  the full list of concept categories) 
   * @return category
  **/
  @ApiModelProperty(example = "null", value = "the human readable label of the concept category (see [Biolink Model Classes](https://biolink.github.io/biolink-model) for  the full list of concept categories) ")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public BeaconConceptCategory description(String description) {
    this.description = description;
    return this;
  }

   /**
   * human readable definition assigned by the beacon for the specified concept category 
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "human readable definition assigned by the beacon for the specified concept category ")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BeaconConceptCategory beacons(List<BeaconConceptCategoriesByBeacon> beacons) {
    this.beacons = beacons;
    return this;
  }

  public BeaconConceptCategory addBeaconsItem(BeaconConceptCategoriesByBeacon beaconsItem) {
    this.beacons.add(beaconsItem);
    return this;
  }

   /**
   * list of metadata for beacons that support the use of this concept category 
   * @return beacons
  **/
  @ApiModelProperty(example = "null", value = "list of metadata for beacons that support the use of this concept category ")
  public List<BeaconConceptCategoriesByBeacon> getBeacons() {
    return beacons;
  }

  public void setBeacons(List<BeaconConceptCategoriesByBeacon> beacons) {
    this.beacons = beacons;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConceptCategory beaconConceptCategory = (BeaconConceptCategory) o;
    return Objects.equals(this.id, beaconConceptCategory.id) &&
        Objects.equals(this.uri, beaconConceptCategory.uri) &&
        Objects.equals(this.category, beaconConceptCategory.category) &&
        Objects.equals(this.description, beaconConceptCategory.description) &&
        Objects.equals(this.beacons, beaconConceptCategory.beacons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uri, category, description, beacons);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConceptCategory {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    beacons: ").append(toIndentedString(beacons)).append("\n");
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

