package in.neuw.learn.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Karanbir Singh on 04/29/2020
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Integer status;

    private String message;

    private String exception;

    private Object data;

    private ZonedDateTime time;
    @JsonProperty("correlation-id")

    private String correlationId;

    public Integer getStatus() {
        return status;
    }

    public ErrorResponse setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getException() {
        return exception;
    }

    public ErrorResponse setException(String exception) {
        this.exception = exception;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ErrorResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public ErrorResponse setTime(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public ErrorResponse setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public ErrorResponse() {
        this.time = ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.of("Asia/Kolkata"));
    }

}