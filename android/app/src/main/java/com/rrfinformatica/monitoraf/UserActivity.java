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
            editor = sharedPreferences.edit();
            buttonValidate.setOnClickListener(l -> saveUsername());
        } catch (Exception e) {
            ErrorUtil.handleError(e, TAG);
        }
    }

    private void saveUsername() {
        try {
            if (editText != null && editText.getText() != null) {
                String editTextValue = editText.getText().toString().trim();
                String username = editTextValue.isEmpty() ? "default" : editTextValue;
                String keySharedPreferenceUsername = getString(R.string.shared_preference_username);
                editor.putString(keySharedPreferenceUsername, username);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),MonitorAFActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            ErrorUtil.handleError(e, "UserActivity");
        }
    }
}
