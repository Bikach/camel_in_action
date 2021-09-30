package com.example.camel_in_action.chapter3.usingeipsandjava.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class StringToCSVWithProcessorTransformer {
    private static final String DATA_PATH = "src/main/java/com/example/camel_in_action/chapter3/usingeipsandjava/processor/data/";

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:" + DATA_PATH + "from?noop=true")
                        .log("====== file name -> ${header.fileName}")
                        .process(new OrderToCsvProcessor())
                        .to("file:" + DATA_PATH + "to?fileName=report-${header.Date}.csv");
            }
        });

        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
