package com.mkt.android.InAppMessage.MktLayoutAttributes;

import android.content.Context;
import com.mkt.android.R;

/**
 * Created by Rihan.
 */

public class MktLayoutProperties {

    public static int getMktLayoutId(String layoutId) {
        if (layoutId.equals("001")) {
            return R.layout.mkt_dialog_template_1;
        } else if (layoutId.equals("002")) {
            return R.layout.mkt_dialog_template_2;
        } else if (layoutId.equals("003")) {
            return R.layout.mkt_dialog_template_3;
        } else if (layoutId.equals("004")) {
            return R.layout.mkt_dialog_template_4;
        } else if (layoutId.equals("005")) {
            return R.layout.mkt_dialog_template_5;
        } else if (layoutId.equals("006")) {
            return R.layout.mkt_dialog_template_6;
        } else if (layoutId.equals("007")) {
            return R.layout.mkt_dialog_template_7;
        }  else if (layoutId.equals("008")) {
            return R.layout.mkt_dialog_template_8;
        }
        else {
            return R.layout.mkt_dialog_template_1;
        }
    }

    public static MktLayoutProperties getMktLayoutProperties(int layoutId, Context context) {
        if (layoutId == R.layout.mkt_dialog_template_1) {
            return MktLayout1.getInstance();
        } else if (layoutId == R.layout.mkt_dialog_template_2) {
            return MktLayout2.getInstance();
        } else if (layoutId == R.layout.mkt_dialog_template_3) {
            return MktLayout3.getInstance();
        } else if (layoutId == R.layout.mkt_dialog_template_4) {
            return MktLayout4.getInstance();
        } else if (layoutId == R.layout.mkt_dialog_template_5) {
            return MktLayout5.getInstance();
        } else if (layoutId == R.layout.mkt_dialog_template_6) {
            return MktLayout6.getInstance();
        } else if (layoutId == R.layout.mkt_dialog_template_7) {
            return MktLayout7.getInstance();
        } else {
            return MktLayout1.getInstance();
        }
    }

    public boolean hasImage = false;
}
