package io.dt3zr.mockashopweb.controller;

import io.dt3zr.mockashopweb.viewmodel.CartItem;
import io.dt3zr.mockashopweb.viewmodel.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class ProductCatalogController {
    private static final Logger log = LoggerFactory.getLogger(ProductCatalogController.class);
    private RestTemplate restTemplate;

    @Autowired
    public ProductCatalogController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/product-catalog")
    public String getProductCatalogPage(Model model) {
        Product[] products = restTemplate.getForObject("http://localhost:8081/product-catalog", Product[].class);
        model.addAttribute("products", products);
        return "product-catalog";
    }

    @GetMapping("/product/{productCode}")
    public String getProductPage(@PathVariable("productCode") String productCode, Model model) {
        Map<String, Object> params = new HashMap<>();
        params.put("productCode", productCode);

        Product product = restTemplate.getForObject("http://localhost:8081/product-catalog/{productCode}", Product.class, params);
        CartItem cartItem = new CartItem();
        cartItem.setProductCode(product.getCode());
        model.addAttribute("product", product);
        model.addAttribute("cartItem", cartItem);

        log.info("Received product {}", product);
        return "product";
    }

}
