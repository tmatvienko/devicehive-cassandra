package com.devicehive.repository;

import com.devicehive.domain.DeviceCommand;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tmatvienko on 2/13/15.
 */
public interface DeviceCommandRepository extends CassandraRepository<DeviceCommand> {

    @Query("select * from device_command where device_guid = ?0")
    Iterable<DeviceCommand> findByDeviceGuid(String deviceGuid);

    @Query("select * from device_command where id >= minTimeuuid(?0) ALLOW FILTERING")
    Iterable<DeviceCommand> findByTimestamp(Date timestamp);

    @Query("select * from device_command where id = ?0 ALLOW FILTERING")
    DeviceCommand findById(UUID id);
}
