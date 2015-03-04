package com.devicehive.service;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.devicehive.domain.wrappers.DeviceCommandWrapper;
import com.devicehive.domain.wrappers.DeviceNotificationWrapper;
import com.devicehive.repository.DeviceNotificationRepository;
import com.devicehive.utils.MessageUtils;
import com.devicehive.utils.mappers.NotificationRowMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by tatyana on 2/11/15.
 */
@Service
public class DeviceNotificationService {

    private static final Integer DEFAULT_MAX_COUNT = 1000;

    @Autowired
    private CqlOperations cqlTemplate;
    @Autowired
    private DeviceNotificationRepository notificationRepository;
    @Autowired
    private MessageUtils messageUtils;

    public List<DeviceNotificationWrapper> get(Integer count, String commandId, String deviceGuids, final Timestamp timestamp) {
        Select.Where select = QueryBuilder.select().from("device_notification").where();
        if (StringUtils.isNotBlank(deviceGuids)) {
            select.and(QueryBuilder.in("device_guid", messageUtils.getDeviceGuids(deviceGuids)));
        }
        if (StringUtils.isNotBlank(commandId)) {
            select.and(QueryBuilder.in("id", commandId));
        }
        if (count == null) {
            count = DEFAULT_MAX_COUNT;
        }
        List<DeviceNotificationWrapper> commands = cqlTemplate.query(select.limit(count).allowFiltering(), new NotificationRowMapper());
        if (timestamp != null) {
            CollectionUtils.filter(commands, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return timestamp.before(((DeviceNotificationWrapper) o).getTimestamp());
                }
            });
        }
        return commands;
    }

    public Long getNotificationsCount() {
        return notificationRepository.count();
    }

    @Async
    public void delete(String deviceGuids) {
        Delete.Where delete = QueryBuilder.delete().from("device_command").where();
        if (StringUtils.isNotEmpty(deviceGuids)) {
            delete.and(QueryBuilder.in("device_guid", messageUtils.getDeviceGuids(deviceGuids)));
        }
        cqlTemplate.execute(delete);
    }
}
