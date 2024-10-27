package com.mini.customerapi.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Configuration;


import java.util.Map;

@Configuration
public class AppInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetails(Map.of(
                "app.name", "Customer Management Service",
                "app.description", "This is a Spring Boot service for managing customer data.",
                "app.version", "1.0.0",
                "app.author", "Paul Ngouabeu"
        ));
    }
}
