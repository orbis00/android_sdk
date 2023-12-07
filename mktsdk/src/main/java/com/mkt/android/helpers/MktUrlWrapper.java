package com.mkt.android.helpers;

/**
 * Created by mkt on 06/12/23.
 */

public class MktUrlWrapper {
    public static String addQueryParam(String url, String queryParamKey, String queryParamVal) {

        if (StringUtils.isNotEmpty(url) && !url.contains(queryParamKey)) {
            if (url.contains("?")) {
                url = url + "&" + queryParamKey + "=" + queryParamVal;
            } else {
                url = url + "?" + queryParamKey + "=" + queryParamVal;
            }
            return url;
        }
        return null;
    }
}
