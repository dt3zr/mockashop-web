package io.dt3zr.mockashopweb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ApiErrorResponseHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            try (BufferedInputStream buffer = new BufferedInputStream(response.getBody())) {
                ObjectMapper mapper = new ObjectMapper();
                ErrorMessage errorMessage = mapper.readValue(buffer, ErrorMessage.class);
                throw new ApiBindingException(errorMessage.getError(), errorMessage.getStatus().name());
            }
        }
    }
}
