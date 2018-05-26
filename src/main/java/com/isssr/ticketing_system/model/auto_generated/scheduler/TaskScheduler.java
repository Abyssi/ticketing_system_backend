package com.isssr.ticketing_system.model.auto_generated.scheduler;

import com.isssr.ticketing_system.model.auto_generated.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class TaskScheduler {

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    public boolean addJob(Query query) {

        if (query.getCron() != null) {

            scheduler.schedule(query, new CronTrigger(query.getCron()));

            return true;

        }

        return false;

    }
}