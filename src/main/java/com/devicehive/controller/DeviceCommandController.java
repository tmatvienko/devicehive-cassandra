package com.devicehive.controller;

import com.devicehive.domain.DeviceCommand;
import com.devicehive.service.DeviceCommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tmatvienko on 2/13/15.
 */
@RestController
@RequestMapping("/commands")
public class DeviceCommandController {

    @Autowired
    private DeviceCommandsService commandsService;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @RequestMapping(value="/all", method = RequestMethod.GET, produces = "application/json")
    public List<DeviceCommand> getLast(@RequestParam(value = "count", required=false, defaultValue = "1000") int count) {
        return commandsService.getLast(count);
    }

    @RequestMapping(value="/{deviceGuid}", method = RequestMethod.GET, produces = "application/json")
    public List<DeviceCommand> getByDevice(@PathVariable String deviceGuid, @RequestParam(value = "count", required=false, defaultValue = "1000") int count) {
        return commandsService.getByDevice(deviceGuid, count);
    }

    @RequestMapping(value="/count", method = RequestMethod.GET, produces = "application/json")
    public Long getCommandsCount() {
        return commandsService.getCommandsCount();
    }

    @RequestMapping(value="/all", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteAllCommands() {
        commandsService.deleteAllCommands();
    }

    @RequestMapping(value="/{deviceGuid}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteByDeviceGuid(@PathVariable String deviceGuid) {
        commandsService.deleteByDeviceGuid(deviceGuid);
    }

    @RequestMapping(value="/new",method = RequestMethod.GET, produces = "application/json")
    public List<DeviceCommand> getNewNotifications(@RequestParam(value = "timestamp", required=true) String timestamp) throws ParseException {
        final Date date = FORMAT.parse(timestamp);
        return commandsService.getNewCommands(date);
    }
}
