package com.devicehive.service;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.devicehive.domain.DeviceCommand;
import com.devicehive.repository.DeviceCommandRepository;
import com.devicehive.utils.mappers.CommandRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by tmatvienko on 2/13/15.
 */
@Service
public class DeviceCommandsService {

    private static final Integer DEFAULT_MAX_COUNT = 100;

    @Autowired
    private CqlOperations cqlTemplate;
    @Autowired
    private DeviceCommandRepository commandRepository;

    public List<DeviceCommand> getLast(Integer count) {
        Select select = QueryBuilder.select().from("device_command").limit(count);
        return cqlTemplate.query(select, new CommandRowMapper());
    }

    public List<DeviceCommand> getByDevice(String deviceGuid, Integer count) {
        if (count == null) {
            count = DEFAULT_MAX_COUNT;
        }
        Select select = QueryBuilder.select().from("device_command").where(QueryBuilder.eq("device_guid",
                deviceGuid)).limit(count);
        return cqlTemplate.query(select, new CommandRowMapper());
    }

    public Long getCommandsCount() {
        return commandRepository.count();
    }

    public void deleteAllCommands() {
        commandRepository.deleteAll();
    }

    public void deleteByDeviceGuid(String deviceGuid) {
        Delete delete = QueryBuilder.delete().from("device_command").where(QueryBuilder.eq("device_guid",
                deviceGuid)).ifExists();
        cqlTemplate.execute(delete);
    }

    public List<DeviceCommand> getNewCommands(Date date) {
        return (List<DeviceCommand>) commandRepository.findByTimestamp(date);
    }
}
