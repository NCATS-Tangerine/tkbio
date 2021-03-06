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
import bio.knowledge.client.model.BeaconConceptWithDetailsBeaconEntry;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * A single record of a given concept clique with details 
 */
@ApiModel(description = "A single record of a given concept clique with details ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-18T18:21:45.192Z")
public class BeaconConceptWithDetails {
  @SerializedName("clique")
  private String clique = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("categories")
  private List<String> categories = new ArrayList<String>();

  @SerializedName("aliases")
  private List<String> aliases = new ArrayList<String>();

  @SerializedName("entries")
  private List<BeaconConceptWithDetailsBeaconEntry> entries = new ArrayList<BeaconConceptWithDetailsBeaconEntry>();

  public BeaconConceptWithDetails clique(String clique) {
    this.clique = clique;
    return this;
  }

   /**
   * CURIE identifying the equivalent concept clique to which the concept belongs. 
   * @return clique
  **/
  @ApiModelProperty(example = "null", value = "CURIE identifying the equivalent concept clique to which the concept belongs. ")
  public String getClique() {
    return clique;
  }

  public void setClique(String clique) {
    this.clique = clique;
  }

  public BeaconConceptWithDetails name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Canonical human readable name of the key concept of the clique 
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "Canonical human readable name of the key concept of the clique ")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BeaconConceptWithDetails categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public BeaconConceptWithDetails addCategoriesItem(String categoriesItem) {
    this.categories.add(categoriesItem);
    return this;
  }

   /**
   * Concept semantic type as a CURIE into a data type ontology 
   * @return categories
  **/
  @ApiModelProperty(example = "null", value = "Concept semantic type as a CURIE into a data type ontology ")
  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public BeaconConceptWithDetails aliases(List<String> aliases) {
    this.aliases = aliases;
    return this;
  }

  public BeaconConceptWithDetails addAliasesItem(String aliasesItem) {
    this.aliases.add(aliasesItem);
    return this;
  }

   /**
   * set of alias CURIES in the equivalent concept clique of the concept 
   * @return aliases
  **/
  @ApiModelProperty(example = "null", value = "set of alias CURIES in the equivalent concept clique of the concept ")
  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }

  public BeaconConceptWithDetails entries(List<BeaconConceptWithDetailsBeaconEntry> entries) {
    this.entries = entries;
    return this;
  }

  public BeaconConceptWithDetails addEntriesItem(BeaconConceptWithDetailsBeaconEntry entriesItem) {
    this.entries.add(entriesItem);
    return this;
  }

   /**
   * List of details specifically harvested from beacons, indexed by beacon 
   * @return entries
  **/
  @ApiModelProperty(example = "null", value = "List of details specifically harvested from beacons, indexed by beacon ")
  public List<BeaconConceptWithDetailsBeaconEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<BeaconConceptWithDetailsBeaconEntry> entries) {
    this.entries = entries;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConceptWithDetails beaconConceptWithDetails = (BeaconConceptWithDetails) o;
    return Objects.equals(this.clique, beaconConceptWithDetails.clique) &&
        Objects.equals(this.name, beaconConceptWithDetails.name) &&
        Objects.equals(this.categories, beaconConceptWithDetails.categories) &&
        Objects.equals(this.aliases, beaconConceptWithDetails.aliases) &&
        Objects.equals(this.entries, beaconConceptWithDetails.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clique, name, categories, aliases, entries);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConceptWithDetails {\n");
    
    sb.append("    clique: ").append(toIndentedString(clique)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    aliases: ").append(toIndentedString(aliases)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
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

