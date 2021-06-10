package io.github.PiotrGamorski.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
public class IndexController {

    @GetMapping(value = "/")
    String showIndex(){
        return "mainPage";
    }
}
