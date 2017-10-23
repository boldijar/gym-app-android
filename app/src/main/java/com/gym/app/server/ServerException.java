package com.gym.app.server;

import java.io.IOException;

/**
 * @author Paul
 * @since 2017.08.30
 */

class ServerException extends IOException {
    private final String mMessage;
    private final int mCode;

    ServerException(String message, int code) {
        mMessage = message;
        mCode = code;
    }

    @Override
    public String getMessage() {
        return "Message: " + mMessage + " Code: " + mCode + " " + super.getMessage();
    }
}
