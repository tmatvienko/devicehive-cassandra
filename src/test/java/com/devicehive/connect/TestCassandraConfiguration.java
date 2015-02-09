package com.devicehive.connect;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * Created by tmatvienko on 2/6/15.
 */
@Configuration
@PropertySource(value = {"classpath:cassandra.properties"})
@EnableCassandraRepositories(basePackages = {"com.devicehive.repository"})
public class TestCassandraConfiguration extends CassandraConfiguration {
    @Override
    protected String getKeyspaceName() {
        return environment.getProperty("cassandra.keyspace.test");
    }
}
