package in.neuw.learn.utils.constants;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {

    OK(200, HttpStatus.OK),
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, HttpStatus.FORBIDDEN),
    NOT_FOUND(404, HttpStatus.NOT_FOUND),
    NOT_ACCEPTABLE(406, HttpStatus.NOT_ACCEPTABLE),
    CONFLICT(409, HttpStatus.CONFLICT),
    GONE(410, HttpStatus.GONE),
    UNPROCESSABLE_ENTITY(422, HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR),
    OPERATION_NOT_IMPLEMENTED(501, HttpStatus.NOT_IMPLEMENTED),
    DOWNSTREAM_SERVICE_UNAVAILABLE(501, HttpStatus.SERVICE_UNAVAILABLE);

    private int code;
    private HttpStatus status;
    private SystemType systemType = SystemType.APIS;

    ErrorCodes(int code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    ErrorCodes(int code, HttpStatus status, SystemType systemType) {
        this.code = status.value();
        this.status = status;
        this.systemType = systemType;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public SystemType getSystemType() {
        return systemType;
    }
}