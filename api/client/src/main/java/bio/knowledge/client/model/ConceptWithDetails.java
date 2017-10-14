/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
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
import bio.knowledge.client.model.ConceptDetail;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * ConceptWithDetails
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-10-13T21:43:21.781-07:00")
public class ConceptWithDetails {
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

  @SerializedName("details")
  private List<ConceptDetail> details = new ArrayList<ConceptDetail>();

  @SerializedName("beacon")
  private String beacon = null;

  public ConceptWithDetails clique(String clique) {
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

  public ConceptWithDetails id(String id) {
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

  public ConceptWithDetails aliases(List<String> aliases) {
    this.aliases = aliases;
    return this;
  }

  public ConceptWithDetails addAliasesItem(String aliasesItem) {
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

  public ConceptWithDetails name(String name) {
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

  public ConceptWithDetails semanticGroup(String semanticGroup) {
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

  public ConceptWithDetails synonyms(List<String> synonyms) {
    this.synonyms = synonyms;
    return this;
  }

  public ConceptWithDetails addSynonymsItem(String synonymsItem) {
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

  public ConceptWithDetails definition(String definition) {
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

  public ConceptWithDetails details(List<ConceptDetail> details) {
    this.details = details;
    return this;
  }

  public ConceptWithDetails addDetailsItem(ConceptDetail detailsItem) {
    this.details.add(detailsItem);
    return this;
  }

   /**
   * Get details
   * @return details
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<ConceptDetail> getDetails() {
    return details;
  }

  public void setDetails(List<ConceptDetail> details) {
    this.details = details;
  }

  public ConceptWithDetails beacon(String beacon) {
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
    ConceptWithDetails conceptWithDetails = (ConceptWithDetails) o;
    return Objects.equals(this.clique, conceptWithDetails.clique) &&
        Objects.equals(this.id, conceptWithDetails.id) &&
        Objects.equals(this.aliases, conceptWithDetails.aliases) &&
        Objects.equals(this.name, conceptWithDetails.name) &&
        Objects.equals(this.semanticGroup, conceptWithDetails.semanticGroup) &&
        Objects.equals(this.synonyms, conceptWithDetails.synonyms) &&
        Objects.equals(this.definition, conceptWithDetails.definition) &&
        Objects.equals(this.details, conceptWithDetails.details) &&
        Objects.equals(this.beacon, conceptWithDetails.beacon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clique, id, aliases, name, semanticGroup, synonyms, definition, details, beacon);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConceptWithDetails {\n");
    
    sb.append("    clique: ").append(toIndentedString(clique)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    aliases: ").append(toIndentedString(aliases)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    semanticGroup: ").append(toIndentedString(semanticGroup)).append("\n");
    sb.append("    synonyms: ").append(toIndentedString(synonyms)).append("\n");
    sb.append("    definition: ").append(toIndentedString(definition)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

