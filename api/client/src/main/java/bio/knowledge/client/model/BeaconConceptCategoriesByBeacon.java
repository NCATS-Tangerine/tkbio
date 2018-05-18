/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.12
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.model;

import java.util.Objects;
import bio.knowledge.client.model.BeaconBeaconConceptCategory;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconConceptCategoriesByBeacon
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-18T11:21:57.544-07:00")
public class BeaconConceptCategoriesByBeacon {
  @SerializedName("beacon")
  private Integer beacon = null;

  @SerializedName("categories")
  private List<BeaconBeaconConceptCategory> categories = new ArrayList<BeaconBeaconConceptCategory>();

  public BeaconConceptCategoriesByBeacon beacon(Integer beacon) {
    this.beacon = beacon;
    return this;
  }

   /**
   * Aggregator index identifier of the given beacon 
   * @return beacon
  **/
  @ApiModelProperty(example = "null", value = "Aggregator index identifier of the given beacon ")
  public Integer getBeacon() {
    return beacon;
  }

  public void setBeacon(Integer beacon) {
    this.beacon = beacon;
  }

  public BeaconConceptCategoriesByBeacon categories(List<BeaconBeaconConceptCategory> categories) {
    this.categories = categories;
    return this;
  }

  public BeaconConceptCategoriesByBeacon addCategoriesItem(BeaconBeaconConceptCategory categoriesItem) {
    this.categories.add(categoriesItem);
    return this;
  }

   /**
   * Get categories
   * @return categories
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<BeaconBeaconConceptCategory> getCategories() {
    return categories;
  }

  public void setCategories(List<BeaconBeaconConceptCategory> categories) {
    this.categories = categories;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConceptCategoriesByBeacon beaconConceptCategoriesByBeacon = (BeaconConceptCategoriesByBeacon) o;
    return Objects.equals(this.beacon, beaconConceptCategoriesByBeacon.beacon) &&
        Objects.equals(this.categories, beaconConceptCategoriesByBeacon.categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(beacon, categories);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConceptCategoriesByBeacon {\n");
    
    sb.append("    beacon: ").append(toIndentedString(beacon)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
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

