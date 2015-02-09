package com.devicehive.messages.converter;

import com.devicehive.domain.DeviceNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kafka.serializer.Decoder;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import java.io.UnsupportedEncodingException;

/**
 * Created by tmatvienko on 2/5/15.
 */
public class DeviceNotificationConverter implements Encoder<DeviceNotification>, Decoder<DeviceNotification> {
    private Gson gson;
    public DeviceNotificationConverter(VerifiableProperties verifiableProperties) {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override
    public DeviceNotification fromBytes(byte[] bytes) {
        try {
            return gson.fromJson(new String(bytes, "UTF-8"), DeviceNotification.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] toBytes(DeviceNotification deviceNotification) {
        return gson.toJson(deviceNotification).getBytes();
    }
}
