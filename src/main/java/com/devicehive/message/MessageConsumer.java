package com.devicehive.message;

import com.devicehive.domain.DeviceNotification;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by tmatvienko on 2/5/15.
 */
@Component
public class MessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private CassandraOperations cassandraTemplate;

    @Async
    public void subscribe(KafkaStream a_stream, int a_threadNumber) throws InterruptedException {
        LOGGER.info("{}: Kafka device notifications consumer started... {} ", Thread.currentThread().getName(), a_threadNumber);
        ConsumerIterator<String, DeviceNotification> it = a_stream.iterator();
        while (it.hasNext()) {
            DeviceNotification message = it.next().message();
            LOGGER.debug("{}: Thread {}: {}", Thread.currentThread().getName(), a_threadNumber, message);
            cassandraTemplate.insertAsynchronously(message);
        }
        LOGGER.info("Shutting down Thread: " + a_threadNumber);
    }
}