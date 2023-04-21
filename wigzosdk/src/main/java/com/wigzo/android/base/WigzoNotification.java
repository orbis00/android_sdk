package com.wigzo.android.base;

import org.json.JSONException;
import org.json.JSONObject;

public class WigzoNotification extends Task{
    public String uuid = null;
    public long campaignId = 0;
    public long organizationId = 0;
    public String timestamp = null;

    protected String url = "";

    WigzoNotification setCampaignId(long campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    public long getCampaignId() {
        return this.campaignId;
    }


    WigzoNotification setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public long getOrganizationId() {
        return this.organizationId;
    }

    @Override
    protected String buildUrl() {
        return this.url;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            if (this.campaignId > 0) {
                data.put("campaignId", this.campaignId);
            }
            if (this.organizationId > 0) {
                data.put("organizationId", this.organizationId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected boolean validate() throws ValidationException {
        if(this.campaignId < 1 && this.organizationId < 1) {
            throw new ValidationException("Campaign and Organization ID cannot be less than or equal to 0");
        }
        return true;
    }
}
