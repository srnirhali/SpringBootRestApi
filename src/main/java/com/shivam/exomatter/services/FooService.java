package com.shivam.exomatter.services;

import org.springframework.stereotype.Service;

@Service
public class FooService {
    public Float fooness(String formula) throws InterruptedException {
        long sleepTime = (long)(Math.random() * 10000);
        Thread.sleep(sleepTime);
        return (float) sleepTime;
    }
}
