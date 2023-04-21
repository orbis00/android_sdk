package com.wigzo.android.tasks;

import com.wigzo.android.Wigzo;
import com.wigzo.android.base.Task;
import com.wigzo.android.base.ValidationException;
import com.wigzo.android.base.WigzoNotification;
import com.wigzo.android.helpers.Configuration;
import com.wigzo.android.helpers.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationRecieved extends WigzoNotification {

    public NotificationRecieved(){}

    public NotificationRecieved (long campaignId, long organizationId) {
        this.campaignId = campaignId;
        this.organizationId = organizationId;
    }

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.FCM_READ_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Wigzo.getOrgToken();
    }
}
