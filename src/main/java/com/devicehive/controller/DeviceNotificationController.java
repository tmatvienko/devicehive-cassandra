package com.devicehive.controller;

import com.devicehive.domain.DeviceNotification;
import com.devicehive.service.DeviceNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by tatyana on 2/11/15.
 */
@RestController
@RequestMapping("/notifications")
public class DeviceNotificationController {

    @Autowired
    private DeviceNotificationService notificationService;

    @RequestMapping(value="/all",method = RequestMethod.GET, produces = "application/json")
    public List<DeviceNotification> getLast(@RequestParam(value = "count", required=false, defaultValue = "1000") int count) {
        return notificationService.getLast(count);
    }

    @RequestMapping(value="/{deviceGuid}",method = RequestMethod.GET, produces = "application/json")
    public List<DeviceNotification> getByDevice(@PathVariable String deviceGuid, @RequestParam(value = "count", required=false, defaultValue = "1000") int count) {
        return notificationService.getByDevice(deviceGuid, count);
    }

    @RequestMapping(value="/count",method = RequestMethod.GET, produces = "application/json")
    public Long getNotificationsCount() {
        return notificationService.getNotificationsCount();
    }
}
