package com.devicehive.service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.devicehive.domain.DeviceNotification;
import com.devicehive.utils.NotificationRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tatyana on 2/11/15.
 */
@Service
public class DeviceNotificationService {

    private static final Integer DEFAULT_MAX_COUNT = 100;

    @Autowired
    private CqlOperations cqlTemplate;

    public List<DeviceNotification> getLast(Integer count) {
        Select select = QueryBuilder.select().from("device_notification").limit(count);
        return cqlTemplate.query(select, new NotificationRowMapper());
    }

    public List<DeviceNotification> getByDevice(String deviceGuid, Integer count) {
        if (count == null) {
            count = DEFAULT_MAX_COUNT;
        }
        Select select = QueryBuilder.select().from("device_notification").where(QueryBuilder.eq("device_guid",
                deviceGuid)).limit(count);
        return cqlTemplate.query(select, new NotificationRowMapper());
    }
}
