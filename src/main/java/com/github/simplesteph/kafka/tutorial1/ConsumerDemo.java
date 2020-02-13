package com.github.simplesteph.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConsumerDemo {
   Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());

   String bootstrapServers = "127.0.0.1";

   Properties properties = new Properties();

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
