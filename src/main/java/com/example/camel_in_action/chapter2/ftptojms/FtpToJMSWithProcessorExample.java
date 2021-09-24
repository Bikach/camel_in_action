package com.example.camel_in_action.chapter2.ftptojms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class FtpToJMSWithProcessorExample {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("vm://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("ftp://camel@172.27.111.59/orders/test.txt?password=pass&passiveMode=true")
                        .process(exchange -> System.out.println("We just downloaded: " + exchange.getIn().getHeader("CamelFileName")))
                        /* full syntax
                            .process(new Processor() {
                                @Override
                                public void process(Exchange exchange) throws Exception {

                                }
                            })
                        */
                        .to("jms:queue:incomingOrders");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
