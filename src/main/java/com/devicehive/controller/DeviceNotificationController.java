package com.devicehive.controller;

import com.devicehive.domain.DeviceNotification;
import com.devicehive.service.DeviceNotificationService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by tatyana on 2/11/15.
 */
@RestController
@RequestMapping("/notifications")
public class DeviceNotificationController {

    @Autowired
    private DeviceNotificationService notificationService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZoneUTC();

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<DeviceNotification> getLast(@RequestParam(value = "count", required=false, defaultValue = "1000") int count,
                                            @RequestParam(value = "deviceGuids", required = false) String deviceGuids,
                                            @RequestParam(value = "timestamp", required = false) String timestamp) {
        final Date date = timestamp != null ? new Date(FORMATTER.parseMillis(timestamp)) : null;
        return notificationService.get(count, deviceGuids, date);
    }

    @RequestMapping(value="/{deviceGuid}",method = RequestMethod.GET, produces = "application/json")
    public List<DeviceNotification> getByDevice(@PathVariable String deviceGuid, @RequestParam(value = "count", required=false, defaultValue = "1000") int count) {
        return notificationService.getByDevice(deviceGuid, count);
    }

    @RequestMapping(value="/count",method = RequestMethod.GET, produces = "application/json")
    public Long getNotificationsCount() {
        return notificationService.getNotificationsCount();
    }

    @RequestMapping(value="/all", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteAllNotifications() {
        notificationService.deleteAllNotifications();
    }

    @RequestMapping(value="/{deviceGuid}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteByDeviceGuid(@PathVariable String deviceGuid) {
        notificationService.deleteByDeviceGuid(deviceGuid);
    }

    @RequestMapping(value="/new",method = RequestMethod.GET, produces = "application/json")
    public List<DeviceNotification> getNewNotifications(@RequestParam(value = "timestamp", required=true) Date timestamp) {
        return notificationService.get(null, null, timestamp);
    }
}
