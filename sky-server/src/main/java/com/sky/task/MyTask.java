package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Test Custom Scheduled Task Class
 */
@Component
@Slf4j
public class MyTask {

    //@Scheduled(cron ="0/5 * * * * ?") // cron expression can be generated online
    public void executeTask() {
        log.info("schedule task start execute: {}", new Date());
    }
}
