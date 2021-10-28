package com.example.camel_in_action.chapter3.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import static com.example.camel_in_action.chapter3.data.Path.DATA;

public class StringToCSVWithProcessorTransformer {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:" + DATA.path + "from?noop=true")
                        .log("file name -> ${header.CamelFileName}")
                        .process(new OrderMapperProcessor())
                        .log("====== date -> ${header.Date}")
                        .toD("file:" + DATA.path + "to?fileName=report-${header.Date}.csv");
            }
        });

        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
