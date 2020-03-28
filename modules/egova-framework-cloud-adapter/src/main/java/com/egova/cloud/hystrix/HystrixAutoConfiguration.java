package com.egova.cloud.hystrix;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.netflix.hystrix.security.HystrixSecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Hystrix配置，提供请求上下文传递
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Configuration
@AutoConfigureAfter(HystrixSecurityAutoConfiguration.class)
public class HystrixAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(HystrixAutoConfiguration.class);

    @Autowired(required = false)
    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    @PostConstruct
    public void init() {
        // Keeps references of existing Hystrix plugins.
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
                .getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
                .getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
                .getPropertiesStrategy();
        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance()
                .getCommandExecutionHook();
        HystrixConcurrencyStrategy concurrencyStrategy = detectRegisteredConcurrencyStrategy();

        HystrixPlugins.reset();

        // Registers existing plugins excepts the Concurrent Strategy plugin.
        HystrixPlugins.getInstance().registerConcurrencyStrategy(
                new RequestContextHystrixConcurrencyStrategy(concurrencyStrategy));
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
    }

    private HystrixConcurrencyStrategy detectRegisteredConcurrencyStrategy() {
        HystrixConcurrencyStrategy registeredStrategy = HystrixPlugins.getInstance()
                .getConcurrencyStrategy();
        if (existingConcurrencyStrategy == null) {
            return registeredStrategy;
        }
        // Hystrix registered a default Strategy.
        if (registeredStrategy instanceof HystrixConcurrencyStrategyDefault) {
            return existingConcurrencyStrategy;
        }
        // If registeredStrategy not the default and not some use bean of existingConcurrencyStrategy.
        if (!existingConcurrencyStrategy.equals(registeredStrategy)) {
            LOG.warn("Multiple HystrixConcurrencyStrategy detected. Bean of HystrixConcurrencyStrategy was used.");
        }
        return existingConcurrencyStrategy;
    }

}
