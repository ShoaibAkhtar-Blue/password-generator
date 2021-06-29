package blue.project.passwordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePinActivity extends AppCompatActivity {
    private TextView pinTextView;
    private TextView confirmPinTextView;
    private EditText pinEditText;
    private EditText confirmPinEditText;
    private Button createPinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        this.setTitle("Create PIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing views
        initViews();

        createPinButton.setOnClickListener(view -> {
            String input1 = pinEditText.getText().toString().trim();
            String input2 = confirmPinEditText.getText().toString().trim();
            if (TextUtils.isEmpty(input1)) {
                Toast.makeText(CreatePinActivity.this, "Enter PIN", Toast.LENGTH_SHORT).show();
            } else if (input1.length() < getResources().getInteger(R.integer.pin_length)) {
                Toast.makeText(CreatePinActivity.this, "PIN must be 6 digits long", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(input2)) {
                Toast.makeText(CreatePinActivity.this, "Confirm PIN", Toast.LENGTH_SHORT).show();
            } else if (!input2.equals(input1)) {
                Toast.makeText(CreatePinActivity.this, "PIN does not match", Toast.LENGTH_SHORT).show();
            } else {
                // Storing PIN on Shared Preferences
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_pin), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.pin), input1);
                if (editor.commit()) {
                    Toast.makeText(CreatePinActivity.this, "PIN saved", Toast.LENGTH_SHORT).show();

                    // Redirecting user to Saved Passwords Activity
                    Intent intent = new Intent(CreatePinActivity.this, SavedPasswordsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreatePinActivity.this, "Error in saving PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method initializes the views.
     */
    private void initViews() {
        pinTextView = findViewById(R.id.textView_pin);
        confirmPinTextView = findViewById(R.id.textView_confirm_pin);
        pinEditText = findViewById(R.id.editText_pin);
        confirmPinEditText = findViewById(R.id.editText_confirm_pin);
        createPinButton = findViewById(R.id.button_create_pin);
    }
}