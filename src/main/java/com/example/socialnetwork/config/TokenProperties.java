package com.example.socialnetwork.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
@Value
public class TokenProperties {
    String secretKey;
    long accessExpiration;
    long refreshExpiration;
    long verifiedExpiration;
}
