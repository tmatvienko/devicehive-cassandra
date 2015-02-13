package com.devicehive.controller;

import com.devicehive.domain.AppInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tmatvienko on 2/5/15.
 */
@RestController
@RequestMapping("/info")
public class AppInfoController {
    private static final AppInfo APP_INFO = new AppInfo("1.0.0",
            new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(new Date()));

    @RequestMapping(value = "/version", method = RequestMethod.GET, headers = "Accept=application/json")
    public AppInfo getAppInfo() {
        return APP_INFO;
    }
}