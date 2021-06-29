package blue.project.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import blue.project.passwordgenerator.data.PasswordsContract;

public class EditorActivity extends AppCompatActivity {
    private EditText accountIdEditText;
    private EditText accountPasswordEditText;
    private ImageView visibilityImageView;
    private Button saveAccountInfoButton;
    private boolean isPasswordVisible = false;
    private String pw;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Initializing views
        initViews();

        // Setting title of this activity
        this.setTitle("Account Information");

        // Enabling Home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting intent
        Intent intent = getIntent();
        pw = intent.getStringExtra(getString(R.string.intent_extra_password));
        uri = intent.getData();

        /**
         * There are three modes of Editor Activity.
         * MODE 1: Save account information when password is already provided. In this case, pw is
         * available but uri is null.
         * MODE 2: Update the existing information. In this case, pw is null but uri is available.
         * MODE 3: Add new account information. In this case, pw and uri both are null.
         */
        if (pw != null) {   // MODE 1
            accountPasswordEditText.setText(pw);
        } else if (uri != null) {   // MODE 2
            try {
                // Getting data from database related to particular account id
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                // Displaying data
                if (cursor.moveToNext()) {
                    String aId = cursor.getString(cursor.getColumnIndex(PasswordsContract.PasswordsEntry.COLUMN_ACCOUNT_ID));
                    String aPw = cursor.getString(cursor.getColumnIndex(PasswordsContract.PasswordsEntry.COLUMN_PASSWORD));
                    accountIdEditText.setText(aId);
                    accountPasswordEditText.setText(aPw);
                    saveAccountInfoButton.setText("Update");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        // Setting onClickListener on visibility ImageView for showing/hiding password
        visibilityImageView.setOnClickListener(view -> {
            if (!isPasswordVisible) {
                accountPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                visibilityImageView.setImageResource(R.drawable.ic_action_hide_password);
                isPasswordVisible = true;
            } else {
                accountPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                visibilityImageView.setImageResource(R.drawable.ic_action_show_password);
                isPasswordVisible = false;
            }
        });

        // Setting onClickListener on Save/Update button
        saveAccountInfoButton.setOnClickListener(view -> {
            try {
                if (pw != null) {   // MODE 1
                    saveData();
                    finish();
                } else if (uri != null) {   // MODE 2
                    updateAccountInfo();
                    finish();
                } else {    // MODE 3
                    saveData();
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(EditorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Redirecting user back to Main Activity
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        accountIdEditText = findViewById(R.id.editText_account_id);
        accountPasswordEditText = findViewById(R.id.editTextText_account_password);
        visibilityImageView = findViewById(R.id.imageView_visibility);
        saveAccountInfoButton = findViewById(R.id.button_save_account_info);
    }

    /**
     * This method saves the account information in the database.
     */
    private void saveData() {
        String accountId = accountIdEditText.getText().toString().trim();
        String accountPw = accountPasswordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(accountId)) {
            throw new IllegalArgumentException(getString(R.string.hint_account_id));
        } else if (TextUtils.isEmpty(accountPw)) {
            throw new IllegalArgumentException(getString(R.string.hint_account_password));
        } else if (isRecordAvailable(accountId)) {
            throw new IllegalArgumentException(getString(R.string.error_duplicate_account_id));
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PasswordsContract.PasswordsEntry.COLUMN_ACCOUNT_ID, accountId);
            contentValues.put(PasswordsContract.PasswordsEntry.COLUMN_PASSWORD, accountPw);
            Uri uri = getContentResolver().insert(PasswordsContract.PasswordsEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                Toast.makeText(this, "Account information saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method updates the account information.
     */
    private void updateAccountInfo() {
        String accountId = accountIdEditText.getText().toString().trim();
        String accountPw = accountPasswordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(accountId)) {
            throw new IllegalArgumentException(getString(R.string.hint_account_id));
        } else if (TextUtils.isEmpty(accountPw)) {
            throw new IllegalArgumentException(getString(R.string.hint_account_password));
        } else {
            ContentValues values = new ContentValues();
            values.put(PasswordsContract.PasswordsEntry.COLUMN_ACCOUNT_ID, accountId);
            values.put(PasswordsContract.PasswordsEntry.COLUMN_PASSWORD, accountPw);
            int rowsUpdated = getContentResolver().update(uri, values, null, null);
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Information Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method checks for duplicate account ID.
     * @param s - Account ID
     * @return  true if duplicate account ID is found
     */
    private boolean isRecordAvailable(String s) {
        String selection = PasswordsContract.PasswordsEntry.COLUMN_ACCOUNT_ID + " = ?";
        String[] selectionArgs = {s};
        Cursor cursor = getContentResolver().query(PasswordsContract.PasswordsEntry.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor.moveToNext()) {
            return true;
        } else {
            return false;
        }
    }
}