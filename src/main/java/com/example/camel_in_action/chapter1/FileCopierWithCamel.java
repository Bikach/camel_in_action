package com.example.camel_in_action.chapter1;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FileCopierWithCamel {

    private static final String DATA_PATH = "src/main/java/com/example/camel_in_action/chapter1/data/";

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + DATA_PATH + "inbox?noop=true")
                        .to("file:" + DATA_PATH + "outbox");
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }

}
