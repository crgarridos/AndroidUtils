package com.ylly.android.utils.exceptions;

/**
 * Created by cristian on 02/12/2015.
 */
public class MethodNotImplementedError extends NoSuchMethodError {
    public MethodNotImplementedError(String s) {
        super(s);
    }
    public MethodNotImplementedError() {
        super();
    }
}
