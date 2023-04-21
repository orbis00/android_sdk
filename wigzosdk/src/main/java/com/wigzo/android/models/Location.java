package com.wigzo.android.models;

import com.wigzo.android.base.ValidationException;
import com.wigzo.android.helpers.StringUtils;
import com.wigzo.android.base.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Location extends Model {
    private String countryCode;
    private String stateName;
    private String stateCode;
    private String city;

    public Location () {
        JSONObject locationJson = com.wigzo.android.helpers.Device.fetchDeviceLocation();

        this.countryCode = locationJson.optString("country", "");
        this.stateName = locationJson.optString("stateName", "");
        this.stateCode = locationJson.optString("stateCode", "");
        this.city = locationJson.optString("city", "");
    }

    public Location setCountryCode(String countryCode) { this.countryCode = countryCode; return this;}
    public Location setStateName(String stateName) { this.stateName = stateName; return this;}
    public Location setStateCode(String stateCode) { this.stateCode = stateCode; return this;}
    public Location setCity(String city) { this.city = city; return this;}

    public String getCountryCode() { return this.countryCode; }
    public String getStateName() { return this.stateName; }
    public String getStateCode() { return this.stateCode; }
    public String getCity() { return this.city; }

    @Override
    public JSONObject toJson() {

        JSONObject locationData = new JSONObject();
        try {
            if(StringUtils.isNotEmpty(this.countryCode)){
                locationData.put("countryCode", this.countryCode);
            }
            if(StringUtils.isNotEmpty(this.stateName)){
                locationData.put("stateName", this.stateName);
            }
            if(StringUtils.isNotEmpty(this.stateCode)){
                locationData.put("stateCode", this.stateCode);
            }
            if(StringUtils.isNotEmpty(this.city)){
                locationData.put("city", this.city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationData;
    }

    @Override
    public boolean isValid() throws ValidationException {
        if(this !=null &&
                StringUtils.allEmpty(this.countryCode, this.stateName, this.stateCode, this.city)) {
            throw new ValidationException("Location should contain at least one field: \n" +
                    "countryCode (String), stateName (String), stateCode (String), city (String)");
        }
        return true;
    }
}