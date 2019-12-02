package com.lifesmile.reptile.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Setter
@Getter
public class Cache {
    public static ConcurrentMap<String, String> cityCache = new ConcurrentHashMap();
    public static ConcurrentMap<String, String> regionCache = new ConcurrentHashMap();
    public static ConcurrentMap<String, String> streetCache = new ConcurrentHashMap();
    public static ConcurrentMap<String, String> recordCache = new ConcurrentHashMap();
}
