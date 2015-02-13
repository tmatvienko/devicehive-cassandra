package com.devicehive.message.converter;

import com.devicehive.domain.DeviceCommand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kafka.serializer.Decoder;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import java.io.UnsupportedEncodingException;

/**
 * Created by tmatvienko on 2/13/15.
 */
public class DeviceCommandConverter implements Encoder<DeviceCommand>, Decoder<DeviceCommand> {
    private Gson gson;
    public DeviceCommandConverter(VerifiableProperties verifiableProperties) {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override
    public DeviceCommand fromBytes(byte[] bytes) {
        try {
            return gson.fromJson(new String(bytes, "UTF-8"), DeviceCommand.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] toBytes(DeviceCommand deviceCommand) {
        return gson.toJson(deviceCommand).getBytes();
    }
}
