/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.6
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * BeaconPredicate
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-12-06T00:37:22.296-08:00")
public class BeaconPredicate {
  @SerializedName("name")
  private String name = null;

  @SerializedName("beacons")
  private List<BeaconPredicateRecord> beacons = new ArrayList<BeaconPredicateRecord>();

  public BeaconPredicate name(String name) {
    this.name = name;
    return this;
  }

   /**
   * exact unique human readable name of predicate relation 
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "exact unique human readable name of predicate relation ")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BeaconPredicate beacons(List<BeaconPredicateRecord> beacons) {
    this.beacons = beacons;
    return this;
  }

  public BeaconPredicate addBeaconsItem(BeaconPredicateRecord beaconsItem) {
    this.beacons.add(beaconsItem);
    return this;
  }

   /**
   * list of metadata for beacons that support the use of this predicate relation 
   * @return beacons
  **/
  @ApiModelProperty(example = "null", value = "list of metadata for beacons that support the use of this predicate relation ")
  public List<BeaconPredicateRecord> getBeacons() {
    return beacons;
  }

  public void setBeacons(List<BeaconPredicateRecord> beacons) {
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
    BeaconPredicate beaconPredicate = (BeaconPredicate) o;
    return Objects.equals(this.name, beaconPredicate.name) &&
        Objects.equals(this.beacons, beaconPredicate.beacons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, beacons);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconPredicate {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
