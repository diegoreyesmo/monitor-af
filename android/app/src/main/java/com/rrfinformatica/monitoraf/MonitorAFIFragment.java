package com.rrfinformatica.monitoraf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MonitorAFIFragment extends Fragment {
    private static final String TAG = StringUtil.VERSION + "[monitorAF] ";

    private final TableLayout.LayoutParams rowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    private ProgressBar loadingProgressBar;
    private Button submitButton;
    private Button addActivityButton;
    private Spinner activitySpinner;
    private Spinner daySpinner;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Spinner startHourSpinner;
    private Spinner startMinutesSpinner;
    private Spinner endHourSpinner;
    private Spinner endMinutesSpinner;
    private TextView durationTextView;

    private SharedPreferences sharedPreferences;
    private String username;

    private Context context;
    private EditText input;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_monitoraf, container, false);
        try {
            context = container.getContext();
            VolleyQueue.getInstance(context);
            Telegram.getInstance(context);

            sharedPreferences = container.getContext().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);

            loadingProgressBar = root.findViewById(R.id.loading);
            submitButton = (Button) root.findViewById(R.id.submitButton);
            addActivityButton = (Button) root.findViewById(R.id.addActivityButton);
            activitySpinner = (Spinner) root.findViewById(R.id.activitySpinner);
            daySpinner = (Spinner) root.findViewById(R.id.daySpinner);
            monthSpinner = (Spinner) root.findViewById(R.id.monthSpinner);
            yearSpinner = (Spinner) root.findViewById(R.id.yearSpinner);
            startHourSpinner = (Spinner) root.findViewById(R.id.beginHoursSpinner);
            startMinutesSpinner = (Spinner) root.findViewById(R.id.beginMinutesSpinner);
            endHourSpinner = (Spinner) root.findViewById(R.id.endHoursSpinner);
            endMinutesSpinner = (Spinner) root.findViewById(R.id.endMinutesSpinner);
            durationTextView = (TextView) root.findViewById(R.id.durationEditText);
            username = sharedPreferences.getString(getString(R.string.shared_preference_username), "default");
            submitButton.setOnClickListener(v -> {
                String startDate = yearSpinner.getSelectedItem().toString() + "-" + monthSpinner.getSelectedItem().toString() + "-" + daySpinner.getSelectedItem().toString();
                String startTime = startHourSpinner.getSelectedItem().toString() + ":" + startMinutesSpinner.getSelectedItem().toString();
                String endTime = endHourSpinner.getSelectedItem().toString() + ":" + endMinutesSpinner.getSelectedItem().toString();
                addRegistry(new RegistryDTO(activitySpinner.getSelectedItem().toString(), username, startDate + " " + startTime, startDate + " " + endTime, durationTextView.getText().toString().replace(" minutos", "")));
            });

            input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setOnFocusChangeListener((v1, hasFocus) -> input.post(() -> {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
            }));
            input.requestFocus();
            VolleyQueue.addArrayRequest(generateRequestGetActivity(username));
            addActivityButton.setOnClickListener(v -> {
                if (input.getParent() != null)
                    ((ViewGroup) input.getParent()).removeView(input);
                new AlertDialog.Builder(context)
                        .setMessage("Escriba el nombre de la nueva actividad física")
                        .setTitle("Agregar actividad")
                        .setView(input)
                        .setPositiveButton("Agregar", (dialog, which) -> {
                            startProgressBar();

                            if (!input.getText().toString().trim().isEmpty()) {
                                addActivity(input.getText().toString());
                            }
                        })
                        .setNeutralButton("Cancelar", (dialog, which) -> {
                            stopProgressBar();
                        })
                        .setCancelable(false)
                        .create()
                        .show();

            });

            LocalDateTime instance = LocalDateTime.now();
            Log.d(TAG, instance.toString());

            int dayPosition = instance.getDayOfMonth() - 1;// Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;
            int monthPosition = instance.getMonthValue() - 1;// Calendar.getInstance().get(Calendar.MONTH);
            int yearPosition = 0;
            int hourPosition = instance.getHour();
            int minutesPosition = instance.getMinute();

            daySpinner.setSelection(dayPosition);
            monthSpinner.setSelection(monthPosition);
            yearSpinner.setSelection(yearPosition);

            startHourSpinner.setSelection(hourPosition);
            startMinutesSpinner.setSelection(minutesPosition);
            endHourSpinner.setSelection(hourPosition);
            endMinutesSpinner.setSelection(minutesPosition);

            durationTextView.setText(String.format(Locale.ENGLISH, "%d minutos", 0));

            startHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    calculateDuration(i, startMinutesSpinner.getSelectedItemPosition(), endHourSpinner.getSelectedItemPosition(), endMinutesSpinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            startMinutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    calculateDuration(startHourSpinner.getSelectedItemPosition(), i, endHourSpinner.getSelectedItemPosition(), endMinutesSpinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            endHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    calculateDuration(startHourSpinner.getSelectedItemPosition(), startMinutesSpinner.getSelectedItemPosition(), i, endMinutesSpinner.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            endMinutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    calculateDuration(startHourSpinner.getSelectedItemPosition(), startMinutesSpinner.getSelectedItemPosition(), endHourSpinner.getSelectedItemPosition(), i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (Exception e) {
            ErrorUtil.handleError(e, TAG);
            e.printStackTrace();
        }
        return root;
    }

    private void calculateDuration(int startHour, int startMinute, int endHour, int endMinute) {
        durationTextView.setText(String.format(Locale.ENGLISH, "%d minutos", ((endHour - startHour) * 60) + endMinute - startMinute));
    }

    private void updateActivitySpinner(String[] arraySpinner) {
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arraySpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activitySpinner.setAdapter(adapter);
            activitySpinner.setSelection(adapter.getCount() - 1);
        } catch (Exception e) {
            ErrorUtil.handleError(e, TAG);
            e.printStackTrace();
        }
    }

    public void addActivity(String actividad) {
        try {
            JSONObject postparams = new JSONObject();
            postparams.put("actividad", actividad);
            postparams.put("creadoPor", username);
            JsonObjectRequest jsonObjReq = generateRequestAddActivity(postparams);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyQueue.addRequest(jsonObjReq);
        } catch (JSONException e) {
            ErrorUtil.handleError(e, TAG);
            e.printStackTrace();
        }
    }

    public void addRegistry(RegistryDTO registryDTO) {
        try {
            JSONObject postparams = new JSONObject();
            postparams.put("actividad", registryDTO.getActividad());
            postparams.put("usuario", registryDTO.getUsuario());
            postparams.put("inicio", registryDTO.getIncio());
            postparams.put("termino", registryDTO.getTermino());
            postparams.put("duracion", registryDTO.getDuracion());
            JsonObjectRequest jsonObjReq = generateRequestAddRegistry(postparams);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyQueue.addRequest(jsonObjReq);
        } catch (JSONException e) {
            ErrorUtil.handleError(e, TAG);
            e.printStackTrace();
        }
    }

    private JsonArrayRequest generateRequestGetActivity(String username) {
        return new JsonArrayRequest(Request.Method.GET,
                "http://ec2-18-118-50-38.us-east-2.compute.amazonaws.com:8081/af/get?creadoPor=" + username, null,
                response -> {
                    try {
                        if (response != null) {
                            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.defaultActivities)));
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject activity = response.getJSONObject(i);
                                arrayList.add(activity.getString("actividad"));
                            }
                            String[] arrayString = new String[arrayList.size()];
                            updateActivitySpinner(arrayList.toArray(arrayString));
                        }
                    } catch (Exception e) {
                        ErrorUtil.handleError(e, TAG);
                    }
                    stopProgressBar();
                },
                error -> {
                    ErrorUtil.handleError(error, TAG);
                    stopProgressBar();
                }) {

            @Override
            public Map getHeaders() {
                return HeadersUtil.generateHeaders();
            }
        };
    }

    private JsonObjectRequest generateRequestAddActivity(JSONObject postparams) {
        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-18-118-50-38.us-east-2.compute.amazonaws.com:8081/af/add", postparams,
                response -> {
                    try {
                        if (response != null) {
                            AlertDialogUtil.showSimpleDialog(context, "Ingresado correctamente");
                            VolleyQueue.addArrayRequest(generateRequestGetActivity(username));
                        }
                    } catch (Exception e) {
                        ErrorUtil.handleError(e, TAG);
                    }
                    stopProgressBar();
                },
                error -> {
                    ErrorUtil.handleError(error, TAG);
                    stopProgressBar();
                }) {

            @Override
            public Map getHeaders() {
                return HeadersUtil.generateHeaders();
            }
        };
    }

    private JsonObjectRequest generateRequestAddRegistry(JSONObject postparams) {
        try {
            Telegram.sendMessage("registro/add:" + postparams.toString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-18-118-50-38.us-east-2.compute.amazonaws.com:8081/registro/add", postparams,
                response -> {
                    try {
                        if (response != null) {
                            AlertDialogUtil.showSimpleDialog(context, "Ingresado correctamente");
                        }
                    } catch (Exception e) {
                        ErrorUtil.handleError(e, TAG);
                    }
                    stopProgressBar();
                },
                error -> {
                    ErrorUtil.handleError(error, TAG);
                    stopProgressBar();
                }) {

            @Override
            public Map getHeaders() {
                return HeadersUtil.generateHeaders();
            }
        };
    }

    private void startProgressBar() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void stopProgressBar() {
        loadingProgressBar.setVisibility(View.INVISIBLE);
    }

}
