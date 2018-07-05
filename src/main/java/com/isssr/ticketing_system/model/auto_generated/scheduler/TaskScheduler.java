package com.isssr.ticketing_system.model.auto_generated.scheduler;

import com.isssr.ticketing_system.model.auto_generated.query.*;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class TaskScheduler {


    private final Scheduler scheduler;

    private String DATA_BASE_QUERY_GROUP_NAME = "Data_base_query_group";

    private String CRON_GROUP_NAME;

    public void addJob(DataBaseTimeQuery query) throws ParseException, SchedulerException {

        //set cron trigger
        CronTrigger cronTrigger = createCronTrigger(query.getCron(), query.getId().toString());

        //set job details
        JobDetailImpl jobDetail = new JobDetailImpl();

        jobDetail.setJobClass(DataBaseTimeQuery.class);

        //set name and group to generate a key
        jobDetail.setName(query.getId().toString());

        jobDetail.setGroup(DATA_BASE_QUERY_GROUP_NAME);

        query.setJobKey(jobDetail.getKey());

        //map job
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(DataBaseTimeQuery.MAP_ME, query);

        jobDetail.setJobDataMap(jobDataMap);

        //schedule job
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public void addJob(ScheduledQuery query) throws ParseException, SchedulerException {

        //set cron trigger
        CronTrigger cronTrigger = createCronTrigger(query.getCron(), query.getId().toString());

        //set job details
        JobDetailImpl jobDetail = new JobDetailImpl();

        jobDetail.setJobClass(query.getClass());

        //set name and group to generate a key
        jobDetail.setName(query.getId().toString());

        jobDetail.setGroup(DATA_BASE_QUERY_GROUP_NAME);

        query.setJobKey(jobDetail.getKey());

        //map job
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(ScheduledQuery.MAP_ME, query);

        jobDetail.setJobDataMap(jobDataMap);

        //schedule job
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public void removeJob(DataBaseTimeQuery query) throws SchedulerException {

        scheduler.deleteJob(JobKey.jobKey(query.getJobKey().getName(), query.getJobKey().getGroup()));

    }

    public void removeJob(ScheduledQuery query) throws SchedulerException {

        scheduler.deleteJob(JobKey.jobKey(query.getJobKey().getName(), query.getJobKey().getGroup()));

    }

    public CronTrigger createCronTrigger(String cron, String queryId) throws ParseException {

        CronTriggerImpl cronTrigger = new CronTriggerImpl();

        cronTrigger.setName(queryId);

        cronTrigger.setGroup(CRON_GROUP_NAME);

        cronTrigger.setCronExpression(cron);

        return cronTrigger;

    }

}