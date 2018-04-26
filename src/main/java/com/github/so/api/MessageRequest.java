package com.github.so.api;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequest implements Serializable {

  private static final long serialVersionUID = 3453766275744185535L;

  @JsonProperty(value = "message", required = true)
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


}
