package com.example.andreea.login.net.mapping;

/**
 * Created by Andreea on 11.11.2016.
 */

import org.json.JSONObject;

import static com.example.andreea.login.net.mapping.Api.Note._ID;

public class IdJsonObjectReader implements ResourceReader<String, JSONObject> {
    private static final String TAG = IdJsonObjectReader.class.getSimpleName();

    @Override
    public String read(JSONObject obj) throws Exception {
        return obj.getString(_ID);
    }
}