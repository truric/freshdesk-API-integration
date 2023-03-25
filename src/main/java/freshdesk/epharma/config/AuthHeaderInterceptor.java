package freshdesk.epharma.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Base64;

public class AuthHeaderInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    public AuthHeaderInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((apiKey + ":X").getBytes()));
        return execution.execute(request, body);
    }
}
