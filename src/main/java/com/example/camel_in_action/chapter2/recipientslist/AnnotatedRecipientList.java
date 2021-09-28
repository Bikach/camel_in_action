package com.example.camel_in_action.chapter2.recipientslist;

import org.apache.camel.RecipientList;
import org.apache.camel.language.xpath.XPath;
import org.springframework.stereotype.Component;

@Component
public class AnnotatedRecipientList {
    @RecipientList
    public String[] recipients(@XPath("/order/@customer") String customer) {
        return (isGoldCustomer(customer))
                ? new String[]{"jms:accounting", "jms:production"}
                : new String[]{"jms:accounting"};
    }

    private boolean isGoldCustomer(String customer) {
        return customer.equals("honda");
    }
}
