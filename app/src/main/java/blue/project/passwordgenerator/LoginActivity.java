package blue.project.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText loginPinEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set title of this activity
        this.setTitle("Login");

        // Enable Home button i.e. up arrow button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views in the layout
        initViews();

        // Set onClickListener on log in button
        loginButton.setOnClickListener(view -> {
            String userPin = loginPinEditText.getText().toString().trim();
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_pin), MODE_PRIVATE);
            String pin = sharedPreferences.getString(getString(R.string.pin), null);
            if (pin.equals(userPin)) {
                Intent intent = new Intent(LoginActivity.this, SavedPasswordsActivity.class);
                startActivity(intent);
            } else if (TextUtils.isEmpty(userPin)) {
                Toast.makeText(LoginActivity.this, "Enter PIN", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Implement the functionality of Home button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Go back to previous activity
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Initialize views
     */
    private void initViews() {
        loginPinEditText = findViewById(R.id.editText_login_pin);
        loginButton = findViewById(R.id.button_login);
    }
}