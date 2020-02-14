package com.github.simplesteph.kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoKeys {
    //Write "psvm" and an option will appearace where it add the
    //main code
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //It's advisable check kafka documentation in google'

        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

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
            String topic ="test_topic";
            String value = "hello world " +  Integer.toString(i);
            String key = "id_ " + Integer.toString(i);
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(topic, key, value);

            logger.info("key: " + key);  // log the key

            // id_0 partition 1
            // id_1 partition 0
            // id_2 partition 2
            // id_3 partition 0
            // id_4 partition 2
            // id_5 partition 0
            // id_6 partition 2
            // id_7 partition 0
            // id_8 partition 2
            // id_9 partition 0


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
            }).get(); // block the .send() to make it synchronous - don't do this in production
        }
        //flush data
        producer.flush();
        //flush and close producer
        producer.close();
    }
}
