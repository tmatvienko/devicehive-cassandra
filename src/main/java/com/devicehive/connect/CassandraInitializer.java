package com.devicehive.connect;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.devicehive.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by tmatvienko on 2/9/15.
 */
@Component
@Scope("singleton")
public class CassandraInitializer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraInitializer.class);

    @Autowired
    protected Environment environment;
    @Autowired
    private ClusterConfiguration clusterConfiguration;

    @Override
    public void afterPropertiesSet() throws Exception {
        Cluster cluster = Cluster.builder().addContactPoint(clusterConfiguration.getClusterConfig().getCassandraContactpoints()).build();
        Session session = cluster.newSession();
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + environment.getProperty(Constants.CASSANDRA_KEYSPACE) +
                " WITH replication = {'class':'SimpleStrategy', 'replication_factor':2};");
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + environment.getProperty(Constants.CASSANDRA_KEYSPACE_TEST) +
                " WITH replication = {'class':'SimpleStrategy', 'replication_factor':2};");
        session.close();
        cluster.close();
    }
}
