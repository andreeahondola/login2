package com.example.andreea.login.net.mapping;

/**
 * Created by Andreea on 11.11.2016.
 */

import android.util.JsonWriter;

import com.example.andreea.login.content.Note;

import java.io.IOException;

import static com.example.andreea.login.net.mapping.Api.Note.STATUS;
import static com.example.andreea.login.net.mapping.Api.Note.TEXT;
import static com.example.andreea.login.net.mapping.Api.Note.UPDATED;
import static com.example.andreea.login.net.mapping.Api.Note.USER_ID;
import static com.example.andreea.login.net.mapping.Api.Note.VERSION;
import static com.example.andreea.login.net.mapping.Api.Note._ID;

public class NoteWriter implements ResourceWriter<Note, JsonWriter>{
    @Override
    public void write(Note note, JsonWriter writer) throws IOException {
        writer.beginObject();
        {
            if (note.getId() != null) {
                writer.name(_ID).value(note.getId());
            }
            writer.name(TEXT).value(note.getText());
            writer.name(STATUS).value(note.getStatus().name());
            if (note.getUpdated() > 0) {
                writer.name(UPDATED).value(note.getUpdated());
            }
            if (note.getUserId() != null) {
                writer.name(USER_ID).value(note.getUserId());
            }
            if (note.getVersion() > 0) {
                writer.name(VERSION).value(note.getVersion());
            }
        }
        writer.endObject();
    }
}