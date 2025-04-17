package com.root37.buflexz.core.network.exception;

/**
 * Created by j2n on 2016. 10. 19..
 */

public class UnknownException extends Exception{

    public UnknownException(String message) {
        super(message);
    }

    public UnknownException() {
        super("sorry, UnknownException..");
    }

}
