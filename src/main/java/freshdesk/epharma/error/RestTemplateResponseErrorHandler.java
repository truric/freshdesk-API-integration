package freshdesk.epharma.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;


import java.io.IOException;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatusCode statusCode = httpResponse.getStatusCode();
        return (statusCode.is4xxClientError() || statusCode.is5xxServerError());
    }


    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatusCode statusCode = httpResponse.getStatusCode();
        if (statusCode.is5xxServerError()) {
            throw new HttpClientErrorException(statusCode);
        } else if (statusCode.is4xxClientError()) {
            if (statusCode == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Resource not found");
            }
            throw new HttpClientErrorException(statusCode);
        }
    }
}
