package com.example.camel_in_action.chapter2.ftptojms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class FtpToJMSExample {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        //  setup the properties component to use the test file
        //PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
        //prop.setLocation("classpath:rider.properties");


        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("ftp://camel@172.27.111.59/orders?password=pass&passiveMode=true")
                        //.to("jms:queue:incomingOrders");
                        //toD("jms:queue:${header.myDest}); //spring properties
                        .toD("jms:queue:{{myDest]}");// camel properties
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
