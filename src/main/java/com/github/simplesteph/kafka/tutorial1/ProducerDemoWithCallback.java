package com.github.simplesteph.kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoWithCallback {
    //Write "psvm" and an option will appearace where it add the
    //main code
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //It's advisable check kafka documentation in google'

        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);

        String bootstrapServers = "127.0.0.1:9092";
        //Create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for(int i =0; i<10; i++){


            //create a producer record
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>("test_topic", "hello world" + Integer.toString(i));


            //send data - asynchronous
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //execute everytime a record is successfully sent or an exception is throw
                    if (e == null) {
                        // the record was sucessfulle sent
                        logger.info("Received new metadata.  \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Partition:" + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp());

                    } else {
                        logger.error("Error while producing", e);
                    }
                }
            }); // block the .send() to make it synchronous - don't do this in production
        }
        //flush data
        producer.flush();
        //flush and close producer
        producer.close();
    }
}
