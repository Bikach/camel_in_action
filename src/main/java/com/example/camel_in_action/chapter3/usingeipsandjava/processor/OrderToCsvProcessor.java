package com.example.camel_in_action.chapter3.usingeipsandjava.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import static java.util.Arrays.stream;

public class OrderToCsvProcessor implements Processor {

    private static final String SEPARATOR = "@";

    @Override
    public void process(Exchange exchange) {
        String custom = exchange.getIn().getBody(String.class);
        System.out.println("=========".concat(custom));
        StringBuilder csv = buildFirstPartOfCSV(custom);
        String[] itemIds = custom.substring(30).split(SEPARATOR);
        stream(itemIds).forEach(itemId -> csv.append(",").append(itemId.trim()));
        exchange.getIn().setBody(csv.toString());
    }

    private StringBuilder buildFirstPartOfCSV(String custom) {
        return new StringBuilder()
                .append(custom.substring(0, 10).trim())
                .append(",").append(custom, 20, 30)
                .append(",").append(custom, 10, 20);
    }
}
