package blue.project.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBarPS;
    private EditText passwordLengthEditText;
    private SwitchCompat specialCharactersSwitch;
    private TextView passwordTextView;
    private TextView passwordStrengthTextView;
    private Button generateButton;
    private Button copyButton;
    private Button saveButton;
    private boolean includeSpecialCharacters = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_manage_passwords) {
            managePasswords();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        initViews();

        // Setting onCheckedChangeListener to detect whether switch is on or off.
        specialCharactersSwitch.setOnCheckedChangeListener((compoundButton, b) -> includeSpecialCharacters = b);

        // Set onClickListener on generate button
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePassword();
            }
        });

        // Setting onClickListener on copy button for copying password on the clipboard.
        copyButton.setOnClickListener(view -> {
            try {
                String data = passwordTextView.getText().toString();

                // Getting handle to clipboard service
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", data);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this, "Password is copied on Clipboard", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error! Unable to copy Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting onClickListener on Save button
        saveButton.setOnClickListener(view -> {
            String pw = passwordTextView.getText().toString().trim();
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            intent.putExtra(getString(R.string.intent_extra_password), pw);
            startActivity(intent);
        });
    }

    /**
     * This method initializes the views.
     */
    private void initViews() {
        passwordLengthEditText = findViewById(R.id.editText_password_length);
        specialCharactersSwitch = findViewById(R.id.switch_sc);
        generateButton = findViewById(R.id.btnGenerate);
        copyButton = findViewById(R.id.button_copy);
        saveButton = findViewById(R.id.button_save);
        passwordTextView = findViewById(R.id.textView_password);
        progressBarPS = findViewById(R.id.progressBar_ps);
        passwordStrengthTextView = findViewById(R.id.textView_password_strength);
    }

    /**
     * This method is called when Generate button is clicked.
     * Its purpose is to generate password according to user inputs.
     * @param view
     */
    public void generatePassword() {
        String pLength = passwordLengthEditText.getText().toString().trim();
        if (TextUtils.isEmpty(pLength)) {
            Toast.makeText(this, "Password length is required", Toast.LENGTH_SHORT).show();
        } else {
            int pwLength = Integer.parseInt(pLength);
            if (pwLength >= getResources().getInteger(R.integer.password_min_length) && pwLength <= getResources().getInteger(R.integer.password_max_length)) {
                String pw = RandomString.getString(pwLength, includeSpecialCharacters);
                passwordTextView.setVisibility(View.VISIBLE);
                progressBarPS.setVisibility(View.VISIBLE);
                passwordTextView.setText(pw);
                setPasswordStrength(pwLength);
                copyButton.setEnabled(true);
                saveButton.setEnabled(true);
            } else {
                Toast.makeText(this, "Password must be " + getResources().getInteger(R.integer.password_min_length) + " to " + getResources().getInteger(R.integer.password_max_length) + " characters long.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method sets the password strength on progress bar
     * @param length    Length of password
     */
    private void setPasswordStrength(int length) {
        int indicatorValue = 0;
        int indicatorColor = 0;
        String ps = null;

        if (includeSpecialCharacters) {
            if (length <= 8) {
                indicatorValue = 40;
                //indicatorColor = getColor(R.color.color_weak_password);
                indicatorColor = ContextCompat.getColor(this, R.color.color_weak_password);
                ps = getString(R.string.weak_password);
            } else if (length > 8 && length < 16) {
                indicatorValue = 80;
                indicatorColor = getColor(R.color.color_medium_password);
                ps = getString(R.string.medium_password);
            } else if (length >= 16) {
                indicatorValue = 100;
                indicatorColor = getColor(R.color.color_strong_password);
                ps = getString(R.string.strong_password);
            }
        } else {
            if (length <= 8) {
                indicatorValue = 20;
                indicatorColor = getColor(R.color.color_weak_password);
                ps = getString(R.string.weak_password);
            } else if (length > 8 && length < 16) {
                indicatorValue = 60;
                indicatorColor = getColor(R.color.color_medium_password);
                ps = getString(R.string.medium_password);
            } else if (length >= 16) {
                indicatorValue = 100;
                indicatorColor = getColor(R.color.color_strong_password);
                ps = getString(R.string.strong_password);
            }
        }
        progressBarPS.setProgress(indicatorValue);
        //progressBarPS.getCurrentDrawable().setColorFilter(indicatorColor, PorterDuff.Mode.SRC_IN);
        passwordStrengthTextView.setText(ps);
        passwordStrengthTextView.setTextColor(indicatorColor);
    }

    /**
     * This method is used to navigate the user to Create PIN Activity or Login Activity.
     * If PIN is already created then this method redirects the user to Login Activity.
     * If PIN is not created then this method redirects the user to Create PIN Activity.
     */
    private void managePasswords() {
        // Getting shared preferences to check whether PIN is available or not
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_pin), MODE_PRIVATE);
        String pin = sharedPreferences.getString(getString(R.string.pin), null);
        Intent intent;
        if (pin != null) {
            // PIN is available, redirecting the user to Login Activity
            intent = new Intent(this, LoginActivity.class);
        } else {
            // PIN is not available, redirecting the user to Create PIN Activity
            intent = new Intent(this, CreatePinActivity.class);
        }
        startActivity(intent);
    }
}