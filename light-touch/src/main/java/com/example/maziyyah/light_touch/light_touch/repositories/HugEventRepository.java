package com.example.maziyyah.light_touch.light_touch.repositories;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.utils.Utils;

@Repository
public class HugEventRepository {

    private static final long SIMULTANEOUS_HUG_WINDOW_SEC = 10; // 2 seconds TTL

    @Qualifier(Utils.template01)
    private final RedisTemplate<String, Object> template;

    public HugEventRepository(@Qualifier(Utils.template01) RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public void logSpontaneousHug(String deviceId) {
        String hugKey = "hug:" + deviceId;
        template.opsForValue().set(hugKey, "hug_detected",  SIMULTANEOUS_HUG_WINDOW_SEC, TimeUnit.SECONDS);
    }

    public boolean checkIfPairedDeviceSentHug(String pairedDeviceId) {
        String hugKey = "hug:" + pairedDeviceId;
        return template.hasKey(hugKey);
    }



    

    
}
