package com.example.camel_in_action.chapter2.contentbasedrouter;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class SendOrderToJms {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:src/main/java/com/example/camel_in_action/chapter2/contentbasedrouter/data?noop=true")
                        .choice()
                            .when(header("CamelFileName").endsWith(".xml"))
                                .log(LoggingLevel.INFO, "=== First filename : ${header.CamelFileName}")
                                .to("jms:xmlOrders")
                            .when(simple("${header.CamelFileName} ends with 'csv'"))
                                .log(LoggingLevel.INFO, "=== Second filename : ${header.CamelFileName}")
                                .to("jms:csvOrders")
                            .otherwise()
                                .log(LoggingLevel.INFO, "=== last filename : ${header.CamelFileName}")
                                .to("jms:badOrders")
                                .stop()
                        .end()
                        .to("jms:continuedProcessing");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
