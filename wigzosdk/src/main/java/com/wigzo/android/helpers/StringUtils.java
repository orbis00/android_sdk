package com.wigzo.android.helpers;

public class StringUtils {
    public static boolean isEmpty(String string) {
        if (null == string) {
            return true;
        } else {
            string = string.trim();
            return string.equals("");
        }
    }

    public static boolean isEmpty(String ... string) {
        if (null == string) {
            return true;
        } else {
            for (String str : string) {
                if (isEmpty(str)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean allEmpty(String ... string) {
        if (null == string) {
            return true;
        } else {
            for (String str : string) {
                if (!isEmpty(str)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isNoneEmpty(String ... string) {
        if (null == string) {
            return false;
        } else {
            for (String str : string) {
                if (isEmpty(str)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
}
