package com.github.so.camel.routes;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Netty4TcpRoute extends RouteBuilder {

  private static final String CAMEL_FROM_URL = "netty4:tcp://localhost:7000?textline=true&reuseChannel=true&encoding=utf8";
  private static final String CAMEL_TO_URL = "bean:messageService";

  private Processor messageProcessor;

  @Autowired
  public Netty4TcpRoute(@Qualifier("messageProcessor") Processor messageProcessor) {
    this.messageProcessor = messageProcessor;
  }

  @Override
  public void configure() throws Exception {
    this.from(CAMEL_FROM_URL).process(this.messageProcessor).to(CAMEL_TO_URL);
  }

}
