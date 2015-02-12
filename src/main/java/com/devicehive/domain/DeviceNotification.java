package com.devicehive.domain;

import com.google.gson.annotations.SerializedName;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Indexed;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tmatvienko on 2/5/15.
 */
@Table(value = "device_notification")
public class DeviceNotification implements Serializable {

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @SerializedName("id")
    private final UUID id;

    @PrimaryKeyColumn(name = "device_guid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @SerializedName("device_guid")
    private final String deviceGuid;

    @Column
    @Indexed(value = "timestamp_idx")
    @SerializedName("timestamp")
    private final Date timestamp;

    @Column
    @SerializedName("notification")
    private final String notification;

    @Column
    @SerializedName("parameters")
    private final String parameters;

    public DeviceNotification(UUID id, String deviceGuid, Date timestamp, String notification, String parameters) {
        this.id = id;
        this.deviceGuid = deviceGuid;
        this.timestamp = timestamp;
        this.notification = notification;
        this.parameters= parameters;
    }

    public UUID getId() {
        return id;
    }

    public String getNotification() {
        return notification;
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceNotification that = (DeviceNotification) o;

        if (deviceGuid != null ? !deviceGuid.equals(that.deviceGuid) : that.deviceGuid != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (notification != null ? !notification.equals(that.notification) : that.notification != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (deviceGuid != null ? deviceGuid.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (notification != null ? notification.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceNotification{" +
                "id=" + id +
                ", notification='" + notification + '\'' +
                ", deviceGuid=" + deviceGuid +
                ", timestamp=" + timestamp +
                '}';
    }
}
