package com.example.camel_in_action.chapter3.transforming;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import static com.example.camel_in_action.Path.CHAPTER_3;

public class StringToCSVWithMethodTransform {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // first method
                from("file:" + CHAPTER_3.path + "transforming/data/method/from?noop=true")
                        .transform(body().regexReplaceAll("@", "-"))
                        .to("file:" + CHAPTER_3.path + "transforming/data/method/to/first?fileName=report-first-edition");

                //second method
                from("file:" + CHAPTER_3.path + "transforming/data/method/to/first?noop=true")
                        .transform(new Expression() {
                            public <T> T evaluate(Exchange exchange, Class<T> type) {
                                String body = exchange.getIn().getBody(String.class);
                                body = body.replaceAll("-", "@");
                                body += "\n1284736473JDEIK3453912/04/2020DIKF@EOLK@ZDJK";
                                return (T) body;
                            }
                        })
                        .to("file:" + CHAPTER_3.path + "transforming/data/method/to/second?fileName=report-second-edition");

            }
        });

        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
