package com.github.so.services;

import com.github.so.vertx.VertxTcpServer;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  
  private static final Logger log = LoggerFactory.getLogger(MessageService.class);

  private List<String> messageStore = new LinkedList<>();
  private EventBus eventBus;

  @Autowired
  public MessageService(final VertxTcpServer server) {
    log.info("[Message Service] - Setting up message service");
    this.eventBus = server.getEventBus();
    this.receiveFromTCP();
  }

  public void sendToTCP(final String message) {
    log.info("[Message Service] - Sending Message -> {}", message);
    this.eventBus.publish(VertxTcpServer.OUTCOME_MESSAGES, message);
  }

  public void receiveFromTCP() {
    this.eventBus.consumer(VertxTcpServer.INCOME_MESSAGES, message -> {
      log.info("[Message Service] - Incoming Message -> {}", message.body().toString());
      this.messageStore.add(message.body().toString());
    });
  }

  public List<String> getReceivedMessages() {
    return messageStore;
  }
}
