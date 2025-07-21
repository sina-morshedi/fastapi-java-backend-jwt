package com.example.fastapi.config;

public class ContextHolder {
    private static final ThreadLocal<String> storeNameHolder = new ThreadLocal<>();

    public static void setStoreName(String storeName) {
        storeNameHolder.set(storeName);
    }

    public static String getStoreName() {
        return storeNameHolder.get();
    }

    public static void clear() {
        storeNameHolder.remove();
    }
}