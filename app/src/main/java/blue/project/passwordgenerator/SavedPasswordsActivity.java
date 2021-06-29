package blue.project.passwordgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import blue.project.passwordgenerator.data.PasswordsContract;

public class SavedPasswordsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView passwordsListView;
    private FloatingActionButton addNewAccountFAB;
    private PasswordCursorAdapter cursorAdapter;
    private static final int PASSWORD_LOADER = 0;

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, PasswordsContract.PasswordsEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    /**
     * Creating menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_saved_passwords, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_passwords);

        // Setting title of this activity
        this.setTitle("Saved Passwords");

        // Enabling Home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cursorAdapter = new PasswordCursorAdapter(this, null);
        View emptyView = findViewById(R.id.empty_view);
        passwordsListView = findViewById(R.id.listView_passwords);
        passwordsListView.setAdapter(cursorAdapter);
        addNewAccountFAB = findViewById(R.id.floatingActionButton_add_new_account);

        // Setting empty view
        // This view is displayed when there is no data in the database to show.
        passwordsListView.setEmptyView(emptyView);

        // Initializing the loader
        getLoaderManager().initLoader(PASSWORD_LOADER, null, this);

        // Setting OnItemClickedListener
        // It navigates the user to Editor Activity.
        passwordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             *
             * @param adapterView
             * @param view
             * @param i - Position of the item clicked
             * @param l - Id of the item clicked
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = ContentUris.withAppendedId(PasswordsContract.PasswordsEntry.CONTENT_URI, l);
                Intent intent = new Intent(SavedPasswordsActivity.this, EditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        // Setting onClickListener on FAB
        // It navigates the user to Editor Activity
        addNewAccountFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavedPasswordsActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Implementing the functionality of Home button.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_change_pin:
                //Toast.makeText(this, "Change PIN is selected", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ChangePinActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                // Redirecting user to Main Activity
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}