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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * BeaconConceptsQueryResult
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-05-18T11:46:52.023-07:00")
public class BeaconConceptsQueryResult {
  @SerializedName("queryId")
  private String queryId = null;

  @SerializedName("beacons")
  private List<Integer> beacons = new ArrayList<Integer>();

  @SerializedName("pageNumber")
  private Integer pageNumber = null;

  @SerializedName("pageSize")
  private Integer pageSize = null;

  @SerializedName("results")
  private List<BeaconConcept> results = new ArrayList<BeaconConcept>();

  public BeaconConceptsQueryResult queryId(String queryId) {
    this.queryId = queryId;
    return this;
  }

   /**
   * session identifier of the query returning the results 
   * @return queryId
  **/
  @ApiModelProperty(example = "null", value = "session identifier of the query returning the results ")
  public String getQueryId() {
    return queryId;
  }

  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  public BeaconConceptsQueryResult beacons(List<Integer> beacons) {
    this.beacons = beacons;
    return this;
  }

  public BeaconConceptsQueryResult addBeaconsItem(Integer beaconsItem) {
    this.beacons.add(beaconsItem);
    return this;
  }

   /**
   * Get beacons
   * @return beacons
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<Integer> getBeacons() {
    return beacons;
  }

  public void setBeacons(List<Integer> beacons) {
    this.beacons = beacons;
  }

  public BeaconConceptsQueryResult pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

   /**
   * session identifier of the query returning the results 
   * @return pageNumber
  **/
  @ApiModelProperty(example = "null", value = "session identifier of the query returning the results ")
  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public BeaconConceptsQueryResult pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

   /**
   * session identifier of the query returning the results 
   * @return pageSize
  **/
  @ApiModelProperty(example = "null", value = "session identifier of the query returning the results ")
  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public BeaconConceptsQueryResult results(List<BeaconConcept> results) {
    this.results = results;
    return this;
  }

  public BeaconConceptsQueryResult addResultsItem(BeaconConcept resultsItem) {
    this.results.add(resultsItem);
    return this;
  }

   /**
   * Get results
   * @return results
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<BeaconConcept> getResults() {
    return results;
  }

  public void setResults(List<BeaconConcept> results) {
    this.results = results;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConceptsQueryResult beaconConceptsQueryResult = (BeaconConceptsQueryResult) o;
    return Objects.equals(this.queryId, beaconConceptsQueryResult.queryId) &&
        Objects.equals(this.beacons, beaconConceptsQueryResult.beacons) &&
        Objects.equals(this.pageNumber, beaconConceptsQueryResult.pageNumber) &&
        Objects.equals(this.pageSize, beaconConceptsQueryResult.pageSize) &&
        Objects.equals(this.results, beaconConceptsQueryResult.results);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queryId, beacons, pageNumber, pageSize, results);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConceptsQueryResult {\n");
    
    sb.append("    queryId: ").append(toIndentedString(queryId)).append("\n");
    sb.append("    beacons: ").append(toIndentedString(beacons)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    results: ").append(toIndentedString(results)).append("\n");
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

