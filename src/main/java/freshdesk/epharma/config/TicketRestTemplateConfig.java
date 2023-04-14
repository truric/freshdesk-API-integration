package freshdesk.epharma.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@Configuration
public class TicketRestTemplateConfig {

    @Value("${freshdesk.api.key}")
    private String API_KEY;
    @Value("${freshdesk.url.main}")
    private String MAIN_URL;

//    @Bean
//    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
//        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
//        messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));
//        return messageConverter;
//    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
//        restTemplate.getMessageConverters().add(jacksonMessageConverter());
        restTemplate.setInterceptors(Collections.singletonList(new AuthHeaderInterceptor(API_KEY)));

        // create Ticket with attachments message converters
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        messageConverters.add(new ByteArrayHttpMessageConverter());
//        messageConverters.add(new FormHttpMessageConverter());
//        messageConverters.add(new MappingJackson2HttpMessageConverter());
//        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    @Bean
    public HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((API_KEY + ":X").getBytes()));
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
