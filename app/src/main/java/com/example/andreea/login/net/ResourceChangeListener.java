package com.example.andreea.login.net;

/**
 * Created by Andreea on 11.11.2016.
 */

public interface ResourceChangeListener<E> {
    void onCreated(E e);
    void onUpdated(E e);
    void onDeleted(String id);
    void onError(Throwable t);
}
