/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API). 
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
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * LogEntry
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-10-10T12:14:03.940-07:00")
public class LogEntry {
  @SerializedName("timestamp")
  private String timestamp = null;

  @SerializedName("beacon")
  private String beacon = null;

  @SerializedName("query")
  private String query = null;

  @SerializedName("message")
  private String message = null;

  public LogEntry timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

   /**
   * timestamp 
   * @return timestamp
  **/
  @ApiModelProperty(example = "null", value = "timestamp ")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public LogEntry beacon(String beacon) {
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

  public LogEntry query(String query) {
    this.query = query;
    return this;
  }

   /**
   * URL of the API call executed by the aggregator 
   * @return query
  **/
  @ApiModelProperty(example = "null", value = "URL of the API call executed by the aggregator ")
  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public LogEntry message(String message) {
    this.message = message;
    return this;
  }

   /**
   * error message 
   * @return message
  **/
  @ApiModelProperty(example = "null", value = "error message ")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogEntry logEntry = (LogEntry) o;
    return Objects.equals(this.timestamp, logEntry.timestamp) &&
        Objects.equals(this.beacon, logEntry.beacon) &&
        Objects.equals(this.query, logEntry.query) &&
        Objects.equals(this.message, logEntry.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, beacon, query, message);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogEntry {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    beacon: ").append(toIndentedString(beacon)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

