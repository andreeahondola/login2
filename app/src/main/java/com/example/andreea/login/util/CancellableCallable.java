package com.example.andreea.login.util;

/**
 * Created by Andreea on 11.11.2016.
 */

import java.util.concurrent.Callable;

public interface CancellableCallable<E> extends Callable<E>, Cancellable {
}
