package com.devicehive.utils.mappers;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.devicehive.domain.DeviceCommand;
import org.springframework.cassandra.core.RowMapper;

/**
 * Created by tmatvienko on 2/13/15.
 */
public class CommandRowMapper implements RowMapper<DeviceCommand> {
    @Override
    public DeviceCommand mapRow(Row row, int i) throws DriverException {
        return new DeviceCommand(row.getString("id"), row.getString("device_guid"),
                row.getDate("timestamp"), row.getString("command"), row.getString("parameters"),
                row.getString("userId"), row.getInt("lifetime"), row.getInt("flags"), row.getString("status"),
                row.getString("result"));
    }
}
