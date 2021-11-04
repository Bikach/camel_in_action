package com.example.camel_in_action.chapter2.jmstooutboxfolder;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import static com.example.camel_in_action.Path.CHAPTER_2;

public class JmsOrderToOutbox {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + CHAPTER_2.path + "jmstooutboxfolder/data/from?noop=true")
                        .log(LoggingLevel.INFO, "=== XML filename : ${header.CamelFileName}")
                        .to("jms:xmlOrders");

                from("jms:xmlOrders")
                        .log(LoggingLevel.INFO, "=== XML filename : ${header.CamelFileName}")
                        .to("file:" + CHAPTER_2.path + "jmstooutboxfolder/data/to?noop=true");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
