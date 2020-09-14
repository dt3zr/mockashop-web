package io.dt3zr.mockashopweb.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ApiBindingException extends RuntimeException {
    private final String error;
    private final String statusCode;

    public ApiBindingException(String error, String statusCode) {
        super(String.format("%s : %s", statusCode, error));
        this.error = error;
        this.statusCode = statusCode;
    }
}
