package blue.project.passwordgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import blue.project.passwordgenerator.data.PasswordsContract;

public class PasswordCursorAdapter extends CursorAdapter {
    private Context context;
    private boolean isPasswordVisible = false;

    public PasswordCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);   // Last argument is the flag
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView accountId = view.findViewById(R.id.textView_a_id);
        EditText accountPw = view.findViewById(R.id.editText_a_password);
        ImageView showPasswordImageView = view.findViewById(R.id.imageView_show_password);
        ImageView delAccountImageView = view.findViewById(R.id.imageView_del_account);
        ImageView copyPasswordImageView = view.findViewById(R.id.imageView_copy_password);
        RelativeLayout parentLayout = view.findViewById(R.id.layout_parent);

        // Getting data from cursor
        String aId = cursor.getString(cursor.getColumnIndex(PasswordsContract.PasswordsEntry.COLUMN_ACCOUNT_ID));
        String aPw = cursor.getString(cursor.getColumnIndex(PasswordsContract.PasswordsEntry.COLUMN_PASSWORD));

        showPasswordImageView.setOnClickListener(view1 -> {
            // Hiding/Showing password
            if (!isPasswordVisible) {
                accountPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());   // Showing password
                showPasswordImageView.setImageResource(R.drawable.ic_action_hide_password);
                isPasswordVisible = true;
            } else {
                accountPw.setTransformationMethod(PasswordTransformationMethod.getInstance());  // Hiding password
                showPasswordImageView.setImageResource(R.drawable.ic_action_show_password);
                isPasswordVisible = false;
            }
        });

        // Getting id of the account
        int id = cursor.getInt(cursor.getColumnIndex(PasswordsContract.PasswordsEntry.COLUMN_ID));

        delAccountImageView.setOnClickListener(view12 -> {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.warning_delete_account_info));
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Deleting account information from database
                    context.getContentResolver().delete(ContentUris.withAppendedId(PasswordsContract.PasswordsEntry.CONTENT_URI, id), null, null);
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    if (dialogInterface != null) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Setting onClickListener on copy password ImageView
        copyPasswordImageView.setOnClickListener(view13 -> {
            try {
                // Getting Clipboard service
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);

                // Making clip of the data
                ClipData clip = ClipData.newPlainText("password", aPw);

                // Clipping data to clipboard
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Password is copied on Clipboard", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error! Unable to copy Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting data to views
        accountId.setText(aId);
        accountPw.setText(aPw);
    }
}
