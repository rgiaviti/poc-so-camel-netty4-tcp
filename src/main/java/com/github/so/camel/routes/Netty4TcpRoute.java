package com.github.so.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Netty4TcpRoute extends RouteBuilder {

  private static final Logger log = LoggerFactory.getLogger(Netty4TcpRoute.class);

  @Override
  public void configure() throws Exception {
    this.from("netty4:tcp://localhost:7000?textline=true&encoding=utf8")
        .process(new Processor() {
          @Override
          public void process(final Exchange exchange) throws Exception {
            log.info("[Processor] - Incoming Message -> {}", exchange.getIn().getBody(String.class));
          }
        }).to("bean:messageService");
  }

}
