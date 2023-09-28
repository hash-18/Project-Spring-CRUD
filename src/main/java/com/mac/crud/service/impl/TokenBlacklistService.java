package com.mac.crud.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenBlacklistService {
	
	private final Set<String> blacklistedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // You can also implement a method to remove tokens from the blacklist if needed
    public void removeTokenFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }

}
