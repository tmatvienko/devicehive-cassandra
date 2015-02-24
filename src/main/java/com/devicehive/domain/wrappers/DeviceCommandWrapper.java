package com.devicehive.domain.wrappers;

import com.devicehive.domain.JsonStringWrapper;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tmatvienko on 2/24/15.
 */
public class DeviceCommandWrapper implements Serializable {

    @SerializedName("id")
    private final String id;

    @SerializedName("device_guid")
    private final String deviceGuid;

    @SerializedName("timestamp")
    private final Date timestamp;

    @SerializedName("command")
    private final String command;

    @SerializedName("parameters")
    private final JsonStringWrapper parameters;

    @SerializedName("userId")
    private final String userId;

    @SerializedName("lifetime")
    private final Integer lifetime;

    @SerializedName("flags")
    private final Integer flags;

    @SerializedName("status")
    private final String status;

    @SerializedName("result")
    private final JsonStringWrapper result;

    public DeviceCommandWrapper(String id, String deviceGuid, Date timestamp, String command, JsonStringWrapper parameters, String userId, Integer lifetime, Integer flags, String status, JsonStringWrapper result) {
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
    }

    public String getId() {
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

    public JsonStringWrapper getParameters() {
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

    public JsonStringWrapper getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceCommandWrapper that = (DeviceCommandWrapper) o;

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
        return result1;
    }

    @Override
    public String toString() {
        return "DeviceCommandWrapper{" +
                "id='" + id + '\'' +
                ", deviceGuid='" + deviceGuid + '\'' +
                ", timestamp=" + timestamp +
                ", command='" + command + '\'' +
                ", parameters=" + parameters +
                ", userId='" + userId + '\'' +
                ", lifetime=" + lifetime +
                ", flags=" + flags +
                ", status='" + status + '\'' +
                ", result=" + result +
                '}';
    }
}
