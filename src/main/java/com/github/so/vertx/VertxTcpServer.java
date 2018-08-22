package com.github.so.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VertxTcpServer {

  private static final Logger log = LoggerFactory.getLogger(VertxTcpServer.class);

  private static final String HOSTNAME = "127.0.0.1"; //localhost
  private static final int PORT = 7000;

  public static final String INCOME_MESSAGES = "INCOME_MESSAGES";
  public static final String OUTCOME_MESSAGES = "OUTCOME_MESSAGES";

  private Vertx vertx;
  private NetServer netServer;
  private EventBus eventBus;

  public VertxTcpServer() {
    this.createVertx();
    this.createEventBus();
    this.createNetServer();
    this.createConnectHandler();
  }

  @PostConstruct
  public void init() {
    this.start(); // Automatically
  }

  private void createVertx() {
    final VertxOptions options = new VertxOptions();
    options.setWorkerPoolSize(40); // another options of vertx can be added here
    this.vertx = Vertx.vertx(options);
  }

  private void createEventBus() {
    final EventBusOptions options = new EventBusOptions();
    options.setUsePooledBuffers(true); // another options of eventBus
    this.eventBus = this.vertx.eventBus();
  }

  private void createNetServer() {
    final NetServerOptions options = new NetServerOptions();
    options.setHost(HOSTNAME);
    options.setPort(PORT);
    this.netServer = this.vertx.createNetServer(options);
  }

  /**
   * When someone connect to our server, what we have to do?
   */
  private void createConnectHandler() {
    this.netServer.connectHandler(socket -> {
      log.info("[TCP Server] - Incoming connection...");
      this.handleCloseConnection(socket);
      this.handleException(socket);
      this.incomingMessageHandler(socket);
      this.outcomingMessageHandler(socket);
    });
  }

  /*
   * If the connection is closed....
   */
  private void handleCloseConnection(final NetSocket socket) {
    socket.closeHandler(closeHandler -> {
      log.info("[TCP Server] - Connection Closed with Host -> {}:{}", socket.remoteAddress().host(), socket.remoteAddress().port());
    });
  }

  /*
   * If an error occurs
   */
  private void handleException(final NetSocket socket) {
    socket.exceptionHandler(ex -> log.error("[TCP Server] - Exception on TCP Communication", ex));
  }

  private void incomingMessageHandler(final NetSocket socket) {
    socket.handler(buffer -> this.eventBus.send(INCOME_MESSAGES, buffer));
  }

  private void outcomingMessageHandler(final NetSocket socket) {
    this.eventBus.consumer(OUTCOME_MESSAGES, message -> {
      socket.write(message.body().toString());
    });
  }

  public void start() {
    this.netServer.listen(res -> {
      if (res.succeeded()) {
        log.info("[TCP Server] - Started a TCP server. Listening at port: {}", PORT);
      } else {
        log.error("[TCP Server] - Failed starting a TCP Server. Error -> {}", res.cause().getMessage(), res.cause());
      }
    });
  }

  public void stop() {
    if (this.netServer != null) {
      this.netServer.close(shutdown -> {
        if (shutdown.succeeded()) {
          log.info("[TCP Server] - Shutdown Gracefully Complete for server");
        } else {
          log.warn("[TCP Server] - Shutdown FAILED for Server");
        }
      });
    } else {
      log.warn("[TCP Server] - This server is not Started.");
    }
  }

  public EventBus getEventBus() {
    return this.eventBus;
  }
}
