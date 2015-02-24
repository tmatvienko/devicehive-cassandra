package com.devicehive.utils.mappers;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.devicehive.domain.DeviceNotification;
import org.springframework.cassandra.core.RowMapper;

/**
 * Created by tatyana on 2/11/15.
 */
public class NotificationRowMapper implements RowMapper<DeviceNotification> {

    @Override
    public DeviceNotification mapRow(Row row, int i) throws DriverException {
        return new DeviceNotification(row.getString("id"), row.getString("device_guid"),
                row.getDate("timestamp"), row.getString("notification"), row.getString("parameters"));
    }
}
