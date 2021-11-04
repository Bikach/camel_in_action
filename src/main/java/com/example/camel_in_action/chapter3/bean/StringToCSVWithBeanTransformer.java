package com.example.camel_in_action.chapter3.bean;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import static com.example.camel_in_action.Path.CHAPTER_3;

public class StringToCSVWithBeanTransformer {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:" + CHAPTER_3.path + "bean/data/from?noop=true")
                        .log("file name -> ${header.CamelFileName}")
                        .bean(new OrderMapperBean())
                        .log("====== date -> ${header.Date}")
                        .toD("file:" + CHAPTER_3.path + "bean/data/to?fileName=report-${header.Date}.csv");
            }
        });

        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
