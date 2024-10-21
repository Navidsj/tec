package com.example.tec.Service;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.script.ReactiveScriptExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.lang.Math.abs;

@Service
public class CaptchaService {



    public ResponseEntity<Integer> getCaptcha(){
        RedissonClient redissonClient = Redisson.create();
        Random random = new Random();

        RList<Integer> captchaList = redissonClient.getList("captchaList");

        Instant now = Instant.now();
        long currentTimeSeconds = now.getEpochSecond();

        String currentTimeString = Long.toString(currentTimeSeconds+ random.nextInt());

        int hashCode = currentTimeString.hashCode();

        captchaList.add(abs(hashCode));

        return ResponseEntity.ok(abs(hashCode));
    }



}
