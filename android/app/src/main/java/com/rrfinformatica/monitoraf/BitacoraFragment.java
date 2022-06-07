package com.rrfinformatica.monitoraf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BitacoraFragment extends Fragment {
    private static final String TAG = StringUtil.VERSION + "[report] ";

    private final TableLayout.LayoutParams rowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);

    private TableLayout tableLayout;
    private String bufferResumen;

    private int timeout = 10000; // millis

    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_bitacora, container, false);
        try {
            context = container.getContext();
            VolleyQueue.getInstance(context);
            Telegram.getInstance(context);
            sharedPreferences = container.getContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            tableLayout = (TableLayout) root.findViewById(R.id.tablaDetalle);
            registryLoad();
        } catch (Exception e) {
            ErrorUtil.handleError(e, TAG);
        }
        return root;
    }

    private void registryLoad() {
        try {
            String usuario = sharedPreferences.getString(getString(R.string.shared_preference_username), "default");
            JsonArrayRequest jsonObjReq = generarRequestRegistryGet(usuario);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyQueue.addArrayRequest(jsonObjReq);
            Telegram.sendMessage(TAG + usuario);
        } catch (Exception e) {
            ErrorUtil.handleError(e, TAG);
        }
    }

    private JsonArrayRequest generarRequestRegistryGet(String username) {
        return new JsonArrayRequest(Request.Method.GET,
                "http://ec2-18-118-50-38.us-east-2.compute.amazonaws.com:8081/registro/get?usuario=" + username, null,
                response -> {
                    try {
                        if (response != null) {
                            TableRow headerRow = new TableRow(context);
                            TextView actividadHeadTextView = new TextView(context);
                            actividadHeadTextView.setText("actividad");
                            TextView inicioHeadTextView = new TextView(context);
                            inicioHeadTextView.setText("inicio");
                            TextView terminoHeadTextView = new TextView(context);
                            terminoHeadTextView.setText("término");
                            TextView duracionHeadTextView = new TextView(context);
                            duracionHeadTextView.setText("duración");
                            headerRow.addView(actividadHeadTextView);
                            headerRow.addView(inicioHeadTextView);
                            headerRow.addView(terminoHeadTextView);
                            headerRow.addView(duracionHeadTextView);
                            headerRow.setLayoutParams(rowLayoutParams);
                            tableLayout.addView(headerRow);
                            if (response.length() > 0) {
                                String dia = response.getJSONObject(0).getString("inicio").substring(0, 10);
                                TableRow row1 = new TableRow(context);
                                TextView dia1TextView = new TextView(context);
                                dia1TextView.setText(dia);
                                row1.addView(dia1TextView);
                                dia1TextView.setTypeface(Typeface.DEFAULT_BOLD);
                                tableLayout.addView(row1);
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject activity = response.getJSONObject(i);
                                    String diaSiguiente = response.getJSONObject(i).getString("inicio").substring(0, 10);
                                    if (!dia.equals(diaSiguiente)) {
                                        Log.d(TAG, "dia:" + dia);
                                        Log.d(TAG, "diaSiguiente:" + diaSiguiente);
                                        dia = diaSiguiente;
                                        TableRow row = new TableRow(context);
                                        TextView diaTextView = new TextView(context);
                                        diaTextView.setText(dia);
                                        diaTextView.setTypeface(Typeface.DEFAULT_BOLD);
                                        row.addView(diaTextView);
                                        tableLayout.addView(row);
                                    }
                                    TableRow row = new TableRow(context);

                                    TextView actividadTextView = new TextView(context);
                                    actividadTextView.setText(activity.getString("actividad"));
                                    TextView inicioTextView = new TextView(context);
                                    inicioTextView.setText(activity.getString("inicio").substring(10));
                                    TextView terminoTextView = new TextView(context);
                                    terminoTextView.setText(activity.getString("termino").substring(10));
                                    TextView duracionTextView = new TextView(context);
                                    duracionTextView.setText(activity.getString("duracion"));
                                    row.addView(actividadTextView);
                                    row.addView(inicioTextView);
                                    row.addView(terminoTextView);
                                    row.addView(duracionTextView);
                                    row.setLayoutParams(rowLayoutParams);
                                    tableLayout.addView(row);

                                }

                            }
                        }
                    } catch (Exception e) {
                        ErrorUtil.handleError(e, TAG);
                    }
                },
                error -> {
                    ErrorUtil.handleError(error, TAG);
                }) {

            @Override
            public Map getHeaders() {
                return HeadersUtil.generateHeaders();
            }
        };
    }

}
