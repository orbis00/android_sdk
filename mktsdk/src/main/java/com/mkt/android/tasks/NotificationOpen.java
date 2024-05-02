package com.mkt.android.tasks;

import com.mkt.android.Mkt;
import com.mkt.android.base.MktNotification;
import com.mkt.android.helpers.Configuration;

public class NotificationOpen extends MktNotification {

    public NotificationOpen(){}

    public NotificationOpen (long campaignId, long organizationId) {
        this.campaignId = campaignId;
        this.organizationId = organizationId;
    }

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.FCM_OPEN_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Mkt.getOrgToken();
    }
}
