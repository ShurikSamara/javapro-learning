package ru.learning.java.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(PaymentServiceProperties.class)
public class ProductIntegrationConfig {
  private static final Logger log = LoggerFactory.getLogger(ProductIntegrationConfig.class);

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder,
                                   PaymentServiceProperties properties,
                                   ProductServiceResponseErrorHandler errorHandler) {
    log.info("Configuring RestTemplate with connection timeout: {}ms, read timeout: {}ms",
      properties.getConnectionTimeout(), properties.getReadTimeout());

    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(properties.getConnectionTimeout());
    requestFactory.setReadTimeout(properties.getReadTimeout());

    return builder.requestFactory(
        () -> new BufferingClientHttpRequestFactory(requestFactory))
      .additionalInterceptors(loggingInterceptor())
      .errorHandler(errorHandler)
      .build();
  }


  @Bean
  public String paymentServiceUrl(PaymentServiceProperties properties) {
    return properties.getProductServiceUrl();
  }

  private ClientHttpRequestInterceptor loggingInterceptor() {
    return (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
      log.debug("Request: {} {}", request.getMethod(), request.getURI());
      log.trace("Request body: {}", new String(body, StandardCharsets.UTF_8));

      ClientHttpResponse response = execution.execute(request, body);

      if (log.isDebugEnabled()) {
        InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
        String responseBody = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
        log.debug("Response status: {}", response.getStatusCode());
        log.trace("Response body: {}", responseBody);
      }
      return response;
    };
  }
}