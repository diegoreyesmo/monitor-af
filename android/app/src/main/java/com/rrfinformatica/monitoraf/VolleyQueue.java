package com.rrfinformatica.monitoraf;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class VolleyQueue {
    private static VolleyQueue volleyQueue;
    private static RequestQueue requestQueue;

    private VolleyQueue() {
    }

    public static VolleyQueue getInstance(Context context) {
        if (volleyQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            volleyQueue = new VolleyQueue();
        }
        return volleyQueue;
    }

    public void addRequest(JsonObjectRequest jsonObjectRequest) {
        if (requestQueue != null)
            requestQueue.add(jsonObjectRequest);
    }

    public void addArrayRequest(JsonArrayRequest jsonArrayRequest) {
        if (requestQueue != null)
            requestQueue.add(jsonArrayRequest);
    }
}
