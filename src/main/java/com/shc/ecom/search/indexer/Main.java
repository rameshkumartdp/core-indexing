package com.shc.ecom.search.indexer;

import com.beust.jcommander.JCommander;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.shc.common.misc.util.MiscUtil;
import com.shc.ecom.search.config.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import static com.shc.common.monitoring.servlet.listener.MyHealthCheckServletContextListener.HEALTH_CHECK_REGISTRY;
import static com.shc.common.monitoring.servlet.listener.MyMetricsServletContextListener.METRIC_REGISTRY;

@Component
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getName());

    @Autowired
    private ProcessManager processManager;


    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        new JCommander(arguments, args);
        LOGGER.info(" **** ");
        PropertiesLoader.load(arguments.getPropertyFile());
        final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        context.getBean(Main.class).processManager.handle(arguments);
        context.close();
        LOGGER.info(" **** ");
        MiscUtil.exitWithSuccess();
    }

    @Bean
    public HealthCheckRegistry getHealthCheckRegistry() {
        return HEALTH_CHECK_REGISTRY;
    }

    @Bean
    public MetricRegistry getMetricRegistry() {
        return METRIC_REGISTRY;
    }

}
