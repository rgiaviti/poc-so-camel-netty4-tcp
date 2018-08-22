package com.github.so.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("messageProcessor")
public class MessageProcessor implements Processor {

  private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

  @Override
  public void process(Exchange exchange) throws Exception {
    log.info("[MessageProcessor] - Handling message in processor");
  }
}
