package io.dt3zr.mockashopweb.controller;

import io.dt3zr.mockashopweb.viewmodel.Product;
import io.dt3zr.mockashopweb.viewmodel.ProductForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class ProductAdminController {
    private static final Logger log = LoggerFactory.getLogger(ProductAdminController.class);
    private RestTemplate restTemplate;

    public ProductAdminController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/product/form")
    public String productForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "product-form";
    }

    @PostMapping("/product/form")
    public String addProduct(Model model, @Valid ProductForm productForm, BindingResult result) {
        log.info("Adding product {}", productForm);

        if (result.hasErrors()) {
            return "product-form";
        }

        restTemplate.postForObject("http://localhost:8081/product-catalog", productForm, Void.class);
        model.addAttribute("productFrom", new ProductForm());
        return "product-form";
    }
}
