package com.example.ikeabackend.controllers;

import com.example.ikeabackend.models.Product;
import com.example.ikeabackend.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("")
    public String viewHomePage(Model model) {
        model.addAttribute("allproductslist", productService.getAllProducts());
        return "index";
    }

    @GetMapping("addnew")
    public String addNewProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "newproduct";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.createProduct(product);
        return "redirect:/products";
    }

    @GetMapping("update/{id}")
    public String updateProduct(@PathVariable(value = "id") long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "update";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(value = "id") long id, Model model) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
