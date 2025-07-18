package ru.learning.java.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.exception.ProductServiceException;

import java.io.IOException;
import java.net.URI;

@Component
public class ProductServiceResponseErrorHandler implements ResponseErrorHandler {

  private static final Logger log = LoggerFactory.getLogger(ProductServiceResponseErrorHandler.class);

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    HttpStatusCode statusCode = response.getStatusCode();
    return statusCode.is4xxClientError() || statusCode.is5xxServerError();
  }

  public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
    HttpStatusCode statusCode = response.getStatusCode();

    log.error("HTTP Error for {} {}: {}", method, url, statusCode.value());

    if (statusCode.value() == HttpStatus.NOT_FOUND.value()) {
      throw new ProductNotFoundException("Product not found at: " + url);
    } else if (statusCode.is5xxServerError()) {
      throw new ProductServiceException("Product service unavailable at: " + url + " (HTTP " + statusCode.value() + ")");
    } else if (statusCode.is4xxClientError()) {
      throw new ProductServiceException("Client error calling: " + url + " (HTTP " + statusCode.value() + ")");
    } else {
      throw new ProductServiceException("Unexpected error calling: " + url + " (HTTP " + statusCode.value() + ")");
    }
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    handleError(URI.create("unknown"), HttpMethod.GET, response);
  }
}
