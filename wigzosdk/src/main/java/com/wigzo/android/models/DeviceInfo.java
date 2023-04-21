package com.wigzo.android.models;

import com.wigzo.android.base.ValidationException;
import com.wigzo.android.helpers.StringUtils;
import com.wigzo.android.base.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceInfo extends Model {


    private String device;
    private String os;
    private String osVersion;
    private String ipAddress;
    private String appVersion;
    private Location location;

    public DeviceInfo() {
        this.device = android.os.Build.MODEL;
        this.os = "Android";
        this.osVersion = android.os.Build.VERSION.RELEASE;;
        this.ipAddress = com.wigzo.android.helpers.Device.fetchIpAddress();
        this.appVersion = com.wigzo.android.helpers.Device.fetchAppVersion();
        this.location = new Location();
    }

    public DeviceInfo setDevice(String device) { this.device = device; return this; }
    public DeviceInfo setOs(String os) { this.os = os; return this; }
    public DeviceInfo setOsVersion(String osVersion) { this.osVersion = osVersion; return this; }
    public DeviceInfo setIpAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
    public DeviceInfo setAppVersion(String appVersion) { this.appVersion = appVersion; return this; }
    public DeviceInfo setLocation(Location location) { this.location = location; return this; }

    public String getDevice() { return this.device; }
    public String getOs() { return this.os; }
    public String getOsVersion() { return this.osVersion; }
    public String getIpAddress() { return this.ipAddress; }
    public String getAppVersion() { return this.appVersion; }
    public Location getLocation() { return this.location; }

    @Override
    public JSONObject toJson() {

        JSONObject deviceInfo = new JSONObject();
        try {
            if(StringUtils.isNotEmpty(this.device)){
                deviceInfo.put("device", this.device);
            }
            if(StringUtils.isNotEmpty(this.os)){
                deviceInfo.put("os", this.os);
            }
            if(StringUtils.isNotEmpty(this.osVersion)){
                deviceInfo.put("osVersion", this.osVersion);
            }
            if(StringUtils.isNotEmpty(this.ipAddress)){
                deviceInfo.put("ipAddress", this.ipAddress);
            }
            if(StringUtils.isNotEmpty(this.appVersion)){
                deviceInfo.put("appVersion", this.appVersion);
            }

            JSONObject locationJson = this.location.toJson();
            if(locationJson.length() > 0){
                deviceInfo.put("location", locationJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }

    @Override
    public boolean isValid() throws ValidationException {
        if (this != null &&
                StringUtils.allEmpty(this.device, this.os, this.osVersion, this.ipAddress, this.appVersion) &&
                this.location.isValid()) {
            throw new ValidationException("Device info should contain at least: \n" +
                    "device (String), os (String), osVersion (String), ipAddress (String), appVersion (String), or location (Location)");
        }
        return true;
    }
}
