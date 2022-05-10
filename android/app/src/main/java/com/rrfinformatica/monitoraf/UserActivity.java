package com.rrfinformatica.monitoraf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class UserActivity extends AppCompatActivity {
    private static final String TAG = StringUtil.VERSION + "[validate_top] ";
    private static final int OTP_MIN_LENGTH = 6;
    private Button buttonValidate;
    private EditText editText;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
private String keySharedPreferenceUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            VolleyQueue.getInstance(this);
            Telegram.getInstance(this);
            setContentView(R.layout.activity_user);
            editText = findViewById(R.id.otp);
            buttonValidate = findViewById(R.id.button_validate_otp);
            sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
            keySharedPreferenceUsername = getString(R.string.shared_preference_username);
            if(sharedPreferences.contains(keySharedPreferenceUsername) && !sharedPreferences.getString(keySharedPreferenceUsername,"default").equals("default")){
                editText.setText(sharedPreferences.getString(keySharedPreferenceUsername,"default"));
                Intent intent = new Intent(getApplicationContext(), MonitorAFIFragment.class);
                startActivity(intent);
            }
            editor = sharedPreferences.edit();
            buttonValidate.setOnClickListener(l -> saveUsername());
        } catch (Exception e) {
            ErrorUtil.handleError(e, TAG);
        }
    }

    private void saveUsername() {
        try {
            if (editText != null && editText.getText() != null) {
                String editTextValue = editText.getText().toString().trim().toLowerCase();
                String username = editTextValue.isEmpty() ? "default" : editTextValue;
                editor.putString(keySharedPreferenceUsername, username);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MonitorAFIFragment.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            ErrorUtil.handleError(e, "UserActivity");
        }
    }
}
