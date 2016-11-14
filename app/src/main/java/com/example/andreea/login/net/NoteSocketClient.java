package com.example.andreea.login.net;

/**
 * Created by Andreea on 11.11.2016.
 */

import android.content.Context;
import android.util.Log;

import com.example.andreea.login.R;
import com.example.andreea.login.content.Note;
import com.example.andreea.login.net.mapping.IdJsonObjectReader;
import com.example.andreea.login.net.mapping.NoteJsonObjectReader;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.andreea.login.net.mapping.Api.Note.NOTE_CREATED;
import static com.example.andreea.login.net.mapping.Api.Note.NOTE_DELETED;
import static com.example.andreea.login.net.mapping.Api.Note.NOTE_UPDATED;

public class NoteSocketClient {
    private static final String TAG = NoteSocketClient.class.getSimpleName();
    private final Context mContext;
    private Socket mSocket;
    private ResourceChangeListener<Note> mResourceListener;

    public NoteSocketClient(Context context) {
        mContext = context;
        Log.d(TAG, "created");
    }

    public void subscribe(final ResourceChangeListener<Note> resourceListener) {
        Log.d(TAG, "subscribe");
        mResourceListener = resourceListener;
        try {
            mSocket = IO.socket(mContext.getString(R.string.api_url));
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "socket connected");
                }
            });
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "socket disconnected");
                }
            });
            mSocket.on(NOTE_CREATED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        Note note = new NoteJsonObjectReader().read((JSONObject) args[0]);
                        Log.d(TAG, String.format("note created %s", note.toString()));
                        mResourceListener.onCreated(note);
                    } catch (Exception e) {
                        Log.w(TAG, "note created", e);
                        mResourceListener.onError(new ResourceException(e));
                    }
                }
            });
            mSocket.on(NOTE_UPDATED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        Note note = new NoteJsonObjectReader().read((JSONObject) args[0]);
                        Log.d(TAG, String.format("note updated %s", note.toString()));
                        mResourceListener.onUpdated(note);
                    } catch (Exception e) {
                        Log.w(TAG, "note updated", e);
                        mResourceListener.onError(new ResourceException(e));
                    }
                }
            });
            mSocket.on(NOTE_DELETED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        String id = new IdJsonObjectReader().read((JSONObject) args[0]);
                        Log.d(TAG, String.format("note deleted %s", id));
                        mResourceListener.onDeleted(id);
                    } catch (Exception e) {
                        Log.w(TAG, "note deleted", e);
                        mResourceListener.onError(new ResourceException(e));
                    }
                }
            });
            mSocket.connect();
        } catch (Exception e) {
            Log.w(TAG, "socket error", e);
            mResourceListener.onError(new ResourceException(e));
        }
    }

    public void unsubscribe() {
        Log.d(TAG, "unsubscribe");
        if (mSocket != null) {
            mSocket.disconnect();
        }
        mResourceListener = null;
    }

}

