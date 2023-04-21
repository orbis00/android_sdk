package com.wigzo.android.tasks;

import com.wigzo.android.base.ValidationException;
import com.wigzo.android.helpers.Configuration;
import com.wigzo.android.helpers.StringUtils;
import com.wigzo.android.Wigzo;
import com.wigzo.android.base.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileMapper extends Task {

    private String fullName;
    private String userName;
    private String email;
    private String organization;
    private String phone;
    private String gender;
    private String birthYear;
    private JSONObject customData;

    public UserProfileMapper() {}

    public UserProfileMapper(String fullName, String userName, String email, String organization,
                             String phone, String gender, String birthYear, JSONObject customData) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.organization = organization;
        this.phone = phone;
        this.gender = gender;
        this.birthYear = birthYear;
        this.customData = customData;
    }

    public UserProfileMapper setFullName(String fullName) { this.fullName = fullName; return this; }
    public UserProfileMapper setUserName(String userName) { this.userName = userName; return this; }
    public UserProfileMapper setEmail(String email) { this.email = email; return this; }
    public UserProfileMapper setOrganization(String organization) { this.organization = organization; return this; }
    public UserProfileMapper setPhone(String phone) { this.phone = phone; return this; }
    public UserProfileMapper setGender(String gender) { this.gender = gender; return this; }
    public UserProfileMapper setBirthYear(String birthYear) { this.birthYear = birthYear; return this; }
    public UserProfileMapper setCustomData(JSONObject customData) { this.customData = customData; return this; }

    public String getFullName() { return this.fullName; }
    public String getUserName() { return this.userName; }
    public String getEmail() { return this.email; }
    public String getOrganization() { return this.organization; }
    public String getPhone() { return this.phone; }
    public String getGender() { return this.gender; }
    public String getBirthYear() { return this.birthYear; }
    public JSONObject getCustomData() { return this.customData; }

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.USER_PROFILE_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Wigzo.getOrgToken();
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            if(StringUtils.isNotEmpty(this.fullName)) {
                data.put("fullName", this.fullName);
            }
            if(StringUtils.isNotEmpty(this.userName)) {
                data.put("userName", this.userName);
            }
            if(StringUtils.isNotEmpty(this.email)) {
                data.put("email", this.email);
            }
            if(StringUtils.isNotEmpty(this.organization)) {
                data.put("organization", this.organization);
            }
            if(StringUtils.isNotEmpty(this.phone)) {
                data.put("phone", this.phone);
            }
            if(StringUtils.isNotEmpty(this.gender)) {
                data.put("gender", this.gender);
            }
            if(StringUtils.isNotEmpty(this.birthYear)) {
                data.put("birthYear", this.birthYear);
            }
            if(this.customData != null && this.customData.length() > 0) {
                data.put("customData", this.customData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected boolean validate() throws ValidationException {
        if(StringUtils.allEmpty(this.fullName, this.userName, this.email, this.organization, this.phone, this.gender, this.birthYear) &&
                !(this.customData != null && this.customData.length() > 0)) {
            throw new ValidationException("User profile should contain at least one field: " +
                    "fullName (String), userName (String), email (String), organization (String), " +
                    "phone (String), gender (String), birthYear (String) or customData(JSONObject)");
        }
        return true;
    }
}
