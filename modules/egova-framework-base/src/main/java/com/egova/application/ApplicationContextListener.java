package com.egova.application;

import com.flagwind.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author chendb
 * @description: 上下文监听
 * @date 2020-04-18 22:32:43
 */
@Slf4j
public class ApplicationContextListener implements SpringApplicationRunListener {

    public ApplicationContextListener(SpringApplication application, String[]  args){
        log.info("constructor");
    }

    @Override
    public void starting() {
        log.info("starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("environment Prepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        Application.start(new Context(context));
        log.info("context Loaded");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("started");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("running");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("failed");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
//
//        Application.start(new Context(context));
    }
}
