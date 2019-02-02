package com.trembleturn.trembleturn.POJO;

public class Errors {
    String Message, Exception, ExceptionMessage, StackStrace;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getException() {
        return Exception;
    }

    public void setException(String exception) {
        Exception = exception;
    }

    public String getExceptionMessage() {
        return ExceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        ExceptionMessage = exceptionMessage;
    }

    public String getStackStrace() {
        return StackStrace;
    }

    public void setStackStrace(String stackStrace) {
        StackStrace = stackStrace;
    }
}
