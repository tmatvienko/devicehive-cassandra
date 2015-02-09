package com.devicehive.controller;

import com.devicehive.domain.AppInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tmatvienko on 2/5/15.
 */
@RestController
@RequestMapping("/info")
public class AppInfoController {

    @RequestMapping(value = "/version", method = RequestMethod.GET, headers = "Accept=application/json")
    public AppInfo getAppInfo() {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppVersion("1.0.0");
        return appInfo;
    }
}