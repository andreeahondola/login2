package com.example.andreea.login.net.mapping;

/**
 * Created by Andreea on 11.11.2016.
 */

import android.util.JsonReader;
import android.util.Log;

import com.example.andreea.login.content.Note;

import org.json.JSONObject;

import java.io.IOException;

import static com.example.andreea.login.net.mapping.Api.Note.STATUS;
import static com.example.andreea.login.net.mapping.Api.Note.TEXT;
import static com.example.andreea.login.net.mapping.Api.Note.UPDATED;
import static com.example.andreea.login.net.mapping.Api.Note.USER_ID;
import static com.example.andreea.login.net.mapping.Api.Note.VERSION;
import static com.example.andreea.login.net.mapping.Api.Note._ID;

public class NoteReader implements ResourceReader<Note, JsonReader> {
    private static final String TAG = NoteReader.class.getSimpleName();

    @Override
    public Note read(JsonReader reader) throws IOException {
        Note note = new Note();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(_ID)) {
                note.setId(reader.nextString());
            } else if (name.equals(TEXT)) {
                note.setText(reader.nextString());
            } else if (name.equals(STATUS)) {
                note.setStatus(Note.Status.valueOf(reader.nextString()));
            } else if (name.equals(UPDATED)) {
                note.setUpdated(reader.nextLong());
            } else if (name.equals(USER_ID)) {
                note.setUserId(reader.nextString());
            } else if (name.equals(VERSION)) {
                note.setVersion(reader.nextInt());
            } else {
                reader.skipValue();
                Log.w(TAG, String.format("Note property '%s' ignored", name));
            }
        }
        reader.endObject();
        return note;
    }
}
