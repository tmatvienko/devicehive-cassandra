package com.devicehive.service;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Update;
import com.devicehive.domain.DeviceCommand;
import com.devicehive.domain.wrappers.DeviceCommandWrapper;
import com.devicehive.repository.DeviceCommandRepository;
import com.devicehive.utils.MessageUtils;
import com.devicehive.utils.mappers.CommandRowMapper;
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
 * Created by tmatvienko on 2/13/15.
 */
@Service
public class DeviceCommandService {

    @Autowired
    private CqlOperations cqlTemplate;
    @Autowired
    private DeviceCommandRepository commandRepository;
    @Autowired
    private MessageUtils messageUtils;

    public List<DeviceCommandWrapper> get(int count, final String commandId, final String deviceGuids, final String commandNames, final Timestamp timestamp) {
        Select.Where select = QueryBuilder.select().from("device_command").where();
        if (StringUtils.isNotBlank(deviceGuids)) {
            select.and(QueryBuilder.in("device_guid", messageUtils.getDeviceGuids(deviceGuids)));
        }
        if (StringUtils.isNotBlank(commandId)) {
            select.and(QueryBuilder.in("id", commandId));
        }
        List<DeviceCommandWrapper> commands = cqlTemplate.query(select.limit(count).allowFiltering(), new CommandRowMapper());
        if (timestamp != null) {
            CollectionUtils.filter(commands, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return timestamp.before(((DeviceCommandWrapper) o).getTimestamp());
                }
            });
        }
        if (StringUtils.isNotEmpty(commandNames)) {
            CollectionUtils.filter(commands, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return commandNames.contains(((DeviceCommandWrapper) o).getCommand());
                }
            });
        }
        return commands;
    }

    public Long getCommandsCount() {
        return commandRepository.count();
    }

    @Async
    public void updateDeviceCommmand(final DeviceCommand command) {
        Update.Assignments update = QueryBuilder.update("device_command").with();
        if (command.getCommand() != null) {
            update.and(QueryBuilder.set("command", command.getCommand()));
        }
        if (command.getParameters() != null) {
            update.and(QueryBuilder.set("parameters", command.getParameters()));
        }
        if (command.getLifetime() != null) {
            update.and(QueryBuilder.set("lifetime", command.getLifetime()));
        }
        if (command.getStatus() != null) {
            update.and(QueryBuilder.set("status", command.getStatus()));
        }
        if (command.getResult() != null) {
            update.and(QueryBuilder.set("result", command.getResult()));
        }
        update.and(QueryBuilder.set("updated", true));
        update.where(QueryBuilder.eq("id", command.getId())).and(QueryBuilder.eq("device_guid", command.getDeviceGuid()));
        cqlTemplate.executeAsynchronously(update);
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
