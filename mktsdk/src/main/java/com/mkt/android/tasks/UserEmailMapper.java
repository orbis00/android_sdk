package com.mkt.android.tasks;

import com.mkt.android.base.ValidationException;
import com.mkt.android.helpers.Configuration;
import com.mkt.android.helpers.StringUtils;
import com.mkt.android.Mkt;
import com.mkt.android.base.Task;
import org.json.JSONException;
import org.json.JSONObject;

public class UserEmailMapper extends Task {

    private String email;

    public UserEmailMapper(){}


    public UserEmailMapper (String email) {
        this.email = email;
    }

    public UserEmailMapper setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.EMAIL_DATA_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Mkt.getOrgToken();
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            if (StringUtils.isNotEmpty(this.email)) {
                data.put("email", this.email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected boolean validate() throws ValidationException {
        if(StringUtils.isEmpty(this.email)) {
            throw new ValidationException("Email cannot be blank");
        }
        return true;
    }
}
