package com.github.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThymeleafController {


    @RequestMapping(value = "/thymeleaf/index")
    public String index(Model model) {
        return "/index";
    }

    @RequestMapping(value = "/thymeleaf/messaging")
    public String messaging(Model model) {
        return "/nav/messaging";
    }
}
