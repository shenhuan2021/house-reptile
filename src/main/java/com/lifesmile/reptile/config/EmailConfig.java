package com.lifesmile.reptile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email")
@Data
public class EmailConfig {
    private String fromPassword;
    private String fromPort;
    private String toAddr;
    private String fromHost;
    private String fromEmail;
}
