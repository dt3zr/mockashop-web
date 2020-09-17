package io.dt3zr.mockashopweb.controller;

import io.dt3zr.mockashopweb.viewmodel.Order;
import io.dt3zr.mockashopweb.viewmodel.OrderEvent;
import io.dt3zr.mockashopweb.viewmodel.OrderSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private RestTemplate restTemplate;

    @Autowired
    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/order")
    public String checkout(Model model, @AuthenticationPrincipal OAuth2User oAuth2User,
                           @RegisteredOAuth2AuthorizedClient("mockashop-api") OAuth2AuthorizedClient authorizedClient) {
        OrderEvent orderEvent = new OrderEvent(oAuth2User.getName());
        log.info("Order checking out for {}", orderEvent.getUsername());
        HttpEntity<OrderEvent> httpEntity = prepareHttpEntity(orderEvent, authorizedClient.getAccessToken().getTokenValue());
        ResponseEntity<Order> responseEntity =
                restTemplate.exchange("http://localhost:8081/order", HttpMethod.POST, httpEntity, Order.class);
        Order order = responseEntity.getBody();
//        Order order = restTemplate.postForObject("http://localhost:8081/order", orderEvent, Order.class);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", order.getOrderItems());
        return "order";
    }

    @GetMapping("/order")
    public String getOrderHistory(Model model, @AuthenticationPrincipal OAuth2User oAuth2User,
                                  @RegisteredOAuth2AuthorizedClient("mockashop-api") OAuth2AuthorizedClient authorizedClient) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", oAuth2User.getName());
        HttpEntity<String> httpEntity = prepareHttpEntity("", authorizedClient.getAccessToken().getTokenValue());
        ResponseEntity<OrderSummary[]> responseEntity =
                restTemplate.exchange("http://localhost:8081/order/user/{username}", HttpMethod.GET, httpEntity, OrderSummary[].class, params);
//        OrderSummary[] orderSummaries = restTemplate.getForObject("http://localhost:8081/order/user/{username}", OrderSummary[].class, params);
        OrderSummary[] orderSummaries = responseEntity.getBody();
        model.addAttribute("orderSummaries", orderSummaries);
        return "order-history";
    }

    @GetMapping("/order/{transactionId}")
    public String getOrderDetails(Model model, @PathVariable String transactionId,
                                  @RegisteredOAuth2AuthorizedClient("mockashop-api") OAuth2AuthorizedClient authorizedClient) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transactionId);
        HttpEntity<String> httpEntity = prepareHttpEntity("", authorizedClient.getAccessToken().getTokenValue());
        ResponseEntity<Order> responseEntity =
                restTemplate.exchange("http://localhost:8081/order/transaction/{transactionId}", HttpMethod.GET, httpEntity, Order.class, params);
//        Order order = restTemplate.getForObject("http://localhost:8081/order/transaction/{transactionId}", Order.class, params);
        Order order = responseEntity.getBody();
        model.addAttribute("order", order);
        model.addAttribute("orderItems", order.getOrderItems());
        return "order";
    }

    private <T> HttpEntity<T> prepareHttpEntity(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<T> entity = new HttpEntity<T>(body, headers);
        return entity;
    }
}
