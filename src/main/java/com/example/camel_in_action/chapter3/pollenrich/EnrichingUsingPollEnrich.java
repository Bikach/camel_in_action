package com.example.camel_in_action.chapter3.pollenrich;

import com.example.camel_in_action.chapter3.processor.OrderMapperProcessor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import static com.example.camel_in_action.Path.CHAPTER_3;

public class EnrichingUsingPollEnrich {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + CHAPTER_3.path + "pollenrich/data/from?noop=true")
                        .to("jms:clientQueue")
                        .process(new OrderMapperProcessor())
                        .pollEnrich("file:" + CHAPTER_3.path + "pollenrich/data/from?noop=true",
                                (oldExchange, newExchange) -> {
                                    if (newExchange == null) {
                                        return oldExchange;
                                    }
                                    String csv = oldExchange.getIn().getBody(String.class);
                                    String txt = newExchange.getIn().getBody(String.class);
                                    String body = csv + "\n" + txt;
                                    oldExchange.getIn().setBody(body);
                                    return oldExchange;
                                })
                        .to("file:" + CHAPTER_3.path + "pollenrich/data/to?fileName=report-completed.txt");
            }
        });

        context.start();
        Thread.sleep(10000);
        context.stop();
    }

}
