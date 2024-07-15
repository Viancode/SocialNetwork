package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.TokenType;
import com.example.socialnetwork.config.TokenProperties;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TokenServiceImpl implements TokenServicePort {
    private static final String KEY_PATTERN = "%s::%s::%s";
    private final long refreshExpiration;
    private final RedisTemplate<String, String> redisTemplate;
    public TokenServiceImpl(TokenProperties tokenProperties, RedisTemplate<String, String> redisTemplate) {
        this.refreshExpiration = tokenProperties.getRefreshExpiration();
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void revokeAllUserTokens(User user, TokenType tokenType) {
        String keyPattern = String.format(KEY_PATTERN, tokenType.name(), user.getUsername(), "*");
        Set<String> refreshTokens = redisTemplate.keys(keyPattern);
        if (refreshTokens != null && !refreshTokens.isEmpty()) {
           refreshTokens.forEach(redisTemplate::delete);
        }
    }

    @Override
    public void revokeRefreshToken(String refreshToken, User user) {
        String userId = getTokenInfo(refreshToken, TokenType.REFRESH);
        if (!userId.equals(user.getUsername())) {
            throw new IllegalArgumentException("Invalid token");
        }
        redisTemplate.delete(String.format(KEY_PATTERN, "REFRESH", userId, refreshToken));
    }

    @Override
    public String getTokenInfo(String token, TokenType tokenType) {
        String keyPattern = String.format(KEY_PATTERN, tokenType, "*", token);
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (keys == null || keys.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
        String key = keys.iterator().next();
        return key.split("::")[1];
    }

    @Override
    public void saveToken(String token, String userId, TokenType tokenType, long expiration) {
        redisTemplate.opsForValue().set(
                String.format(KEY_PATTERN, tokenType.name(), userId, token),
                "",// them thong tin remember me
                expiration, TimeUnit.MILLISECONDS
        );
    }
}
