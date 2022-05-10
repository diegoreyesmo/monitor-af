package com.rrfinformatica.monitoraf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BitacoraFragment extends Fragment {
    private static final String TAG = StringUtil.VERSION + "[report] ";

    private final TableLayout.LayoutParams rowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    private Button submitButton;
    private Spinner startDay;
    private Spinner startMonth;
    private Spinner startYear;
    private Spinner endDay;
    private Spinner endMonth;
    private Spinner endYear;
    private Spinner tipoDocumento;
    private TextView resumenText;
    private TableLayout tablaResumen;
    private TableLayout tablaDetalle;
    private String bufferResumen;
    // WS
    private RequestQueue requestQueue;
    private int timeout = 10000; // millis
    private String resumenPath;
    private String documentListPath;

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
            sharedPreferences = context.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            submitButton = (Button) root.findViewById(R.id.submitButton);
            startDay = (Spinner) root.findViewById(R.id.startDay);
            startMonth = (Spinner) root.findViewById(R.id.startMonth);
            startYear = (Spinner) root.findViewById(R.id.startYear);
            endDay = (Spinner) root.findViewById(R.id.endDay);
            endMonth = (Spinner) root.findViewById(R.id.endMonth);
            endYear = (Spinner) root.findViewById(R.id.endYear);
            tipoDocumento = (Spinner) root.findViewById(R.id.tipo_documento_spinner);
            resumenText = (TextView) root.findViewById(R.id.resumenText);
            tablaResumen = (TableLayout) root.findViewById(R.id.tablaResumen);
            tablaDetalle = (TableLayout) root.findViewById(R.id.tablaDetalle);

            submitButton.setOnClickListener(v -> {
                String startDate = startYear.getSelectedItem().toString() + "-" + startMonth.getSelectedItem().toString() + "-" + startDay.getSelectedItem().toString();
                String endDate = endYear.getSelectedItem().toString() + "-" + endMonth.getSelectedItem().toString() + "-" + endDay.getSelectedItem().toString();
                tablaResumen.removeAllViews();
                tablaDetalle.removeAllViews();
                //documentList(resumenRequestDTO);
            });

            int dayPosition = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;
            int monthPosition = Calendar.getInstance().get(Calendar.MONTH);
            int yearPosition = 0;
            startDay.setSelection(dayPosition);
            startMonth.setSelection(monthPosition);
            startYear.setSelection(yearPosition);

            endDay.setSelection(dayPosition);
            endMonth.setSelection(monthPosition);
            endYear.setSelection(yearPosition);

            //documentListPath = getString(R.string.invoice_range_url);

        } catch (Exception e) {
            ErrorUtil.handleError(e, TAG);
        }
        return root;
    }

    private void documentList() {
        try {
            JSONObject postparams = new JSONObject();
//            postparams.put(StringUtil.USERNAME, resumenRequestDTO.getUsername());
//            postparams.put(StringUtil.PASSWORD, resumenRequestDTO.getPassword());
//            postparams.put(StringUtil.IDPOS, resumenRequestDTO.getIdPos());
//            postparams.put(StringUtil.FECHADESDE, resumenRequestDTO.getFechadesde());
//            postparams.put(StringUtil.FECHAHASTA, resumenRequestDTO.getFechahasta());
//            postparams.put(StringUtil.TIPO_DOCUMENTO, resumenRequestDTO.getTipoDocumento());
//            postparams.put(StringUtil.ID_SUCURSAL, resumenRequestDTO.getIdSucursal());
            JsonObjectRequest jsonObjReq = generarRequestDocumentList(postparams);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Telegram.sendMessage(TAG + "[list] " + postparams.toString(2));
            requestQueue.add(jsonObjReq);
        } catch (JSONException e) {
            ErrorUtil.handleError(e, TAG);
        }
    }

    private JsonObjectRequest generarRequestDocumentList(JSONObject postparams) {
        return new JsonObjectRequest(Request.Method.POST,
                documentListPath, postparams,
                response -> {
                    try {
                        if (response != null) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                            documentListResponseDTO = gson.fromJson(response.toString(), DocumentListResponseDTO.class);
//
//                            if (documentListResponseDTO != null && documentListResponseDTO.getDTEs() != null && !documentListResponseDTO.getDTEs().isEmpty()) {
//                                TableRow headerRow = new TableRow(this);
//
//                                TextView folioHeaderTextView = new TextView(this);
//                                folioHeaderTextView.setText("Folio");
//
//                                TextView montoHeaderTextView = new TextView(this);
//                                montoHeaderTextView.setText("Monto");
//
//                                TextView fechaHeaderTextView = new TextView(this);
//                                fechaHeaderTextView.setText("Fecha");
//
//                                headerRow.addView(folioHeaderTextView);
//                                headerRow.addView(montoHeaderTextView);
//                                headerRow.addView(fechaHeaderTextView);
//                                headerRow.setLayoutParams(rowLayoutParams);
//                                tablaDetalle.addView(headerRow);
//                                for (DocumentListItemResponseDTO itemDTO : documentListResponseDTO.getDTEs()) {
//                                    String monto = StringUtil.formatInt(StringUtil.parseInt(itemDTO.getAmount(), telegram, TAG));
//                                    TableRow row = new TableRow(this);
//
//                                    TextView folioTextView = new TextView(this);
//                                    folioTextView.setText(itemDTO.getFolio());
//
//                                    TextView montoTextView = new TextView(this);
//                                    montoTextView.setText(monto);
//
//                                    TextView fechaTextView = new TextView(this);
//                                    fechaTextView.setText(itemDTO.getDate());
//
//                                    row.addView(folioTextView);
//                                    row.addView(montoTextView);
//                                    row.addView(fechaTextView);
//                                    row.setLayoutParams(rowLayoutParams);
//                                    tablaDetalle.addView(row);
//                                }
//                            }
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
