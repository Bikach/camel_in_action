package com.example.camel_in_action.chapter2.multicast;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class SendOrderToJmsMulticast {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:src/main/java/com/example/camel_in_action/chapter2/multicast/data?noop=true")
                        .to("jms:xmlOrders");

                from("jms:xmlOrders")
                        .multicast()
                        .stopOnException() // optional
                        .parallelProcessing() // optional
                        .to("jms:production")
                        .to("file:src/main/java/com/example/camel_in_action/chapter2/multicast/accounting?noop=true");

                /*
                    from("direct:production")
                        .throwException(Exception.class, "I failed!")
                 */
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
