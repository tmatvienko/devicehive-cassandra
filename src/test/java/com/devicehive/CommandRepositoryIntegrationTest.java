package com.devicehive;

import com.datastax.driver.core.utils.UUIDs;
import com.devicehive.domain.DeviceCommand;
import com.devicehive.repository.DeviceCommandRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by tmatvienko on 2/13/15.
 */
public class CommandRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private DeviceCommandRepository commandRepository;

    @Test
    public void repositoryStoresAndRetrievesEvents() {
        final UUID id = UUIDs.timeBased();
        final DeviceCommand command1 = new DeviceCommand(id, deviceGuid, date, "command1", null, "0", null, null, null, null, null);
        final DeviceCommand command2 = new DeviceCommand(UUIDs.timeBased(), deviceGuid, date, "command2", null, "0", null, null, null, null, null);
        commandRepository.save(ImmutableSet.of(command1, command2));

        Iterable<DeviceCommand> commands = commandRepository.findByDeviceGuid(deviceGuid);

        DeviceCommand deviceCommand = commandRepository.findById(id);

        assertThat(commands, hasItem(command1));
        assertThat(commands, hasItem(command2));
        assertThat(commands, hasItem(deviceCommand));
    }

    @Test
    public void repositoryDeletesStoredEvents() {
        final DeviceCommand command1 = new DeviceCommand(UUIDs.timeBased(), deviceGuid, date, "command", null, "0", null, null, null, null, null);
        final DeviceCommand command2 = new DeviceCommand(UUIDs.timeBased(), deviceGuid, date, "command", null, "0", null, null, null, null, null);
        commandRepository.save(ImmutableSet.of(command1, command2));

        commandRepository.delete(command1);
        commandRepository.delete(command2);

        Iterable<DeviceCommand> notifications = commandRepository.findByDeviceGuid(deviceGuid);

        assertThat(notifications, not(hasItem(command1)));
        assertThat(notifications, not(hasItem(command2)));
    }

    @Test
    public void repositoryStoresAndRetrievesEventsByDate() {
        final DeviceCommand command1 = new DeviceCommand(UUIDs.timeBased(), deviceGuid, date, "command", null, "0", null, null, null, null, null);
        final DeviceCommand command2 = new DeviceCommand(UUIDs.timeBased(), deviceGuid, date, "command", null, "0", null, null, null, null, null);
        commandRepository.save(ImmutableSet.of(command1, command2));

        Iterable<DeviceCommand> notifications = commandRepository.findByTimestamp(date);

        assertThat(notifications, hasItem(command1));
        assertThat(notifications, hasItem(command2));
    }
}
