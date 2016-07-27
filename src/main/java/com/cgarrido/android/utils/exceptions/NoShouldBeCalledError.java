package com.cgarrido.android.utils.exceptions;

/**
 * Created by cristian on 07/12/2015.
 */
public class NoShouldBeCalledError extends IllegalAccessError {
    public NoShouldBeCalledError(String message) {
        super(message);
    }
}
