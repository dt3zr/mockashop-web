package io.dt3zr.mockashopweb.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ErrorMessage {
    private String error;
    private HttpStatus status;
}
