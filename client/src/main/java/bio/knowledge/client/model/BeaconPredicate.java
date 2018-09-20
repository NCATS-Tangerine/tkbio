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
import bio.knowledge.client.model.BeaconPredicatesByBeacon;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconPredicate
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-09-20T12:33:58.705-07:00")
public class BeaconPredicate {
  @SerializedName("id")
  private String id = null;

  @SerializedName("uri")
  private String uri = null;

  @SerializedName("edge_label")
  private String edgeLabel = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("beacons")
  private List<BeaconPredicatesByBeacon> beacons = new ArrayList<BeaconPredicatesByBeacon>();

  public BeaconPredicate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * the CURIE of the predicate relation (see [Biolink Model](https://biolink.github.io/biolink-model)
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "the CURIE of the predicate relation (see [Biolink Model](https://biolink.github.io/biolink-model)")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconPredicate uri(String uri) {
    this.uri = uri;
    return this;
  }

   /**
   * the URI of the predicate relation (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of URI)
   * @return uri
  **/
  @ApiModelProperty(example = "null", value = "the URI of the predicate relation (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of URI)")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public BeaconPredicate edgeLabel(String edgeLabel) {
    this.edgeLabel = edgeLabel;
    return this;
  }

   /**
   * the human readable 'edge label' of the 'minimal' predicate (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of Biolink Model minimal predicates)
   * @return edgeLabel
  **/
  @ApiModelProperty(example = "null", value = "the human readable 'edge label' of the 'minimal' predicate (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of Biolink Model minimal predicates)")
  public String getEdgeLabel() {
    return edgeLabel;
  }

  public void setEdgeLabel(String edgeLabel) {
    this.edgeLabel = edgeLabel;
  }

  public BeaconPredicate description(String description) {
    this.description = description;
    return this;
  }

   /**
   * human readable definition assigned by the beacon for the predicate relation 
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "human readable definition assigned by the beacon for the predicate relation ")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BeaconPredicate beacons(List<BeaconPredicatesByBeacon> beacons) {
    this.beacons = beacons;
    return this;
  }

  public BeaconPredicate addBeaconsItem(BeaconPredicatesByBeacon beaconsItem) {
    this.beacons.add(beaconsItem);
    return this;
  }

   /**
   * list of metadata for beacons that support the use of this predicate relation 
   * @return beacons
  **/
  @ApiModelProperty(example = "null", value = "list of metadata for beacons that support the use of this predicate relation ")
  public List<BeaconPredicatesByBeacon> getBeacons() {
    return beacons;
  }

  public void setBeacons(List<BeaconPredicatesByBeacon> beacons) {
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
    return Objects.equals(this.id, beaconPredicate.id) &&
        Objects.equals(this.uri, beaconPredicate.uri) &&
        Objects.equals(this.edgeLabel, beaconPredicate.edgeLabel) &&
        Objects.equals(this.description, beaconPredicate.description) &&
        Objects.equals(this.beacons, beaconPredicate.beacons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uri, edgeLabel, description, beacons);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconPredicate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    edgeLabel: ").append(toIndentedString(edgeLabel)).append("\n");
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

