package com.example.andreea.login.net.mapping;

/**
 * Created by Andreea on 11.11.2016.
 */

import java.io.IOException;

public interface ResourceWriter<E, Writer> {
    void write(E e, Writer writer) throws IOException;
}
