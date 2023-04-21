package com.wigzo.android.models;

import com.wigzo.android.base.ValidationException;
import com.wigzo.android.helpers.StringUtils;
import com.wigzo.android.base.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class EventData extends Model {

    private String eventName;
    private String eventValue;
    private EventMetaData metadata;

    public EventData() {}
    public EventData(String eventName, String eventValue, EventMetaData metadata) {
        this.eventName = eventName;
        this.eventValue = eventValue;
        this.metadata = metadata;
    }

    public EventData setEventName(String eventName) { this.eventName = eventName; return this; }
    public EventData setEventValue(String eventValue) { this.eventValue = eventValue; return this; }
    public EventData setMetadata(EventMetaData metadata) { this.metadata = metadata; return this; }

    public String getEventName() { return this.eventName; }
    public String getEventValue() { return this.eventValue; }
    public EventMetaData getMetadata() { return this.metadata; }

    @Override
    public JSONObject toJson() {
        JSONObject eventData = new JSONObject();
        try {
            if (StringUtils.isNotEmpty(this.eventName)){
                eventData.put("eventName", this.eventName);
            }
            if (StringUtils.isNotEmpty(this.eventValue)){
                eventData.put("eventValue", this.eventValue);
            }
            JSONObject metadataJson = this.metadata != null ? this.metadata.toJson() : new JSONObject();
            if (metadataJson.length() > 0){
                eventData.put("metadata", metadataJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventData;
    }

    @Override
    public boolean isValid() throws ValidationException {
        if (StringUtils.isEmpty(this.eventName, this.eventValue)) {
            throw new ValidationException("Event name and Event Value both are required to send an event.");
        }
        return true;
    }
}
