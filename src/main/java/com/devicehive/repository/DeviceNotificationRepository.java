package com.devicehive.repository;

import com.devicehive.domain.DeviceNotification;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tmatvienko on 2/5/15.
 */
public interface DeviceNotificationRepository extends CassandraRepository<DeviceNotification> {

    @Query("select * from device_notification where device_guid = ?0")
    Iterable<DeviceNotification> findByDeviceGuid(String deviceGuid);

    @Query("select * from device_notification where id >= minTimeuuid(?0) ALLOW FILTERING")
    Iterable<DeviceNotification> findByTimestamp(Date timestamp);

    @Query("select * from device_notification where id = ?0 ALLOW FILTERING")
    DeviceNotification findById(UUID id);
}
