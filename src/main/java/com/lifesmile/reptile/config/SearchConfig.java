package com.lifesmile.reptile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "search")
@Data
public class SearchConfig {
    private String city;
    private String regions;
    private String keyword;
}
