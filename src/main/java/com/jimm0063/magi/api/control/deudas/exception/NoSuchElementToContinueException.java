package com.jimm0063.magi.api.control.deudas.exception;

public class NoSuchElementToContinueException extends Exception {
    public NoSuchElementToContinueException (String param) {
        super("The parameter " + param + " is not present in the request that why the process can't continue");
    }
}
