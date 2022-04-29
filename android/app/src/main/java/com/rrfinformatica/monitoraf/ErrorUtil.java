package com.rrfinformatica.monitoraf;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtil {

    public static void handleError(Exception exception, String tag) {
        try {
            StringWriter errors = new StringWriter();
            exception.printStackTrace(new PrintWriter(errors));
            Telegram.sendErrorMessage(tag + errors);
        } catch (Exception e) {
        }
    }

    public static String handleVolleyErrorResponse(Context context, VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ApiResponse apiResponse = gson.fromJson(new String(error.networkResponse.data), ApiResponse.class);
            if (apiResponse.getCode().equals("fail") && apiResponse.getMessage() != null && apiResponse.getMessage() instanceof String) {
                String message = (String) apiResponse.getMessage();
                AlertDialogUtil.showSimpleDialog(context, message);
                return message;
            }
        }
        AlertDialogUtil.showSimpleDialog(context, "Verifique su conexión a internet");
        return "";
    }
}
