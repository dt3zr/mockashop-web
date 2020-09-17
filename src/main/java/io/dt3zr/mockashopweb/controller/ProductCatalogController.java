package io.dt3zr.mockashopweb.controller;

import io.dt3zr.mockashopweb.viewmodel.CartItem;
import io.dt3zr.mockashopweb.viewmodel.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class ProductCatalogController {
    private static final Logger log = LoggerFactory.getLogger(ProductCatalogController.class);
    private RestTemplate restTemplate;

    public ProductCatalogController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/product-catalog")
    public String getProductCatalogPage(
            Model model, @RegisteredOAuth2AuthorizedClient("mockashop-api") OAuth2AuthorizedClient authorizedClient) {
        log.info("Principal name: {}", authorizedClient.getPrincipalName());

        HttpEntity<String> httpEntity = prepareHttpEntity(authorizedClient.getAccessToken().getTokenValue());
        ResponseEntity<Product[]> responseEntity =
                restTemplate.exchange("http://localhost:8081/product-catalog", HttpMethod.GET, httpEntity, Product[].class);
        Product[] products = responseEntity.getBody();
        model.addAttribute("products", products);
        return "product-catalog";
    }

    @GetMapping("/product/{productCode}")
    public String getProductPage(
            @PathVariable("productCode") String productCode,
            Model model, @RegisteredOAuth2AuthorizedClient("mockashop-api") OAuth2AuthorizedClient authorizedClient) {

        Map<String, Object> params = new HashMap<>();
        params.put("productCode", productCode);

        HttpEntity<String> httpEntity = prepareHttpEntity(authorizedClient.getAccessToken().getTokenValue());

        ResponseEntity<Product> responseEntity = restTemplate.exchange(
                "http://localhost:8081/product-catalog/{productCode}",
                HttpMethod.GET,
                httpEntity,
                Product.class,
                params);
        Product product = responseEntity.getBody();

        CartItem cartItem = new CartItem();
        cartItem.setProductCode(product.getCode());
        model.addAttribute("product", product);
        model.addAttribute("cartItem", cartItem);

        log.info("Received product {}", product);
        return "product";
    }

    private HttpEntity<String> prepareHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return entity;
    }

}
