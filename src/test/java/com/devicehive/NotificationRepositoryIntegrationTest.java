package com.devicehive;

import com.datastax.driver.core.utils.UUIDs;
import com.devicehive.domain.DeviceNotification;
import com.devicehive.repository.DeviceNotificationRepository;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;
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
        final String id = String.valueOf(UUIDs.timeBased().timestamp());
        final DeviceNotification notif1 = new DeviceNotification(id, deviceGuid, date, "notification1", null);
        final DeviceNotification notif2 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid2, date, "notification2", null);
        notificationRepository.save(ImmutableSet.of(notif1, notif2));

        Iterable<DeviceNotification> notifications = notificationRepository.findByTimestamp(date);

        assertThat(notifications, hasItem(notif1));
        assertThat(notifications, hasItem(notif2));
    }

    @Test
    public void repositoryDeletesStoredEvents() {
        final DeviceNotification notif1 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid, date, "notification1", null);
        final DeviceNotification notif2 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid, date, "notification1", null);
        notificationRepository.save(ImmutableSet.of(notif1, notif2));

        notificationRepository.delete(notif1);
        notificationRepository.delete(notif2);

        Iterable<DeviceNotification> notifications = notificationRepository.findByDeviceGuid(deviceGuid);

        assertThat(notifications, not(hasItem(notif1)));
        assertThat(notifications, not(hasItem(notif2)));
    }

    @Test
    public void repositoryStoresAndRetrievesEventsByDate() {
        final DeviceNotification notif1 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid, date, "notification1", null);
        final DeviceNotification notif2 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid2, date, "notification1", null);
        notificationRepository.save(ImmutableSet.of(notif1, notif2));

        Iterable<DeviceNotification> notifications = notificationRepository.findByTimestamp(date);

        assertThat(notifications, hasItem(notif1));
        assertThat(notifications, hasItem(notif2));
    }

    @Test
    public void repositoryStoresAndRetrievesEventsByDates() {
        final Long curMillis = System.currentTimeMillis();
        final Timestamp date1 = new Timestamp(curMillis);
        final Timestamp date2 = new Timestamp(curMillis + 1000);
        final Timestamp date3 = new Timestamp(curMillis + 10000);
        final DeviceNotification notif1 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid, date1, "notification1", null);
        final DeviceNotification notif2 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid2, date2, "notification1", null);
        final DeviceNotification notif3 = new DeviceNotification(String.valueOf(UUIDs.timeBased()), deviceGuid2, date3, "notification1", null);
        notificationRepository.save(ImmutableSet.of(notif1, notif2, notif3));

        Iterable<DeviceNotification> notifications = notificationRepository.findInPeriod(date1, date2);

        assertThat(notifications, hasItem(notif1));
        assertThat(notifications, hasItem(notif2));
        assertThat(notifications, not(hasItem(notif3)));

        notifications = notificationRepository.findInPeriod(date2, date3);

        assertThat(notifications, not(hasItem(notif1)));
        assertThat(notifications, hasItem(notif2));
        assertThat(notifications, hasItem(notif3));
    }
}
