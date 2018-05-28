package com.isssr.ticketing_system.config;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
public class SchedulingConfig {

    private List<Trigger> triggerList = new ArrayList<>();

    /*@Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutoWiringSpringBeanJobFactory jobFactory =
                new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }*/

    /*@Bean
    public StdSchedulerFactory schedulerFactory() {

        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();

        return schedulerFactory;
    }*/

    @Bean
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        factoryBean.setJobFactory(jobFactory);    // Set jobFactory to AutowiringSpringBeanJobFactory
        return factoryBean;
    }

    /*@Bean
    public Scheduler scheduler() throws SchedulerException {

        SchedulerFactoryBean schedulerFactory = schedulerFactory();

        Scheduler scheduler = schedulerFactory.getScheduler();

        scheduler.start();

        return scheduler;
    }*/
}
