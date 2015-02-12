package com.devicehive.domain;

import java.util.Date;

/**
 * Created by tmatvienko on 2/5/15.
 */
public class AppInfo {
    private String appVersion;
    private Date serverDate;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Date getServerDate() {
        return serverDate;
    }

    public void setServerDate(Date serverDate) {
        this.serverDate = serverDate;
    }
}
