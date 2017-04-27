package bio.knowledge.server.model;

import java.util.Objects;
import bio.knowledge.server.model.InlineResponse2004DataPage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
/**
 * InlineResponse2004
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

public class InlineResponse2004   {
  @JsonProperty("keywords")
  private List<String> keywords = new ArrayList<String>();

  @JsonProperty("pageNumber")
  private Integer pageNumber = null;

  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("totalEntries")
  private Integer totalEntries = null;

  @JsonProperty("dataPage")
  private List<InlineResponse2004DataPage> dataPage = new ArrayList<InlineResponse2004DataPage>();

  public InlineResponse2004 keywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }

  public InlineResponse2004 addKeywordsItem(String keywordsItem) {
    this.keywords.add(keywordsItem);
    return this;
  }

   /**
   * Get keywords
   * @return keywords
  **/
  @ApiModelProperty(value = "")
  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public InlineResponse2004 pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

   /**
   * Get pageNumber
   * @return pageNumber
  **/
  @ApiModelProperty(value = "")
  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public InlineResponse2004 pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

   /**
   * Get pageSize
   * @return pageSize
  **/
  @ApiModelProperty(value = "")
  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public InlineResponse2004 totalEntries(Integer totalEntries) {
    this.totalEntries = totalEntries;
    return this;
  }

   /**
   * Get totalEntries
   * @return totalEntries
  **/
  @ApiModelProperty(value = "")
  public Integer getTotalEntries() {
    return totalEntries;
  }

  public void setTotalEntries(Integer totalEntries) {
    this.totalEntries = totalEntries;
  }

  public InlineResponse2004 dataPage(List<InlineResponse2004DataPage> dataPage) {
    this.dataPage = dataPage;
    return this;
  }

  public InlineResponse2004 addDataPageItem(InlineResponse2004DataPage dataPageItem) {
    this.dataPage.add(dataPageItem);
    return this;
  }

   /**
   * Get dataPage
   * @return dataPage
  **/
  @ApiModelProperty(value = "")
  public List<InlineResponse2004DataPage> getDataPage() {
    return dataPage;
  }

  public void setDataPage(List<InlineResponse2004DataPage> dataPage) {
    this.dataPage = dataPage;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2004 inlineResponse2004 = (InlineResponse2004) o;
    return Objects.equals(this.keywords, inlineResponse2004.keywords) &&
        Objects.equals(this.pageNumber, inlineResponse2004.pageNumber) &&
        Objects.equals(this.pageSize, inlineResponse2004.pageSize) &&
        Objects.equals(this.totalEntries, inlineResponse2004.totalEntries) &&
        Objects.equals(this.dataPage, inlineResponse2004.dataPage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keywords, pageNumber, pageSize, totalEntries, dataPage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2004 {\n");
    
    sb.append("    keywords: ").append(toIndentedString(keywords)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    totalEntries: ").append(toIndentedString(totalEntries)).append("\n");
    sb.append("    dataPage: ").append(toIndentedString(dataPage)).append("\n");
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

