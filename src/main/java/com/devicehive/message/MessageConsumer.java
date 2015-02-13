package com.devicehive.message;

import com.devicehive.domain.DeviceCommand;
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
    public void subscribeOnNotifications(KafkaStream a_stream, int a_threadNumber) throws InterruptedException {
        LOGGER.info("{}: Kafka device notifications consumer started... {} ", Thread.currentThread().getName(), a_threadNumber);
        ConsumerIterator<String, DeviceNotification> it = a_stream.iterator();
        while (it.hasNext()) {
            DeviceNotification message = it.next().message();
            LOGGER.info("Notification {}: Thread {}: {}", Thread.currentThread().getName(), a_threadNumber, message);
            cassandraTemplate.insertAsynchronously(message);
        }
        LOGGER.info("Shutting down Thread: " + a_threadNumber);
    }

    @Async
    public void subscribeOnCommands(KafkaStream a_stream, int a_threadNumber) throws InterruptedException {
        LOGGER.info("{}: Kafka device commands consumer started... {} ", Thread.currentThread().getName(), a_threadNumber);
        ConsumerIterator<String, DeviceCommand> it = a_stream.iterator();
        while (it.hasNext()) {
            DeviceCommand message = it.next().message();
            LOGGER.info("Command {}: Thread {}: {}", Thread.currentThread().getName(), a_threadNumber, message);
            cassandraTemplate.insert(message);
        }
        LOGGER.info("Shutting down Thread: " + a_threadNumber);
    }

    @Async
    public void subscribeOnCommandsUpdate(KafkaStream a_stream, int a_threadNumber) throws InterruptedException {
        LOGGER.info("{}: Kafka device commands update consumer started... {} ", Thread.currentThread().getName(), a_threadNumber);
        ConsumerIterator<String, DeviceCommand> it = a_stream.iterator();
        while (it.hasNext()) {
            DeviceCommand message = it.next().message();
            LOGGER.info("CommandUpdate {}: Thread {}: {}", Thread.currentThread().getName(), a_threadNumber, message);
            cassandraTemplate.updateAsynchronously(message);
        }
        LOGGER.info("Shutting down Thread: " + a_threadNumber);
    }
}
