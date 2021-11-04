package com.example.camel_in_action.chapter2.filter;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import static com.example.camel_in_action.Path.CHAPTER_2;

public class SendOrderToJmsWithFilter {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:"+ CHAPTER_2.path +"filter/data?noop=true")
                        .filter(xpath("/order[@test='true']"))
                        .log("Received XML order: ${header.CamelFileName}")
                        .to("jms:oderTest");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
