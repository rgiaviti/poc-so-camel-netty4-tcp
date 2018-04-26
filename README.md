# QUESTION / PROBLEM
=====================================

## How to send a response back over a established TCP connection in async mode using Apache Camel Netty4?

I'm building a micro-service that has an Apache Camel route using Netty4 Component (http://camel.apache.org/netty4.html) in consumer mode. So, in my micro-service, this route that I'm building, will receive messages over a a TCP connection. To do that, I did this:

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

Well, **I'm receiving the messages normally**. To test, I use telnet:

    $ telnet localhost 7000
    Trying 127.0.0.1...
    Connected to localhost.
    Escape character is '^]'.
    TheMessage

The problem is when I want to send back a message to the same TCP channel established in that route. In synchronous mode, I can do that easily using the Exchange object. **However, in asynchronous mode, I don't know how to send back a message to the producer.**

The Spring Service that receive and should send the messages, is this:

    @Service
    public class MessageService {
      
      private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    
      private List<String> messageStore = new LinkedList<>();
      
      public void sendToTCP(final String message) {
        log.info("[Service] - Sending Message over TCP Channel --> {}", message);
      }
    
      @Handler
      public void receiveFromTCP(final Exchange exchange) {
        final String messageFromTcp = exchange.getIn().getBody(String.class);
        log.info("[Service] - Message Received from TCP Channel --> {}", messageFromTcp);
        this.messageStore.add(messageFromTcp);
      }
    
      public List<String> getReceivedMessages() {
        return messageStore;
      }
    }

In resume, what I need is to put some code in this method, something like that:

    public void sendToTCP(final String message) {
      log.info("[Service] - Sending Message over TCP Channel --> {}", message);
      // Send message to producer here
      camelContext.createProducerTemplate.send....
    }

I cannot create another route to producer because I don't know the producer IP. I really need to use the already established TCP channel between the producer and my application. The communication needs to be over TCP, other tools, like Queues, are not an option.

----------

## GitHub Sample Project

I uploaded a sample project on GitHub: https://github.com/rgiaviti/so-camel-netty4-tcp

## Draw
[![This is the draw of the communication TCP communication][1]][1]


  [1]: https://i.stack.imgur.com/yoCsa.png

I'm using:

 - Spring Boot 1.5.12;
 - Apache Camel 2.21.0;
