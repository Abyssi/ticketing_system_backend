package com.isssr.ticketing_system.model.auto_generated.scheduler;

import com.isssr.ticketing_system.model.auto_generated.temporary.DataBaseTimeQuery;
import lombok.RequiredArgsConstructor;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class TaskScheduler {



    /*@Autowired
    private ThreadPoolTaskScheduler scheduler;


    private List<ScheduledFuture> scheduledFutures = new ArrayList<>();

    public boolean addJob(Query query) {

        if (query.getCron() != null) {

            scheduler.schedule(query, new CronTrigger(query.getCron()));

            return true;

        }

        return false;

    }

    public ScheduledFuture addJob(DataBaseTimeQuery query) {

        if (query.getCron() != null) {

            ScheduledFuture scheduledFuture = scheduler.schedule(query, new CronTrigger(query.getCron()));

            this.scheduledFutures.add(scheduledFuture);

            return scheduledFuture;

        }

        return null;

    }


    public void removeJob(ScheduledFuture scheduledFuture) throws ExecutionException, InterruptedException {

        int index = this.scheduledFutures.indexOf(scheduledFuture);

        DataBaseTimeQuery q = (DataBaseTimeQuery) this.scheduledFutures.get(index).get();

    }*/


    private final Scheduler scheduler;

    private Long jobCounter = 1L; //use this counter to keep track of jobs

    private String DATA_BASE_QUERY_GROUP_NAME = "Data base query group";

    private String CRON_GROUP_NAME;

    public void addJob(DataBaseTimeQuery query) throws ParseException, SchedulerException {

        //set cron trigger
        CronTrigger cronTrigger = createCronTrigger(query.getCron());

        //set job details
        JobDetailImpl jobDetail = new JobDetailImpl();

        jobDetail.setJobClass(DataBaseTimeQuery.class);

        //set name and group to generate a key
        jobDetail.setName(jobCounter.toString());

        jobDetail.setGroup(DATA_BASE_QUERY_GROUP_NAME);

        query.setJobKey(jobDetail.getKey());

        //map job
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(DataBaseTimeQuery.MAP_ME, query);

        jobDetail.setJobDataMap(jobDataMap);

        //schedule job
        scheduler.scheduleJob(jobDetail, cronTrigger);

        //increment job counter
        jobCounter++;
    }

    public void removeJob(DataBaseTimeQuery query) throws SchedulerException {

        scheduler.deleteJob(query.getJobKey());

    }

    public CronTrigger createCronTrigger(String cron) throws ParseException {

        CronTriggerImpl cronTrigger = new CronTriggerImpl();

        cronTrigger.setName(jobCounter.toString());

        cronTrigger.setGroup(CRON_GROUP_NAME);

        cronTrigger.setCronExpression(cron);

        return cronTrigger;

    }

}