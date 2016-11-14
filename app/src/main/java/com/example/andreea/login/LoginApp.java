package com.example.andreea.login;

/**
 * Created by Andreea on 11.11.2016.
 */

import android.app.Application;
import android.util.Log;

import com.example.andreea.login.net.NoteRestClient;
import com.example.andreea.login.net.NoteSocketClient;
import com.example.andreea.login.service.NoteManager;

public class LoginApp extends Application {
    public static final String TAG = LoginApp.class.getSimpleName();
    private NoteManager mNoteManager;
    private NoteRestClient mNoteRestClient;
    private NoteSocketClient mNoteSocketClient;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        mNoteManager = new NoteManager(this);
        mNoteRestClient = new NoteRestClient(this);
        mNoteSocketClient = new NoteSocketClient(this);
        mNoteManager.setNoteRestClient(mNoteRestClient);
        mNoteManager.setNoteSocketClient(mNoteSocketClient);
    }

    public NoteManager getNoteManager() {
        return mNoteManager;
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }
}