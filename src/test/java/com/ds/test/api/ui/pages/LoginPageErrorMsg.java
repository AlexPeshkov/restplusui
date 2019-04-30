package com.ds.test.api.ui.pages;

public enum LoginPageErrorMsg {

    PASSWORD_ERROR_MIN("Minimum size is 6"),
    PASSWORD_ERROR_MAX("Maximum size is 255"),
    IS_REQUIRED("Required"),
    PASSWORD_NOT_THE_SAME("passwords are not the same"),
    EMAIL_IS_INVALID("Invalid email address"),
    TO_BE_UNIQUE("Must be unique");

    private String message;

    LoginPageErrorMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}