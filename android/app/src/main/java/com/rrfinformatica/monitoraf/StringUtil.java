package com.rrfinformatica.monitoraf;

import java.text.DecimalFormat;

public class StringUtil {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TELEPHONE = "telephone";
    public static final String VERSION = "afi v1.0 ";
    public static final String APPLICATION_JSON_TYPE = "application/json";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String OTP = "otp";
    private static DecimalFormat decimalFormat = new DecimalFormat("$ ###,###.##");
    private static int lineLength = 32;

    private StringUtil() {
    }

    public static int parseInt(String value, Telegram telegram, String tag) {
        try {
            if (value == null || value.isEmpty()) return 0;
            return Integer.parseInt(value
                    .replace(" ", "")
                    .replace("$", "")
                    .replace(",", "")
                    .replace(".", ""));
        } catch (Exception e) {
        }
        return 0;
    }

    public static String formatDouble(double value) {
        return decimalFormat.format((int) value);
    }

    public static String formatInt(int value) {
        return decimalFormat.format(value);
    }

    public static String center(String text) {
        if (text != null) {
            if (text.length() <= lineLength) {
                int offset = (lineLength - text.length()) / 2;
                char[] centerText = "                                ".toCharArray();
                for (int i = 0; i < text.length(); i++) {
                    centerText[i + offset] = text.charAt(i);
                }
                return new String(centerText);
            }
        }
        return "";
    }

    public static String twoColumns(String col1, String col2) {
        char[] centerText = "                                ".toCharArray();
        for (int i = 0; i < col1.length(); i++) {
            centerText[i] = col1.charAt(i);
        }
        int lengthCol2 = col2.length();
        for (int i = 0; i < lengthCol2; i++) {
            centerText[lineLength - lengthCol2 + i] = col2.charAt(i);
        }
        return new String(centerText);
    }
}
