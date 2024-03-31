package com.chain.chainscrape.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeWithHtmlController {
    //wired from application.properties
    @Value("${homeText1}")
    String welcome1;

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("welcome1", welcome1);
        return "homePage"; //refers homePage.html
    }
}