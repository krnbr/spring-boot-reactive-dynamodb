package in.neuw.learn.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.neuw.learn.exception.AppRuntimeException;
import in.neuw.learn.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @author Karanbir Singh on 04/29/2020
 */
@Component
@Order(-2)
public class ApisExceptionHandler implements WebExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(ApisExceptionHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof AppRuntimeException appRuntimeException) {

            logger.info("errors:" + appRuntimeException.getMessage());
            var errorResponse = new ErrorResponse()
                    .setMessage(appRuntimeException.getMessage())
                    .setStatus(appRuntimeException.getErrorCodes().getCode())
                    .setCorrelationId(exchange.getAttribute("correlation-id"));

            if (appRuntimeException.getException() != null) {
                errorResponse.setException(appRuntimeException.getException().getClass().getSimpleName());
            }

            if (appRuntimeException.getData() != null) {
                errorResponse.setData(appRuntimeException.getData());
            }

            try {
                exchange.getResponse().setStatusCode(appRuntimeException.getErrorCodes().getStatus());
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                var db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));

                // write the given data buffer to the response
                // and return a Mono that signals when it's done
                return exchange.getResponse().writeWith(Mono.just(db));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Mono.empty();
            }
        } else if(ex instanceof WebExchangeBindException) {
            FieldError fieldError = ((WebExchangeBindException) ex).getFieldErrors().get(0);
            logger.error("WebExchangeBindException for the field - "+fieldError.getField()+" with correlation-id "+exchange.getAttribute("correlation-id"));
            var errorResponse = new ErrorResponse()
                    //.setMessage("Field '"+((WebExchangeBindException) ex).getFieldErrors().get(0).getDefaultMessage()+"' has some issues!")
                    .setMessage(fieldError.getField()+": "+fieldError.getDefaultMessage())
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .setCorrelationId(exchange.getAttribute("correlation-id"));
            try {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                var db = new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));

                return exchange.getResponse().writeWith(Mono.just(db));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Mono.empty();
            }
        }
        return Mono.error(ex);
    }

}