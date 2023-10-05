package com.example.ikeabackend.shop;

import com.example.ikeabackend.customer.CustomerService;
import com.example.ikeabackend.products.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ProductService productService;

    @GetMapping({"", "/"})
    public String viewHomePage(Model model, Long... user) {
        float rebate;
        Map<String, Object> attributes = new HashMap<>();

        try {
            attributes.put("user", user[0]);
            rebate = checkRebate(user[0]);
        } catch (Exception e) {
            attributes.put("user", null);
            rebate = 0;
        }

        attributes.put("allproductslist", productService.getAllProducts());
        attributes.put("allcustomerslist", customerService.getAllCustomers());
        attributes.put("rebate", rebate);
        model.addAllAttributes(attributes);
        return "shop/index";
    }

    @GetMapping("/{user}")
    public String selectUser(Model model, @PathVariable Long user) {
        return viewHomePage(model, user);
    }

    @GetMapping("/none")
    public String redirectView() {
        return "redirect:viewHomePage";
    }

    private float checkRebate(Long id) {
        float rebate;
        try {
            if (customerService.getCustomerById(id).isEmpty()) {
                throw new Exception();
            }
            rebate = customerService.getCustomerById(id).get().getRebate();
        } catch (Exception e) {
            rebate = 0;
        }
        return rebate;
    }
}
