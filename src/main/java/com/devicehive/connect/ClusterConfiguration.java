package com.devicehive.connect;

import com.devicehive.domain.ClusterConfig;
import com.devicehive.domain.DeviceNotification;
import com.devicehive.exception.HiveException;
import com.devicehive.utils.Constants;
import com.devicehive.utils.messages.Messages;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tmatvienko on 2/5/15.
 */
@Configuration
@PropertySource(value = {"classpath:app.properties"})
@EnableCassandraRepositories(basePackages = {"com.devicehive.repository"})
public class ClusterConfiguration extends AbstractCassandraConfiguration implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterConfiguration.class);

    private ClusterConfig clusterConfig;

    @Autowired
    protected Environment environment;

    @Bean
    public CassandraClusterFactoryBean cluster() {
        final String contactPoints = clusterConfig.getCassandraContactpoints();
        final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contactPoints);
        List<CreateKeyspaceSpecification> createKeyspaceSpecifications = Arrays.asList(
                new CreateKeyspaceSpecification(environment.getProperty(Constants.CASSANDRA_KEYSPACE)).ifNotExists(),
                new CreateKeyspaceSpecification(environment.getProperty(Constants.CASSANDRA_KEYSPACE_TEST)).ifNotExists()
        );
        cluster.setKeyspaceCreations(createKeyspaceSpecifications);
        LOGGER.info("Cassandra cluster started on {}", contactPoints);
        return cluster;
    }

    @Override
    protected String getKeyspaceName() {
        return environment.getProperty(Constants.CASSANDRA_KEYSPACE);
    }

    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }

    @Bean
    public CassandraAdminOperations cassandraTemplate() throws Exception {
        CassandraAdminOperations adminTemplate = new CassandraAdminTemplate(this.session().getObject(), this.cassandraConverter());
        adminTemplate.createTable(true, CqlIdentifier.cqlId("device_notification"), DeviceNotification.class,
                new HashMap<String, Object>());
        return adminTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
            final GenericUrl url = new GenericUrl(environment.getProperty(Constants.CLUSTER_CONFIG_ENDPOINT));
            final HttpRequest request = requestFactory.buildGetRequest(url);
            final JsonObject jsonObject = (JsonObject) new JsonParser().parse(request.execute().parseAsString());
            if (jsonObject.get(Constants.METADATA_BROKER_LIST) == null) {
                LOGGER.error("metadata.broker.list config was not provided by DeviceHive API");
                throw new HiveException(String.format(Messages.WRONG_CONFIG, Constants.METADATA_BROKER_LIST));
            }
            if (jsonObject.get(Constants.ZOOKEEPER_CONNECT) == null) {
                LOGGER.error("zookeeper.connect config was not provided by DeviceHive API");
                throw new HiveException(String.format(Messages.WRONG_CONFIG, Constants.ZOOKEEPER_CONNECT));
            }
            if (jsonObject.get(Constants.CASSANDRA_CONTACTPOINTS) == null) {
                LOGGER.error("cassandra.contactpoints config was not provided by DeviceHive API");
                throw new HiveException(String.format(Messages.WRONG_CONFIG, Constants.CASSANDRA_CONTACTPOINTS));
            }
            this.clusterConfig = new ClusterConfig(jsonObject.get(Constants.METADATA_BROKER_LIST).getAsString(),
                    jsonObject.get(Constants.ZOOKEEPER_CONNECT).getAsString(),
                    jsonObject.get(Constants.THREADS_COUNT).getAsInt(),
                    jsonObject.get(Constants.CASSANDRA_CONTACTPOINTS).getAsString());
        } catch (IOException e) {
            LOGGER.error(Messages.GET_EXECUTION_ERROR, e);
            throw new HiveException(Messages.GET_EXECUTION_ERROR);
        }
    }

    public ClusterConfig getClusterConfig() {
        return clusterConfig;
    }
}