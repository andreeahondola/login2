package com.example.andreea.login.service;

/**
 * Created by Andreea on 11.11.2016.
 */

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import com.example.andreea.login.content.Note;
import com.example.andreea.login.content.User;
import com.example.andreea.login.content.database.LoginDatabase;
import com.example.andreea.login.net.LastModifiedList;
import com.example.andreea.login.net.NoteRestClient;
import com.example.andreea.login.net.NoteSocketClient;
import com.example.andreea.login.net.ResourceChangeListener;
import com.example.andreea.login.net.ResourceException;
import com.example.andreea.login.util.Cancellable;
import com.example.andreea.login.util.CancellableCallable;
import com.example.andreea.login.util.OnErrorListener;
import com.example.andreea.login.util.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NoteManager extends Observable {
    private static final String TAG = NoteManager.class.getSimpleName();
    private final LoginDatabase mKD;

    private ConcurrentMap<String, Note> mNotes = new ConcurrentHashMap<String, Note>();
    private String mNotesLastUpdate;

    private final Context mContext;
    private NoteRestClient mNoteRestClient;
    private NoteSocketClient mNoteSocketClient;
    private String mToken;
    private User mCurrentUser;

    public NoteManager(Context context) {
        mContext = context;
        mKD = new LoginDatabase(context);
    }

    public CancellableCallable<LastModifiedList<Note>> getNotesCall() {
        Log.d(TAG, "getNotesCall");
        return mNoteRestClient.search(mNotesLastUpdate);
    }

    public List<Note> executeNotesCall(CancellableCallable<LastModifiedList<Note>> getNotesCall) throws Exception {
        Log.d(TAG, "execute getNotes...");
        LastModifiedList<Note> result = getNotesCall.call();
        List<Note> notes = result.getList();
        if (notes != null) {
            mNotesLastUpdate = result.getLastModified();
            updateCachedNotes(notes);
            notifyObservers();
        }
        return cachedNotesByUpdated();
    }

    public NoteLoader getNoteLoader() {
        Log.d(TAG, "getNoteLoader...");
        return new NoteLoader(mContext, this);
    }

    public void setNoteRestClient(NoteRestClient noteRestClient) {
        mNoteRestClient = noteRestClient;
    }

    public Cancellable getNotesAsync(final OnSuccessListener<List<Note>> successListener, OnErrorListener errorListener) {
        Log.d(TAG, "getNotesAsync...");
        return mNoteRestClient.searchAsync(mNotesLastUpdate, new OnSuccessListener<LastModifiedList<Note>>() {

            @Override
            public void onSuccess(LastModifiedList<Note> result) {
                Log.d(TAG, "getNotesAsync succeeded");
                List<Note> notes = result.getList();
                if (notes != null) {
                    mNotesLastUpdate = result.getLastModified();
                    updateCachedNotes(notes);
                }
                successListener.onSuccess(cachedNotesByUpdated());
                notifyObservers();
            }
        }, errorListener);
    }

    public Cancellable getNoteAsync(
            final String noteId,
            final OnSuccessListener<Note> successListener,
            final OnErrorListener errorListener) {
        Log.d(TAG, "getNoteAsync...");
        return mNoteRestClient.readAsync(noteId, new OnSuccessListener<Note>() {

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "getNoteAsync succeeded");
                if (note == null) {
                    setChanged();
                    mNotes.remove(noteId);
                } else {
                    if (!note.equals(mNotes.get(note.getId()))) {
                        setChanged();
                        mNotes.put(noteId, note);
                    }
                }
                successListener.onSuccess(note);
                notifyObservers();
            }
        }, errorListener);
    }

    public Cancellable saveNoteAsync(
            final Note note,
            final OnSuccessListener<Note> successListener,
            final OnErrorListener errorListener) {
        Log.d(TAG, "saveNoteAsync...");
        return mNoteRestClient.updateAsync(note, new OnSuccessListener<Note>() {

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "saveNoteAsync succeeded");
                mNotes.put(note.getId(), note);
                successListener.onSuccess(note);
                setChanged();
                notifyObservers();
            }
        }, errorListener);
    }

    public void subscribeChangeListener() {
        mNoteSocketClient.subscribe(new ResourceChangeListener<Note>() {
            @Override
            public void onCreated(Note note) {
                Log.d(TAG, "changeListener, onCreated");
                ensureNoteCached(note);
            }

            @Override
            public void onUpdated(Note note) {
                Log.d(TAG, "changeListener, onUpdated");
                ensureNoteCached(note);
            }

            @Override
            public void onDeleted(String noteId) {
                Log.d(TAG, "changeListener, onDeleted");
                if (mNotes.remove(noteId) != null) {
                    setChanged();
                    notifyObservers();
                }
            }

            private void ensureNoteCached(Note note) {
                if (!note.equals(mNotes.get(note.getId()))) {
                    Log.d(TAG, "changeListener, cache updated");
                    mNotes.put(note.getId(), note);
                    setChanged();
                    notifyObservers();
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "changeListener, error", t);
            }
        });
    }

    public void unsubscribeChangeListener() {
        mNoteSocketClient.unsubscribe();
    }

    public void setNoteSocketClient(NoteSocketClient noteSocketClient) {
        mNoteSocketClient = noteSocketClient;
    }

    private void updateCachedNotes(List<Note> notes) {
        Log.d(TAG, "updateCachedNotes");
        for (Note note : notes) {
            mNotes.put(note.getId(), note);
        }
        setChanged();
    }

    private List<Note> cachedNotesByUpdated() {
        ArrayList<Note> notes = new ArrayList<>(mNotes.values());
        Collections.sort(notes, new NoteByUpdatedComparator());
        return notes;
    }

    public List<Note> getCachedNotes() {
        return cachedNotesByUpdated();
    }

    public Cancellable loginAsync(
            String username, String password,
            final OnSuccessListener<String> successListener,
            final OnErrorListener errorListener) {
        final User user = new User(username, password);
        return mNoteRestClient.getToken(
                user, new OnSuccessListener<String>() {

                    @Override
                    public void onSuccess(String token) {
                        mToken = token;
                        if (mToken != null) {
                            user.setToken(mToken);
                            setCurrentUser(user);
                            mKD.saveUser(user);
                            successListener.onSuccess(mToken);
                        } else {
                            errorListener.onError(new ResourceException(new IllegalArgumentException("Invalid credentials")));
                        }
                    }
                }, errorListener);
    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
        mNoteRestClient.setUser(currentUser);
    }

    public User getCurrentUser() {
        return mKD.getCurrentUser();
    }

    private class NoteByUpdatedComparator implements java.util.Comparator<Note> {
        @Override
        public int compare(Note n1, Note n2) {
            return (int) (n1.getUpdated() - n2.getUpdated());
        }
    }
}

