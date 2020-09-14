package io.dt3zr.mockashopweb.controller;

import io.dt3zr.mockashopweb.api.ApiBindingException;
import io.dt3zr.mockashopweb.viewmodel.CartItem;
import io.dt3zr.mockashopweb.viewmodel.ShoppingCartItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class ShoppingCartController {
    private static final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    private RestTemplate restTemplate;

    public ShoppingCartController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/shopping-cart")
    public String addCartItem(CartItem cartItem, @AuthenticationPrincipal OAuth2User oAuth2User) {
        log.info("Put cart item {} to shopping cart for {}", cartItem, oAuth2User.getName());
        cartItem.setQuantity(1);
        cartItem.setUsername(oAuth2User.getName());
        restTemplate.postForObject("http://localhost:8081/shopping-cart", cartItem, Void.class);
        return "redirect:product-catalog";
    }

    @GetMapping("/shopping-cart")
    public String getCheckoutPage(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        log.info("About to checkout for {}", oAuth2User.getName());
        Map<String, Object> params = new HashMap<>();
        params.put("username", oAuth2User.getName());

        try {
            ShoppingCartItems shoppingCartItems =
                    restTemplate.getForObject("http://localhost:8081/shopping-cart/{username}", ShoppingCartItems.class, params);
            log.info("Retrieved shopping cart {}", shoppingCartItems);
            model.addAttribute("shoppingCartItems", shoppingCartItems.getShoppingCartItems());
            model.addAttribute("username", oAuth2User.getName());
            model.addAttribute("errorMessage", null);
        } catch (ApiBindingException exception) {
            model.addAttribute("errorMessage", "Shopping cart is empty.");
        }
        return "shopping-cart";
    }

}
