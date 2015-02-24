package com.devicehive.domain;

import com.devicehive.domain.wrappers.DeviceCommandWrapper;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tmatvienko on 2/13/15.
 */
@Table(value = "device_command")
public class DeviceCommand implements Serializable {

    @PrimaryKeyColumn(name = "id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private String id;

    @PrimaryKeyColumn(name = "device_guid", ordinal = 0, type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    private String deviceGuid;

    @PrimaryKeyColumn(name = "timestamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date timestamp;

    private String command;

    private String parameters;

    private String userId;

    private Integer lifetime;

    private Integer flags;

    private String status;

    private String result;

    public DeviceCommand() {
    }

    public DeviceCommand(String id, String deviceGuid, Date timestamp, String command, String parameters, String userId, Integer lifetime, Integer flags, String status, String result) {
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

    public DeviceCommand(DeviceCommandWrapper wrapper) {
        if (wrapper.getId() != null) {
            this.id = wrapper.getId();
        }
        if (wrapper.getDeviceGuid() != null) {
            this.deviceGuid = wrapper.getDeviceGuid();
        }
        if (wrapper.getTimestamp() != null) {
            this.timestamp = wrapper.getTimestamp();
        }
        if (wrapper.getCommand() != null) {
            this.command = wrapper.getCommand();
        }
        if (wrapper.getParameters() != null) {
            this.parameters = wrapper.getParameters().getJsonString();
        }
        if (wrapper.getUserId() != null) {
            this.userId = wrapper.getUserId();
        }
        if (wrapper.getLifetime() != null) {
            this.lifetime = wrapper.getLifetime();
        }
        if (wrapper.getFlags() != null) {
            this.flags = wrapper.getFlags();
        }
        if (wrapper.getStatus() != null) {
            this.status = wrapper.getStatus();
        }
        if (wrapper.getResult() != null) {
            this.result = wrapper.getResult().getJsonString();
        }
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
}
