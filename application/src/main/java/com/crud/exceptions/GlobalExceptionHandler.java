package com.crud.exceptions;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.codec.CodecException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.serde.exceptions.SerdeException;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Singleton
@Slf4j
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {
        return switch (exception) {

            case FunctionNotFoundException e -> handleNotFound(e, request);
            case PositionNotFoundException e -> handleNotFound(e, request);

            case CodecException e -> handleJsonParsingError(request, e);
            case SerdeException e -> handleJsonParsingError(request, e);
            case IOException e -> handleJsonParsingError(request, e);

            default -> handleGenericException(request, exception);
        };
    }

    private HttpResponse<?> handleNotFound(RuntimeException exception, HttpRequest<?> request) {
        log.warn("Not found error: {} - Request: {}", exception.getMessage(), request.getUri());

        return HttpResponse.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    private HttpResponse<?> handleJsonParsingError(HttpRequest<?> request, Exception exception) {
        log.warn("JSON parsing error: {} - Request: {}", exception.getMessage(), request.getUri());

        return HttpResponse.status(HttpStatus.BAD_REQUEST)
                .body("Invalid JSON format.");
    }

    private HttpResponse<?> handleGenericException(HttpRequest<?> request, Exception exception) {
        log.error(
                "Unhandled exception: {} - Message: {} - Request: {}",
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                request.getUri(),
                exception
        );

        return HttpResponse.serverError()
                .body("An unexpected error occurred. Please try again later.");
    }
}
