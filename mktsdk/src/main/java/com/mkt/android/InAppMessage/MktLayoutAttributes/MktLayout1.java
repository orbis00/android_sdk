package com.mkt.android.InAppMessage.MktLayoutAttributes;

/**
 * Created by Rihan.
 */

public class MktLayout1 extends MktLayoutProperties {
    private MktLayout1() {
        hasImage = true;
    }

    public static MktLayout1 getInstance() {
        return new MktLayout1();
    }
}
