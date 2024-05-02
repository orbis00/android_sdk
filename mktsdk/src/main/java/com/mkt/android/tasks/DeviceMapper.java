package com.mkt.android.tasks;

import com.mkt.android.helpers.Configuration;
import com.mkt.android.Mkt;
import com.mkt.android.base.Task;
import com.mkt.android.models.DeviceInfo;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceMapper extends Task {

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.INITIAL_DATA_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Mkt.getOrgToken();
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            data.put("deviceInfo", (new DeviceInfo()).toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
//        return new JSONObject();
    }

    @Override
    protected boolean validate() {
        return true;
    }
}
