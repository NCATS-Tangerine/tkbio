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

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * BeaconConcept
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-12-06T00:28:23.617-08:00")
public class BeaconConcept {
  @SerializedName("clique")
  private String clique = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("aliases")
  private List<String> aliases = new ArrayList<String>();

  @SerializedName("name")
  private String name = null;

  @SerializedName("semanticGroup")
  private String semanticGroup = null;

  @SerializedName("synonyms")
  private List<String> synonyms = new ArrayList<String>();

  @SerializedName("definition")
  private String definition = null;

  @SerializedName("beacon")
  private String beacon = null;

  public BeaconConcept clique(String clique) {
    this.clique = clique;
    return this;
  }

   /**
   * CURIE identifying the inferred equivalent concept clique to which the concept belongs. This is assigned by an identifier precedence heuristic by the beacon-aggregator 
   * @return clique
  **/
  @ApiModelProperty(example = "null", value = "CURIE identifying the inferred equivalent concept clique to which the concept belongs. This is assigned by an identifier precedence heuristic by the beacon-aggregator ")
  public String getClique() {
    return clique;
  }

  public void setClique(String clique) {
    this.clique = clique;
  }

  public BeaconConcept id(String id) {
    this.id = id;
    return this;
  }

   /**
   * CURIE for the concept in the specified knowledge beacon 
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "CURIE for the concept in the specified knowledge beacon ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BeaconConcept aliases(List<String> aliases) {
    this.aliases = aliases;
    return this;
  }

  public BeaconConcept addAliasesItem(String aliasesItem) {
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

  public BeaconConcept name(String name) {
    this.name = name;
    return this;
  }

   /**
   * canonical human readable name of the concept 
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "canonical human readable name of the concept ")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BeaconConcept semanticGroup(String semanticGroup) {
    this.semanticGroup = semanticGroup;
    return this;
  }

   /**
   * concept semantic type 
   * @return semanticGroup
  **/
  @ApiModelProperty(example = "null", value = "concept semantic type ")
  public String getSemanticGroup() {
    return semanticGroup;
  }

  public void setSemanticGroup(String semanticGroup) {
    this.semanticGroup = semanticGroup;
  }

  public BeaconConcept synonyms(List<String> synonyms) {
    this.synonyms = synonyms;
    return this;
  }

  public BeaconConcept addSynonymsItem(String synonymsItem) {
    this.synonyms.add(synonymsItem);
    return this;
  }

   /**
   * list of synonyms for concept 
   * @return synonyms
  **/
  @ApiModelProperty(example = "null", value = "list of synonyms for concept ")
  public List<String> getSynonyms() {
    return synonyms;
  }

  public void setSynonyms(List<String> synonyms) {
    this.synonyms = synonyms;
  }

  public BeaconConcept definition(String definition) {
    this.definition = definition;
    return this;
  }

   /**
   * concept definition 
   * @return definition
  **/
  @ApiModelProperty(example = "null", value = "concept definition ")
  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  public BeaconConcept beacon(String beacon) {
    this.beacon = beacon;
    return this;
  }

   /**
   * beacon ID 
   * @return beacon
  **/
  @ApiModelProperty(example = "null", value = "beacon ID ")
  public String getBeacon() {
    return beacon;
  }

  public void setBeacon(String beacon) {
    this.beacon = beacon;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconConcept beaconConcept = (BeaconConcept) o;
    return Objects.equals(this.clique, beaconConcept.clique) &&
        Objects.equals(this.id, beaconConcept.id) &&
        Objects.equals(this.aliases, beaconConcept.aliases) &&
        Objects.equals(this.name, beaconConcept.name) &&
        Objects.equals(this.semanticGroup, beaconConcept.semanticGroup) &&
        Objects.equals(this.synonyms, beaconConcept.synonyms) &&
        Objects.equals(this.definition, beaconConcept.definition) &&
        Objects.equals(this.beacon, beaconConcept.beacon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clique, id, aliases, name, semanticGroup, synonyms, definition, beacon);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconConcept {\n");
    
    sb.append("    clique: ").append(toIndentedString(clique)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    aliases: ").append(toIndentedString(aliases)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    semanticGroup: ").append(toIndentedString(semanticGroup)).append("\n");
    sb.append("    synonyms: ").append(toIndentedString(synonyms)).append("\n");
    sb.append("    definition: ").append(toIndentedString(definition)).append("\n");
    sb.append("    beacon: ").append(toIndentedString(beacon)).append("\n");
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

