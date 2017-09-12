package com.satuh;

/**
 * Created by User on 9/11/2017.
 */

public class SatuhError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int mErrorCode = 0;
    private String mErrorType;

    public SatuhError(String message) {
        super(message);
    }

    public SatuhError(String message, String type, int code) {
        super(message);
        mErrorType = type;
        mErrorCode = code;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorType() {
        return mErrorType;
    }

}