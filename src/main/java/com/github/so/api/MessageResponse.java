package com.github.so.api;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse implements Serializable {

  private static final long serialVersionUID = 3885544236295077467L;

  @JsonProperty("messagesFromTCP")
  private List<String> messages;

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }
  
  
}
