package com.example.camel_in_action.chapter3.bean;

import static java.util.Arrays.stream;

public class OrderMapperBean {
    private static final String SEPARATOR = "@";

    public String map(String message){
        StringBuilder csv = buildFirstPartOfCSV(message);
        String[] itemIds = message.substring(30).split(SEPARATOR);
        stream(itemIds).forEach(itemId -> csv.append(",").append(itemId.trim()));
        return csv.toString();
    }

    private StringBuilder buildFirstPartOfCSV(String custom) {
        return new StringBuilder()
                .append(custom.substring(0, 10).trim())
                .append(",").append(custom, 20, 30)
                .append(",").append(custom, 10, 20);
    }
}
