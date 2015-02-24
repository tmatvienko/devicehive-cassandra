package com.devicehive.domain.wrappers;

import com.devicehive.domain.JsonStringWrapper;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tmatvienko on 2/24/15.
 */
public class DeviceNotificationWrapper implements Serializable {

    @SerializedName("id")
    private final String id;

    @SerializedName("device_guid")
    private final String deviceGuid;

    @SerializedName("timestamp")
    private final Date timestamp;

    @SerializedName("notification")
    private final String notification;

    @SerializedName("parameters")
    private final JsonStringWrapper parameters;

    public DeviceNotificationWrapper(String id, String deviceGuid, Date timestamp, String notification, JsonStringWrapper parameters) {
        this.id = id;
        this.deviceGuid = deviceGuid;
        this.timestamp = timestamp;
        this.notification = notification;
        this.parameters= parameters;
    }

    public String getId() {
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

    public JsonStringWrapper getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceNotificationWrapper that = (DeviceNotificationWrapper) o;

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
        return "DeviceNotificationWrapper{" +
                "id=" + id +
                ", notification='" + notification + '\'' +
                ", deviceGuid=" + deviceGuid +
                ", timestamp=" + timestamp +
                '}';
    }
}
