package com.devicehive;

import com.datastax.driver.core.utils.UUIDs;
import com.devicehive.domain.DeviceNotification;
import com.devicehive.repository.DeviceNotificationRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by tmatvienko on 2/2/15.
 */
public class NotificationRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private DeviceNotificationRepository notificationRepository;

    @Test
    public void repositoryStoresAndRetrievesEvents() {
        final UUID id = UUIDs.timeBased();
        final DeviceNotification notif1 = new DeviceNotification(id, "notification1", deviceGuid, date, null);
        final DeviceNotification notif2 = new DeviceNotification(UUIDs.timeBased(), "notification2", deviceGuid, date, null);
        notificationRepository.save(ImmutableSet.of(notif1, notif2));

        Iterable<DeviceNotification> notifications = notificationRepository.findByDeviceGuid(deviceGuid);

        DeviceNotification notification = notificationRepository.findById(id);

        assertThat(notifications, hasItem(notif1));
        assertThat(notifications, hasItem(notif2));
        assertThat(notifications, hasItem(notification));
    }

    @Test
    public void repositoryDeletesStoredEvents() {
        final DeviceNotification notif1 = new DeviceNotification(UUIDs.timeBased(), "notification1", deviceGuid, date, null);
        final DeviceNotification notif2 = new DeviceNotification(UUIDs.timeBased(), "notification1", deviceGuid, date, null);
        notificationRepository.save(ImmutableSet.of(notif1, notif2));

        notificationRepository.delete(notif1);
        notificationRepository.delete(notif2);

        Iterable<DeviceNotification> notifications = notificationRepository.findByDeviceGuid(deviceGuid);

        assertThat(notifications, not(hasItem(notif1)));
        assertThat(notifications, not(hasItem(notif2)));
    }
}
