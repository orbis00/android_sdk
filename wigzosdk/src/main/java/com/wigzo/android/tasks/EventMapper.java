package com.wigzo.android.tasks;

import com.wigzo.android.base.ValidationException;
import com.wigzo.android.helpers.Configuration;
import com.wigzo.android.Wigzo;
import com.wigzo.android.base.Task;
import com.wigzo.android.models.DeviceInfo;
import com.wigzo.android.models.EventData;
import com.wigzo.android.models.EventDataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EventMapper extends Task {

    private EventDataList eventDataList = new EventDataList();
    private DeviceInfo deviceInfo;

    public EventMapper() {
        this.deviceInfo = new DeviceInfo();
    }
    public EventMapper(EventDataList eventDataList, DeviceInfo deviceInfo) {
        this.eventDataList = eventDataList;
        this.deviceInfo = deviceInfo;
    }

    private EventMapper setEventDataList(EventData eventData) { this.eventDataList.add(eventData); return this; }
    public EventMapper addEventData(EventData eventData) { this.setEventDataList(eventData); return this; }
    public EventMapper setDeviceInfo(DeviceInfo deviceInfo) { this.deviceInfo = deviceInfo; return this; }

    public List<EventData> getEventDataList() { return this.eventDataList; }
    public EventData getEventData(int index) { return this.eventDataList.get(index); }
    public DeviceInfo getDeviceInfo() { return this.deviceInfo; }

    @Override
    protected String buildUrl() {
        return Configuration.BASE_URL.value + Configuration.EVENT_DATA_URL.value + "?"
                + Configuration.SITE_ID.value + "=" + Wigzo.getOrgToken();
    }

    @Override
    public JSONObject toJson() {
        JSONObject eventJson = new JSONObject();

        try {
            JSONObject deviceInfoJson = this.deviceInfo != null ? this.deviceInfo.toJson() : (new DeviceInfo()).toJson();
            eventJson.put("deviceInfo", deviceInfoJson);

            if (this.eventDataList.size() > 0) {
                JSONArray eventDataJsonArray = new JSONArray();
                for (EventData eventData : this.eventDataList) {
                    JSONObject eventDataJson = eventData.toJson();
                    if (eventDataJson.length() > 0) {
                        eventDataJsonArray.put(eventDataJson);
                    }
                }
                eventJson.put("eventData", eventDataJsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventJson;
    }

    @Override
    protected boolean validate() throws ValidationException {
        if (!this.eventDataList.hasValidItems()) {
            throw new ValidationException("Provide valid event data");
        }
        return true;
    }
}
