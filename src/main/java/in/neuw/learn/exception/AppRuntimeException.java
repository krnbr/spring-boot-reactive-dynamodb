package in.neuw.learn.exception;


import in.neuw.learn.utils.constants.ErrorCodes;

public class AppRuntimeException extends RuntimeException {

    private ErrorCodes errorCodes = ErrorCodes.INTERNAL_SERVER_ERROR;

    private Object data;

    private Throwable exception;

    public AppRuntimeException(String errorMsg, ErrorCodes errorCodes, Throwable ex) {
        super(errorMsg, ex);
        this.errorCodes = errorCodes;
        this.exception = ex;
    }

    public AppRuntimeException(String errorMsg, Throwable ex) {
        super(errorMsg, ex);
    }

    public AppRuntimeException(String errorMsg, ErrorCodes errorCodes) {
        super(errorMsg);
        this.errorCodes = errorCodes;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public Object getData() {
        return data;
    }

    public Throwable getException() {
        return exception;
    }
}
