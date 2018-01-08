package com.gym.app.parts.authentication.register;

/**
 * @author catalinradoiu
 * @since 2017.11.15
 */

interface RegisterView {

    enum RegisterErrorType{
        EMAIL_IN_USE_ERROR, CONNECTION_ERROR
    }

    void displayRegistrationSuccess();

    void displayRegistrationError(RegisterErrorType errorType);
}
