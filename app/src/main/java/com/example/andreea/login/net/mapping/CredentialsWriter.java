package com.example.andreea.login.net.mapping;

/**
 * Created by Andreea on 11.11.2016.
 */

import android.util.JsonWriter;

import com.example.andreea.login.content.User;

import java.io.IOException;

import static com.example.andreea.login.net.mapping.Api.Auth.PASSWORD;
import static com.example.andreea.login.net.mapping.Api.Auth.USERNAME;

public class CredentialsWriter implements ResourceWriter<User, JsonWriter> {
    @Override
    public void write(User user, JsonWriter writer) throws IOException {
        writer.beginObject();
        {
            writer.name(USERNAME).value(user.getUsername());
            writer.name(PASSWORD).value(user.getPassword());
        }
        writer.endObject();
    }
}