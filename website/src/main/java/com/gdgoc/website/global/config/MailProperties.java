package com.gdgoc.website.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String apiKey;
    private String senderName;
    private String senderEmail;
    private long connectTimeoutMs = 5000;
    private long readTimeoutMs = 10000;

    public String getFormattedFrom() {
        return String.format("%s <%s>", senderName, senderEmail);
    }
}