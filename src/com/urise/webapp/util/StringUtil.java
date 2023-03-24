package com.urise.webapp.util;

public class StringUtil {
    public static boolean checkEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
