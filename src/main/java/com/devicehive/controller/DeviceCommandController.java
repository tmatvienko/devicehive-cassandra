package com.devicehive.controller;

import com.devicehive.domain.wrappers.DeviceCommandWrapper;
import com.devicehive.message.converter.adapter.TimestampAdapter;
import com.devicehive.service.DeviceCommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by tmatvienko on 2/13/15.
 */
@RestController
@RequestMapping("/commands")
public class DeviceCommandController {

    @Autowired
    private DeviceCommandsService commandsService;
    @Autowired
    private TimestampAdapter timestampAdapter;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<DeviceCommandWrapper> get(@RequestParam(value = "count", required=false, defaultValue = "1000") int count,
                                            @RequestParam(value = "id", required = false) String id,
                                            @RequestParam(value = "deviceGuids", required = false) String deviceGuids,
                                            @RequestParam(value = "timestamp", required = false) String timestamp) {
        final Timestamp date = timestampAdapter.parseTimestamp(timestamp);
        return commandsService.get(count, id, deviceGuids, date);
    }

    @RequestMapping(value="/count", method = RequestMethod.GET, produces = "application/json")
    public Long getCommandsCount() {
        return commandsService.getCommandsCount();
    }

    @RequestMapping(value="/{deviceGuid}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteByDeviceGuid(@PathVariable String deviceGuid) {
        commandsService.delete(deviceGuid);
    }
}
