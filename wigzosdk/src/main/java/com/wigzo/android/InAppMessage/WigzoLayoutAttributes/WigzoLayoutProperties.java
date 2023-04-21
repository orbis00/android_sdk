package com.wigzo.android.InAppMessage.WigzoLayoutAttributes;

import android.content.Context;

import com.wigzo.android.R;

/**
 * Created by Rihan.
 */

public class WigzoLayoutProperties {

    public static int getWigzoLayoutId(String layoutId) {
        if (layoutId.equals("001")) {
            return R.layout.wigzo_dialog_template_1;
        } else if (layoutId.equals("002")) {
            return R.layout.wigzo_dialog_template_2;
        } else if (layoutId.equals("003")) {
            return R.layout.wigzo_dialog_template_3;
        } else if (layoutId.equals("004")) {
            return R.layout.wigzo_dialog_template_4;
        } else if (layoutId.equals("005")) {
            return R.layout.wigzo_dialog_template_5;
        } else if (layoutId.equals("006")) {
            return R.layout.wigzo_dialog_template_6;
        } else if (layoutId.equals("007")) {
            return R.layout.wigzo_dialog_template_7;
        } else {
            return R.layout.wigzo_dialog_template_1;
        }
    }

    public static WigzoLayoutProperties getWigzoLayoutProperties(int layoutId, Context context) {
        if (layoutId == R.layout.wigzo_dialog_template_1) {
            return WigzoLayout1.getInstance();
        } else if (layoutId == R.layout.wigzo_dialog_template_2) {
            return WigzoLayout2.getInstance();
        } else if (layoutId == R.layout.wigzo_dialog_template_3) {
            return WigzoLayout3.getInstance();
        } else if (layoutId == R.layout.wigzo_dialog_template_4) {
            return WigzoLayout4.getInstance();
        } else if (layoutId == R.layout.wigzo_dialog_template_5) {
            return WigzoLayout5.getInstance();
        } else if (layoutId == R.layout.wigzo_dialog_template_6) {
            return WigzoLayout6.getInstance();
        } else if (layoutId == R.layout.wigzo_dialog_template_7) {
            return WigzoLayout7.getInstance();
        } else {
            return WigzoLayout1.getInstance();
        }
    }

    public boolean hasImage = false;
}
