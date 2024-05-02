package com.mkt.android.helpers;
/**
 * Created by Rihan Husain
 */
public enum Configuration {

//    BASE_URL("baseUrl", getSharedStorage().getString("BASE_URL", "https://hulk.mktpush.com")),
//    BASE_URL("baseUrl", BuildConfig.DEBUG ? "http://192.168.135.125:9000" : "https://app.mkt.com"),
    BASE_URL("baseUrl","https://app.mkt.com"),

    APP_KEY("appKey","APP_KEY"),
    DEFAULT_APP_VERSION("defaultAppVersion","1.0"),
    DEFAULT_SDK_VERSION("defaultSdkVersion","1.0"),
    DEVICE_ID_KEY("deviceIdKey","DEVICE_ID_KEY"),
    DEVICE_ID_TAG("deviceIdTag","DeviceInfo"),
    DEVICE_LOCATION_KEY("deviceLocationKey","DEVICE_LOCATION_KEY"),
    EMAIL_DATA_URL("emailDataUrl","/androidsdk/mapemail"),
    EMAIL_KEY("emailKey","EMAIL_KEY"),
    EMAIL_SYNC_KEY("emailSyncKey","EMAIL_SYNC_KEY"),
    EVENTS_KEY("eventKey","MKT_EVENTS"),
    EVENT_DATA_URL("eventDataUrl","/androidsdk/geteventdata"),
    FCM_DEVICE_MAPPED("fcmDeviceMapped", "FCM_DEVICE_MAPPED"),
    FCM_DEVICE_MAPPING_URL("fcmDeviceMappingUrl", "/androidsdk/map-fcm"),
    FCM_OPEN_KEY("fcmOpenKey", "FCM_OPEN_KEY"),
    FCM_OPEN_URL("fcmOpenUrl", "/rest/v1/push/android/track/open-multiple"),
    FCM_READ_KEY("fcmReadKey", "FCM_READ_KEY"),
    FCM_READ_URL("fcmReadUrl", "/rest/v1/push/android/track/read-multiple"),
    // FCM_REGISTRATION_URL("fcmRegistrationUrl", "/rest/v1/push/android/register-subscription"),
    FCM_TOKEN("fcmToken", "FCM_TOKEN"),
    USER_LOCATION_URL("userLocationUrl", "/report/track/web"),
    INITIAL_DATA_URL("initialDataUrl","/androidsdk/getinitialdata"),
    ORG_TOKEN_KEY("orgToken","ORG_TOKEN_KEY"),
    PREV_TIME_SPENT_KEY("prevTimeSpent","PREV_TIME_SPENT_KEY"),
    SENT_FCM_TOKEN_TO_SERVER("sentTokenToServer", "SENT_FCM_TOKEN_TO_SERVER"),
    SESSION_DATA_URL("sessionDataUrl","/androidsdk/getsessiondata"),
    SITE_ID("siteid", "_siteid"),
    STORAGE_KEY("storageKey","MKT_SHARED_STORAGE"),
    TIME_DELAY("timeDelay","45"),
    TIME_SPENT_KEY("timeSpentKey","TIME_SPENT_KEY"),
    USER_PROFILE_DATA_KEY("userProfileDataKey","USER_PROFILE_DATA_KEY"),
    USER_PROFILE_PICTURE_KEY("userProfilePictureKey","USER_PROFILE_PICTURE_KEY"),
    USER_PROFILE_SYNC_KEY("userProfileSyncKey","USER_PROFILE_SYNC_KEY"),
    USER_PROFILE_URL("userProfileUrl","/androidsdk/getuserprofiledata"),
    MKT_FCM_LISTENER_SERVICE_TAG("mktFCMListenerServiceTag","MktFcmListenerService"),
    MKT_INIT_DATA_SYNC_FLAG_KEY("mktInitiDataSyncFlag","MKT_INIT_DATA_SYNC_FLAG_KEY"),
    MKT_REG_INTENT_SERVICE_TAG("mktRegIntentServiceTag","RegIntentService"),
    MKT_SDK_TAG("mktSdkTag","Mkt"),
    APP_RUNNING_STATUS("appRunningSatus", "AppRunningStatus");

    public String key;
    public String value;

    Configuration(String key, String value)
    {
        this.key   = key;
        this.value = value;
    }


}
