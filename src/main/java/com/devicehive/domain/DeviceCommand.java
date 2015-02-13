package com.devicehive.domain;

import com.google.gson.annotations.SerializedName;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tmatvienko on 2/13/15.
 */
@Table(value = "device_command")
public class DeviceCommand implements Serializable {

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @SerializedName("id")
    private final UUID id;

    @PrimaryKeyColumn(name = "device_guid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @SerializedName("device_guid")
    private final String deviceGuid;

    @Column
    @SerializedName("timestamp")
    private final Date timestamp;

    @Column
    @SerializedName("command")
    private final String command;

    @Column
    @SerializedName("parameters")
    private final String parameters;

    @Column
    @SerializedName("userId")
    private final String userId;

    @Column
    @SerializedName("lifetime")
    private final Integer lifetime;

    @Column
    @SerializedName("flags")
    private final Integer flags;

    @Column
    @SerializedName("status")
    private final String status;

    @Column
    @SerializedName("result")
    private final String result;

    @Column
    @SerializedName("originSessionId")
    private String originSessionId;

    public DeviceCommand(UUID id, String deviceGuid, Date timestamp, String command, String parameters, String userId, Integer lifetime, Integer flags, String status, String result, String originSessionId) {
        this.id = id;
        this.deviceGuid = deviceGuid;
        this.timestamp = timestamp;
        this.command = command;
        this.parameters = parameters;
        this.userId = userId;
        this.lifetime = lifetime;
        this.flags = flags;
        this.status = status;
        this.result = result;
        this.originSessionId = originSessionId;
    }

    public UUID getId() {
        return id;
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getCommand() {
        return command;
    }

    public String getParameters() {
        return parameters;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getLifetime() {
        return lifetime;
    }

    public Integer getFlags() {
        return flags;
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public String getOriginSessionId() {
        return originSessionId;
    }

    public void setOriginSessionId(String originSessionId) {
        this.originSessionId = originSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceCommand that = (DeviceCommand) o;

        if (command != null ? !command.equals(that.command) : that.command != null) return false;
        if (deviceGuid != null ? !deviceGuid.equals(that.deviceGuid) : that.deviceGuid != null) return false;
        if (flags != null ? !flags.equals(that.flags) : that.flags != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lifetime != null ? !lifetime.equals(that.lifetime) : that.lifetime != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (originSessionId != null ? !originSessionId.equals(that.originSessionId) : that.originSessionId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = id != null ? id.hashCode() : 0;
        result1 = 31 * result1 + (deviceGuid != null ? deviceGuid.hashCode() : 0);
        result1 = 31 * result1 + (timestamp != null ? timestamp.hashCode() : 0);
        result1 = 31 * result1 + (command != null ? command.hashCode() : 0);
        result1 = 31 * result1 + (parameters != null ? parameters.hashCode() : 0);
        result1 = 31 * result1 + (userId != null ? userId.hashCode() : 0);
        result1 = 31 * result1 + (lifetime != null ? lifetime.hashCode() : 0);
        result1 = 31 * result1 + (flags != null ? flags.hashCode() : 0);
        result1 = 31 * result1 + (status != null ? status.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (originSessionId != null ? originSessionId.hashCode() : 0);
        return result1;
    }
}
