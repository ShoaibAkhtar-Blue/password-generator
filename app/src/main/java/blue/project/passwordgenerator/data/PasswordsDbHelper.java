package blue.project.passwordgenerator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PasswordsDbHelper extends SQLiteOpenHelper {
    // Constants for database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "passwords.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + PasswordsContract.PasswordsEntry.TABLE_NAME + " (" +
            PasswordsContract.PasswordsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PasswordsContract.PasswordsEntry.COLUMN_ACCOUNT_ID + " TEXT NOT NULL UNIQUE, " +
            PasswordsContract.PasswordsEntry.COLUMN_PASSWORD + " TEXT NOT NULL)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE " + PasswordsContract.PasswordsEntry.TABLE_NAME;

    public PasswordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO: In case of upgrading the database, implement this method.
    }
}
