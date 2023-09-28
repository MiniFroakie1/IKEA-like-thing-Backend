package com.example.ikeabackend.shop;

import com.example.ikeabackend.customer.CustomerService;
import com.example.ikeabackend.products.Product;
import com.example.ikeabackend.products.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ProductService productService;

    @GetMapping("")
    public String viewHomePage(Model model, HttpSession httpSession) {
        Map<Long, Integer> cart = checkCart(httpSession);
        float rebate = checkRebate(httpSession);
        Map<String, Object> attributes = new HashMap<>();

        httpSession.setAttribute("cart", cart);

        attributes.put("allproductslist", productService.getAllProducts());
        attributes.put("allcustomerslist", customerService.getAllCustomers());
        attributes.put("product", new Product());
        attributes.put("cart", cart);
        attributes.put("rebate", rebate);
        model.addAllAttributes(attributes);
        return "shop/index";
    }

    @PostMapping("/addtocart")
    public String addToCart(@ModelAttribute("product") Product product, HttpSession httpSession) {
        System.out.println(product.getId());
        Map<Long, Integer> cart = checkCart(httpSession);
        cart.put(product.getId(), cart.get(product.getId()) + 1);
        httpSession.setAttribute("cart", cart);
        return "redirect:/shop";
    }

    @PostMapping("removefromcart")
    public String removeFromCart(@ModelAttribute("product") Product product, HttpSession httpSession) {
        Map<Long, Integer> cart = checkCart(httpSession);
        if (cart.get(product.getId()) - 1 >= 0) {
            cart.put(product.getId(), cart.get(product.getId()) - 1);
        }
        httpSession.setAttribute("cart", cart);
        return "redirect:/shop";
    }

    private Map<Long, Integer> checkCart(HttpSession httpSession) {
        Map<Long, Integer> cart;
        try {
            if (httpSession.getAttribute("cart") == null) {
                throw new IllegalStateException();
            }
            cart = (Map<Long, Integer>) httpSession.getAttribute("cart");
        } catch (IllegalStateException e) {
            cart = new HashMap<>();
            for (Product product : productService.getAllProducts()) {
                cart.put(product.getId(), 0);
            }
        }
        return cart;
    }

    private float checkRebate(HttpSession httpSession) {
        float rebate;
        try {
            if (customerService.getCustomerById((Long) httpSession.getAttribute("customerId")).isPresent()) {
                throw new Exception();
            }
            rebate = customerService.getCustomerById((Long) httpSession.getAttribute("customerId")).get().getRebate();
        } catch (Exception e) {
            rebate = 0;
        }
        return rebate;
    }
}
