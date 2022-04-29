package com.rrfinformatica.monitoraf;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;


public class Telegram {
    public static final String TAG = "telegram";
    private static VolleyQueue volleyQueue;
    private static String chatIdError;
    private static String urlTelegramInfo;
    private static String chatIdInfo;
    private static String urlTelegramError;
    private static Telegram telegram;

    private Telegram() {
    }

    public static Telegram getInstance(Context context) {
        if (telegram == null) {
            urlTelegramError = context.getString(R.string.url_telegram_error);
            chatIdError = context.getString(R.string.chat_id_error);
            urlTelegramInfo = context.getString(R.string.url_telegram_info);
            chatIdInfo = context.getString(R.string.chat_id_info);
            volleyQueue = VolleyQueue.getInstance(context);
            telegram = new Telegram();
        }
        return telegram;
    }

    public static void sendErrorMessage(String text) {
        if (text == null || text.isEmpty()) return;
        try {
            JSONObject postparams = new JSONObject();
            postparams.put("chat_id", chatIdError);
            postparams.put("text", text);
            JsonObjectRequest jsonObjReq = generarSendErrorMessage(postparams);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            volleyQueue.addRequest(jsonObjReq);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static JsonObjectRequest generarSendErrorMessage(JSONObject postparams) {
        return new JsonObjectRequest(Request.Method.POST,
                urlTelegramError, postparams,
                response -> {
                },
                error -> {
                }) {

            @Override
            public Map getHeaders() {
                return HeadersUtil.generateHeaders();
            }
        };
    }

    public static void sendMessage(String text) {
        if (text == null || text.isEmpty()) return;
        try {
            JSONObject postparams = new JSONObject();
            postparams.put("chat_id", chatIdInfo);
            postparams.put("text", text);
            JsonObjectRequest jsonObjReq = generarSendMessage(postparams);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            volleyQueue.addRequest(jsonObjReq);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static JsonObjectRequest generarSendMessage(JSONObject postparams) {
        return new JsonObjectRequest(Request.Method.POST,
                urlTelegramInfo, postparams,
                response -> {
                },
                error -> {
                }) {

            @Override
            public Map getHeaders() {
                return HeadersUtil.generateHeaders();
            }
        };
    }
}
