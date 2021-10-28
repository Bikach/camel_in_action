package com.example.camel_in_action.chapter3.transformingXMLwithXSLT;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import static com.example.camel_in_action.chapter3.data.Path.DATA;

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
                from("file:" + DATA.path + "xslt/from?noop=true")
                        .to("xslt://file:" + DATA.path + "xslt/xsltransformer/transform.xsl")
                        .to("jms:queue:transformed");

                from("jms:queue:transformed")
                        .to("file:" + DATA.path + "xslt/to?fileName=cds.html");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}