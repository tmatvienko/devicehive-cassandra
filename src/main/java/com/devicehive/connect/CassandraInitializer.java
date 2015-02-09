package com.devicehive.connect;

import com.devicehive.domain.DeviceNotification;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by tmatvienko on 2/9/15.
 */
@Component
@Scope("singleton")
public class CassandraInitializer implements InitializingBean {

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        adminTemplate.createTable(true, CqlIdentifier.cqlId("device_notification"), DeviceNotification.class, new HashMap<String, Object>());
    }
}
