package com.rrfinformatica.monitoraf;

import java.util.HashMap;
import java.util.Map;

public class HeadersUtil {
    public static Map generateHeaders(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put(StringUtil.HEADER_CONTENT_TYPE, StringUtil.APPLICATION_JSON_TYPE);
        return headers;
    }
}
