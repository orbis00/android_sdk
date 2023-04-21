package com.wigzo.android.tasks;

import com.wigzo.android.Wigzo;
import com.wigzo.android.base.Task;
import com.wigzo.android.base.ValidationException;
import com.wigzo.android.base.WigzoNotification;
import com.wigzo.android.helpers.Configuration;
import com.wigzo.android.helpers.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationOpen extends WigzoNotification {

    public NotificationOpen(){}

    public NotificationOpen (long campaignId, long organizationId) {
        this.campaignId = campaignId;
        this.organizationId = organizationId;
    }

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.FCM_OPEN_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Wigzo.getOrgToken();
    }
}
