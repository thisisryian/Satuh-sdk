package com.satuh;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 9/11/2017.
 */


public class AccessTokenCache {
    static final String CACHED_ACCESS_TOKEN_KEY
            = "com.satuh.AccessTokenManager.CachedAccessToken";
    private final SharedPreferences sharedPreferences;

    AccessTokenCache(Context context) {
        sharedPreferences = context.getSharedPreferences(CACHED_ACCESS_TOKEN_KEY, Context.MODE_PRIVATE);

    }

    public void save(String accessToken) {
        notNull(accessToken, "accessToken");
        sharedPreferences.edit().putString(CACHED_ACCESS_TOKEN_KEY, accessToken)
                .apply();


    }
    public void clear() {
        sharedPreferences.edit().remove(CACHED_ACCESS_TOKEN_KEY).apply();

    }
    private boolean hasCachedAccessToken() {
        return sharedPreferences.contains(CACHED_ACCESS_TOKEN_KEY);
    }

    public static void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument '" + name + "' cannot be null");
        }
    }


    public String load() {
        String accessToken = null;
        if (hasCachedAccessToken()) {
            // If we have something cached, we try to use it; even if it is invalid, do not fall
            // back to a legacy caching strategy.
            accessToken = getCachedAccessToken();
        }

        return accessToken;
    }


    private String getCachedAccessToken() {
        String jsonString = sharedPreferences.getString(CACHED_ACCESS_TOKEN_KEY, null);

        return jsonString;
    }

}

