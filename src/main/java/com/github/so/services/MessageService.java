package com.github.so.services;

import java.util.LinkedList;
import java.util.List;
import io.netty.channel.Channel;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.component.netty4.NettyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  
  private static final Logger log = LoggerFactory.getLogger(MessageService.class);

  private List<String> messageStore = new LinkedList<>();
  private Channel openedChannel;
  
  public void sendToTCP(final String message) {
    log.info("[Service] - Sending Message over TCP Channel --> {}", message);
    log.info("[Service] - Channel is Active? {}", this.openedChannel.isActive());
    log.info("[Service] - Channel is Open? {}", this.openedChannel.isOpen());
    log.info("[Service] - Channel is Writeble? {}", this.openedChannel.isWritable());

    this.openedChannel.write(message);
    this.openedChannel.flush();
  }

  @Handler
  public void receiveFromTCP(final Exchange exchange) {
    this.openedChannel = exchange.getProperty(NettyConstants.NETTY_CHANNEL, Channel.class);
    final String messageFromTcp = exchange.getIn().getBody(String.class);
    log.info("[Service] - Message Received from TCP Channel --> {}", messageFromTcp);
    this.messageStore.add(messageFromTcp);
  }

  public List<String> getReceivedMessages() {
    return messageStore;
  }
}
