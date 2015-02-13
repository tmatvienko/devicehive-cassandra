package com.devicehive.message;

import com.devicehive.connect.ClusterConfiguration;
import com.devicehive.domain.ClusterConfig;
import com.devicehive.domain.DeviceCommand;
import com.devicehive.domain.DeviceNotification;
import com.devicehive.message.converter.DeviceCommandConverter;
import com.devicehive.message.converter.DeviceNotificationConverter;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ConsumerConnector commandConnector;
    private ConsumerConnector commandUpdateConnector;

    private static final String ZOOKEEPER_CONNECT = "zookeeper.connect";
    private static final String GROUP_ID = "group.id";
    private static final String ZOOKEEPER_SESSION_TIMEOUT_MS = "zookeeper.session.timeout.ms";
    private static final String ZOOKEEPER_SYNC_TIME_MS = "zookeeper.sync.time.ms";
    private static final String AUTO_COMMIT_INTERVAL_MS = "auto.commit.interval.ms";
    private static final String NOTIFICATION_TOPIC_NAME = "notification.topic.name";
    private static final String COMMAND_TOPIC_NAME = "command.topic.name";
    private static final String COMMAND_UPDATE_TOPIC_NAME = "command.update.topic.name";

    @Autowired
    private Environment environment;
    @Autowired
    private MessageConsumer messageConsumer;
    @Autowired
    private ClusterConfiguration cassandraConfiguration;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Kafka consumer initialization...");
        ClusterConfig config = cassandraConfiguration.getClusterConfig();
        Properties consumerProperties = new Properties();
        consumerProperties.put(ZOOKEEPER_CONNECT, config.getZookeeperConnect());
        consumerProperties.put(GROUP_ID, environment.getProperty(GROUP_ID));
        consumerProperties.put(ZOOKEEPER_SESSION_TIMEOUT_MS, environment.getProperty(ZOOKEEPER_SESSION_TIMEOUT_MS));
        consumerProperties.put(ZOOKEEPER_SYNC_TIME_MS, environment.getProperty(ZOOKEEPER_SYNC_TIME_MS));
        consumerProperties.put(AUTO_COMMIT_INTERVAL_MS, environment.getProperty(AUTO_COMMIT_INTERVAL_MS));
        this.notificationConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerProperties));
        this.commandConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerProperties));
        this.commandUpdateConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerProperties));
        LOGGER.info("Notification consumer config: {}", consumerProperties);

        final Integer threadsCountStr = config.getThreadsCount();
        int threadsCount = threadsCountStr != null ? threadsCountStr : 1;

        Map<String, Integer> notificationTopicCountMap = new HashMap();
        notificationTopicCountMap.put(environment.getProperty(NOTIFICATION_TOPIC_NAME), threadsCount);

        Map<String, Integer> commandTopicCountMap = new HashMap();
        commandTopicCountMap.put(environment.getProperty(COMMAND_TOPIC_NAME), threadsCount);

        Map<String, Integer> commandUpdateTopicCountMap = new HashMap();
        commandUpdateTopicCountMap.put(environment.getProperty(COMMAND_UPDATE_TOPIC_NAME), threadsCount);

        Map<String, List<KafkaStream<String, DeviceNotification>>> notificationStreams = notificationConnector.createMessageStreams(
                notificationTopicCountMap, new StringDecoder(new VerifiableProperties()),
                new DeviceNotificationConverter(new VerifiableProperties()));

        Map<String, List<KafkaStream<String, DeviceCommand>>> commandStreams = commandConnector.createMessageStreams(
                commandTopicCountMap, new StringDecoder(new VerifiableProperties()),
                new DeviceCommandConverter(new VerifiableProperties()));

        Map<String, List<KafkaStream<String, DeviceCommand>>> commandUpdateStreams = commandUpdateConnector.createMessageStreams(
                commandUpdateTopicCountMap, new StringDecoder(new VerifiableProperties()),
                new DeviceCommandConverter(new VerifiableProperties()));

        List<KafkaStream<String, DeviceNotification>> notificationStream = notificationStreams.get(
                environment.getProperty(NOTIFICATION_TOPIC_NAME));

        List<KafkaStream<String, DeviceCommand>> commandStream = commandStreams.get(
                environment.getProperty(COMMAND_TOPIC_NAME));

        List<KafkaStream<String, DeviceCommand>> commandUpdateStream = commandUpdateStreams.get(
                environment.getProperty(COMMAND_UPDATE_TOPIC_NAME));

        int threadNumber = 0;
        for (final KafkaStream stream : notificationStream) {
            messageConsumer.subscribeOnNotifications(stream, threadNumber);
            threadNumber++;
        }

        threadNumber = 0;
        for (final KafkaStream stream : commandStream) {
            messageConsumer.subscribeOnCommands(stream, threadNumber);
            threadNumber++;
        }

        threadNumber = 0;
        for (final KafkaStream stream : commandUpdateStream) {
            messageConsumer.subscribeOnCommandsUpdate(stream, threadNumber);
            threadNumber++;
        }
    }

    @Override
    public void destroy() throws Exception {
        if (notificationConnector != null) {
            notificationConnector.shutdown();
        }

        if (commandConnector != null) {
            commandConnector.shutdown();
        }

        if (commandUpdateConnector != null) {
            commandUpdateConnector.shutdown();
        }
    }

}
