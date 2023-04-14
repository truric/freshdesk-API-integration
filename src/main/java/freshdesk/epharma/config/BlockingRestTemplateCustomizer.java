//package freshdesk.epharma.model.TicketUserAccess;
//
//import org.apache.hc.client5.http.config.RequestConfig;
//import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
//import org.apache.hc.core5.util.Timeout;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.client.RestTemplateCustomizer;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {
//    private final Integer maxTotalConnection = 20;
//    private final Integer defaultMaxPerRoute = 20;
//    private final Long connectionRequestTimeOut = 3000L;
//
////    public BlockingRestTemplateCustomizer(@Value("${sfg.httpclient.maxtotalconnection}") Integer maxTotalConnection, @Value("${sfg.httpclient.defaultmaxperRoute}") Integer defaultMaxPerRoute, @Value("${sfg.httpclient.connectionrequesttimeout:180000}") Long connectionRequestTimeOut) {
////        this.maxTotalConnection = maxTotalConnection;
////        this.defaultMaxPerRoute = defaultMaxPerRoute;
////        this.connectionRequestTimeOut = connectionRequestTimeOut;
////    }
//
//    public ClientHttpRequestFactory clientHttpRequestFactory() {
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(maxTotalConnection);
//        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
//        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeOut, TimeUnit.MILLISECONDS).build();
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).setDefaultRequestConfig(requestConfig).build();
//        return new HttpComponentsClientHttpRequestFactory(httpClient);
//    }
//
//    @Override
//    public void customize(RestTemplate restTemplate) {
//        restTemplate.setRequestFactory(this.clientHttpRequestFactory());
//    }
//}