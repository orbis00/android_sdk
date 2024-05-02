package com.mkt.android.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class provides a persistence layer for the sdk.
 *
 *  @author Rihan Husain
 */
public class MktSharedStorage {

    public SharedPreferences getSharedStorage() {
        return sharedStorage;
    }

    private static SharedPreferences sharedStorage = null;

    /**
     * Constructs a MktStore object.
     * @param context used to retrieve storage meta data, must not be null.
     * @throws IllegalArgumentException if context is null
     */
    public MktSharedStorage(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("must provide valid context");
        }
        sharedStorage = context.getSharedPreferences(Configuration.STORAGE_KEY.value, Context.MODE_PRIVATE);
    }
}
