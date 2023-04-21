package com.wigzo.android.models;

import com.wigzo.android.base.ValidationException;

import java.util.ArrayList;

public class EventDataList extends ArrayList<EventData> {

    public boolean hasValidItems() throws ValidationException {
        for (EventData eventData : this) {
            if(!eventData.isValid()) {
                throw new ValidationException();
            }
        }
        return true;
    }
}
