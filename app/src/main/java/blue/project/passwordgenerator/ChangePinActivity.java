package blue.project.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePinActivity extends AppCompatActivity {
    private EditText newPinEditText;
    private EditText confirmNewPinEditText;
    private Button changePinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        // Initializing views
        initViews();

        // Setting title of the activity
        this.setTitle("Change PIN");

        // Enabling Home/back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting onClickListener on changePinButton
        changePinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input1 = newPinEditText.getText().toString().trim();
                String input2 = confirmNewPinEditText.getText().toString().trim();
                if (TextUtils.isEmpty(input1)) {
                    Toast.makeText(ChangePinActivity.this, "Enter PIN", Toast.LENGTH_SHORT).show();
                } else if (input1.length() < getResources().getInteger(R.integer.pin_length)) {
                    Toast.makeText(ChangePinActivity.this, "PIN must be 6 digits long", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(input2)) {
                    Toast.makeText(ChangePinActivity.this, "Confirm PIN", Toast.LENGTH_SHORT).show();
                } else if (!input2.equals(input1)) {
                    Toast.makeText(ChangePinActivity.this, "PIN does not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Storing PIN on Shared Preferences
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_pin), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.pin), input1);
                    if (editor.commit()) {
                        Toast.makeText(ChangePinActivity.this, "PIN saved", Toast.LENGTH_SHORT).show();
                        finish();   //This method finish the current activity and navigate the user back to previous activity
                    } else {
                        Toast.makeText(ChangePinActivity.this, "Error in saving PIN", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * This method initialize the views
     */
    private void initViews() {
        newPinEditText = findViewById(R.id.editText_new_pin);
        confirmNewPinEditText = findViewById(R.id.editText_confirm_new_pin);
        changePinButton = findViewById(R.id.button_change_pin);
    }

    /**
     * Implementing the functionality of Home/Back button.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {

            // Navigate the user to the previous activity i.e. SavedPasswordsActivity
            onBackPressed();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}