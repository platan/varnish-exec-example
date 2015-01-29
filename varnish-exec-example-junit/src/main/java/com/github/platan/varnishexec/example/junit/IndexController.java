package com.github.platan.varnishexec.example.junit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @RequestMapping("/header")
    public String header() {
        return "header";
    }

    @RequestMapping("/")
    public String index(HttpServletResponse response) {
        response.setHeader("Surrogate-Control", "ESI/1.0");
        return "index";
    }

    @RequestMapping("/footer")
    public String footer() {
        return "footer";
    }

}