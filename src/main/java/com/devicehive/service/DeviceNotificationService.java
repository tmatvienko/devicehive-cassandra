package com.devicehive.service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.devicehive.domain.DeviceNotification;
import com.devicehive.repository.DeviceNotificationRepository;
import com.devicehive.utils.mappers.NotificationRowMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by tatyana on 2/11/15.
 */
@Service
public class DeviceNotificationService {

    private static final Integer DEFAULT_MAX_COUNT = 100;

    @Autowired
    private CqlOperations cqlTemplate;
    @Autowired
    private DeviceNotificationRepository notificationRepository;

    public List<DeviceNotification> get(Integer count, String deviceGuids) {
        Select select = QueryBuilder.select().from("device_notification");
        if (StringUtils.isNotBlank(deviceGuids)) {
            String[] guids = StringUtils.split(StringUtils.deleteWhitespace(deviceGuids), ',');
            select.where(QueryBuilder.in("device_guid", guids));
        }
        return cqlTemplate.query(select.limit(count), new NotificationRowMapper());
    }

    public List<DeviceNotification> getByDevice(String deviceGuid, Integer count) {
        if (count == null) {
            count = DEFAULT_MAX_COUNT;
        }
        Select select = QueryBuilder.select().from("device_notification").where(QueryBuilder.eq("device_guid",
                deviceGuid)).limit(count);
        return cqlTemplate.query(select, new NotificationRowMapper());
    }

    public Long getNotificationsCount() {
        return notificationRepository.count();
    }

    public List<DeviceNotification> getNewNotifications(Date date) {
        return (List<DeviceNotification>) notificationRepository.findByTimestamp(date);
    }
}
