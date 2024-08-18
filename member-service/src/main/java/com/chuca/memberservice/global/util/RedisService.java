package com.chuca.memberservice.global.util;

import java.time.Duration;

public interface RedisService {
    void setValues(String key, String data) ;

    void setValues(String key, String data, Duration duration) ;

    String getValues(String key) ;

    void deleteValues(String key) ;

    void saveValues(String key, String value, long time);
}
