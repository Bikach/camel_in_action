package com.example.camel_in_action.chapter2.ftptojms;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FtpToJMSRoute  extends RouteBuilder {
    @Override
    public void configure() {
        from("ftp://camel@172.27.111.59/orders?password=pass&passiveMode=true")
                .to("jms:queue:incomingOrders");
    }
}

/* Spring XML file
    <context:component-scan base-package="camelinaction.routes"/>
    <camelContext xmlns="http://camel.apache.org/schema/spring">
        <contextScan/>
    </camelContext>
*/
