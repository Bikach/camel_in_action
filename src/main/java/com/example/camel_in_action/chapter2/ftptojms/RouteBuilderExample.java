package com.example.camel_in_action.chapter2.ftptojms;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RouteBuilderExample {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                //from -> to
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
