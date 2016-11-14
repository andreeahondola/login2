package com.example.andreea.login.net.mapping;

/**
 * Created by Andreea on 11.11.2016.
 */

import org.json.JSONException;

import java.io.IOException;

public interface ResourceReader<E, Reader> {
    E read(Reader reader) throws IOException, JSONException, Exception;
}
