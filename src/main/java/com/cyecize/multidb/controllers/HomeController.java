package com.cyecize.multidb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    @Autowired
    public HomeController() {

    }

    @GetMapping
    public String home() {
        return "index";
    }
}
