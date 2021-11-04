package com.example.camel_in_action.chapter1;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import static com.example.camel_in_action.Path.CHAPTER_1;

public class FileCopierWithCamel {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + CHAPTER_1.path + "data/from?noop=true")
                        .to("file:" + CHAPTER_1.path + "data/to?noop=true");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }

}
