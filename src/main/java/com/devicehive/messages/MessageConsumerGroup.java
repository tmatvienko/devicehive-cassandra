package com.devicehive.messages;

import com.devicehive.messages.converter.DeviceNotificationConverter;
import com.devicehive.domain.DeviceNotification;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * Created by tmatvienko on 2/5/15.
 */
@Component
@Scope("singleton")
@PropertySource(value = {"classpath:kafka.properties"})
public class MessageConsumerGroup implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumerGroup.class);

    private ConsumerConnector notificationConnector;

    private static final String ZOOKEEPER_CONNECT = "zookeeper.connect";
    private static final String GROUP_ID = "group.id";
    private static final String ZOOKEEPER_SESSION_TIMEOUT_MS = "zookeeper.session.timeout.ms";
    private static final String ZOOKEEPER_SYNC_TIME_MS = "zookeeper.sync.time.ms";
    private static final String AUTO_COMMIT_INTERVAL_MS = "auto.commit.interval.ms";
    private static final String NOTIFICATION_TOPIC_NAME = "notification.topic.name";
    private static final String THREADS_COUNT = "threads.count";

    @Autowired
    private Environment environment;
    @Autowired
    private MessageConsumer messageConsumer;

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties consumerProperties = new Properties();
        consumerProperties.put(ZOOKEEPER_CONNECT, environment.getProperty(ZOOKEEPER_CONNECT));
        consumerProperties.put(GROUP_ID, environment.getProperty(GROUP_ID));
        consumerProperties.put(ZOOKEEPER_SESSION_TIMEOUT_MS, environment.getProperty(ZOOKEEPER_SESSION_TIMEOUT_MS));
        consumerProperties.put(ZOOKEEPER_SYNC_TIME_MS, environment.getProperty(ZOOKEEPER_SYNC_TIME_MS));
        consumerProperties.put(AUTO_COMMIT_INTERVAL_MS, environment.getProperty(AUTO_COMMIT_INTERVAL_MS));
        this.notificationConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerProperties));

        final String threadsCountStr = environment.getProperty(THREADS_COUNT);
        int threadsCount = threadsCountStr != null ? Integer.valueOf(threadsCountStr) : 1;

        List<KafkaStream<String, DeviceNotification>> notificationStreams = notificationConnector.createMessageStreamsByFilter(
                new Whitelist(String.format("%s.*", environment.getProperty(NOTIFICATION_TOPIC_NAME))), threadsCount,
                new kafka.serializer.StringDecoder(new VerifiableProperties()),
                new DeviceNotificationConverter(new VerifiableProperties()));

        int threadNumber = 0;
        for (final KafkaStream<String, DeviceNotification> stream : notificationStreams) {
            messageConsumer.subscribe(stream, threadNumber);
            threadNumber++;
        }
    }

    @Override
    public void destroy() throws Exception {
        if (notificationConnector != null) {
            notificationConnector.shutdown();
        }
    }

}
