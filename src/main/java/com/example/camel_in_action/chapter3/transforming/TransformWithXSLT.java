package com.example.camel_in_action.chapter3.transforming;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import static com.example.camel_in_action.Path.CHAPTER_3;

public class TransformWithXSLT {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + CHAPTER_3.path + "transforming/data/xslt/from?noop=true")
                        .to("xslt://file:" + CHAPTER_3.path + "transforming/data/xslt/xsltransformer/transform.xsl")
                        .to("jms:queue:transformed");

                from("jms:queue:transformed")
                        .to("file:" + CHAPTER_3.path + "transforming/data/xslt/to?fileName=cds.html");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}