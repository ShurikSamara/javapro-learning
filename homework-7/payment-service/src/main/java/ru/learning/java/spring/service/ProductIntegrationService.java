package ru.learning.java.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.learning.java.spring.dto.ProductDto;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.exception.ProductServiceException;

import java.util.List;
import java.util.function.Supplier;

@Service
public class ProductIntegrationService {
  private static final Logger log = LoggerFactory.getLogger(ProductIntegrationService.class);
  private final RestTemplate restTemplate;
  private final String productServiceUrl;

  public ProductIntegrationService(RestTemplate restTemplate, String productServiceUrl) {
    this.restTemplate = restTemplate;
    this.productServiceUrl = productServiceUrl;
  }

  public ProductDto getProductById(Long productId) {
    String url = buildUrl("/api/v1/products/{id}", productId);
    log.debug("Fetching product by ID: {}", productId);

    return executeRequest(url, () ->
      restTemplate.exchange(url, HttpMethod.GET, null, ProductDto.class)
    );
  }

  public List<ProductDto> getProductsByClientId(Long clientId) {
    String url = buildUrl("/api/v1/products/client/{id}", clientId);
    log.debug("Fetching products for client ID: {}", clientId);

    return executeRequest(url, () ->
      restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {})
    );
  }

  private String buildUrl(String path, Object... params) {
    return UriComponentsBuilder.fromUriString(productServiceUrl)
      .path(path)
      .buildAndExpand(params)
      .toUriString();
  }

  private <T> T executeRequest(String url, Supplier<ResponseEntity<T>> requestSupplier) {
    try {
      ResponseEntity<T> response = requestSupplier.get();

      if (response.getStatusCode().is2xxSuccessful()) {
        log.debug("Successfully retrieved data from: {}", url);
        return response.getBody();
      } else {
        throw new ProductServiceException("Unexpected response status: " + response.getStatusCode());
      }
    } catch (ProductNotFoundException | ProductServiceException e) {
      log.warn("Product service error for URL {}: {}", url, e.getMessage());
      throw e;
    } catch (ResourceAccessException e) {
      log.error("Network error while connecting to: {}", url, e);
      throw new ProductServiceException("Unable to connect to product service", e);
    } catch (RestClientException e) {
      log.error("Error calling product service: {}", url, e);
      throw new ProductServiceException("Error calling product service", e);
    }
  }
}