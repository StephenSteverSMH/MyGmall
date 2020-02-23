package com.stever.gmall.password.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PasswordController {
    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
