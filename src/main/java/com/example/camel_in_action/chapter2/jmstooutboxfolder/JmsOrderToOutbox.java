package com.example.camel_in_action.chapter2.jmstooutboxfolder;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class JmsOrderToOutbox {
    private static final String DATA_PATH = "src/main/java/com/example/camel_in_action/chapter2/jmstooutboxfolder/";

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + DATA_PATH + "data?noop=true")
                        .log(LoggingLevel.INFO, "=== XML filename : ${header.CamelFileName}")
                        .to("jms:xmlOrders");

                from("jms:xmlOrders")
                        .log(LoggingLevel.INFO, "=== XML filename : ${header.CamelFileName}")
                        .to("file:" + DATA_PATH + "outbox?noop=true");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
