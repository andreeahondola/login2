package com.example.andreea.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.andreea.login.content.User;
import com.example.andreea.login.service.NoteManager;
import com.example.andreea.login.util.Cancellable;
import com.example.andreea.login.util.DialogUtils;
import com.example.andreea.login.util.OnErrorListener;
import com.example.andreea.login.util.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private Cancellable mCancellable;
    private NoteManager mNoteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mNoteManager = ((LoginApp) getApplication()).getNoteManager();
        User user = mNoteManager.getCurrentUser();
        if (user != null) {
            startNoteListActivity();
            finish();
        }
        setupToolbar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCancellable != null) {
            mCancellable.cancel();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                Snackbar.make(view, "Authenticating, please wait", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
            }
        });
    }

    private void login() {
        EditText usernameEditText = (EditText) findViewById(R.id.username);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        mCancellable = mNoteManager
                .loginAsync(
                        usernameEditText.getText().toString(), passwordEditText.getText().toString(),
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startNoteListActivity();
                                    }
                                });
                            }
                        }, new OnErrorListener() {
                            @Override
                            public void onError(final Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtils.showError(MainActivity.this, e);
                                    }
                                });
                            }
                        });
    }

    private void startNoteListActivity() {
        startActivity(new Intent(this, NoteListActivity.class));
    }
}
