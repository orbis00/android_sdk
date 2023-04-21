package com.wigzo.android.tasks;

import com.wigzo.android.base.ValidationException;
import com.wigzo.android.helpers.Configuration;
import com.wigzo.android.helpers.StringUtils;
import com.wigzo.android.Wigzo;
import com.wigzo.android.base.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class FCMMapper extends Task {

    private String registrationId;

    public FCMMapper(){}
    public FCMMapper (String registrationId) {
        this.registrationId = registrationId;
    }

    FCMMapper setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
        return this;
    }

    public String getRegistrationId() {
        return this.registrationId;
    }

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.FCM_DEVICE_MAPPING_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Wigzo.getOrgToken();
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            if (StringUtils.isNotEmpty(this.registrationId)) {
                data.put("registrationId", this.registrationId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected boolean validate() throws ValidationException {
        if(StringUtils.isEmpty(this.registrationId)) {
            throw new ValidationException("Firebase registration ID cannot be blank");
        }
        return true;
    }
}
