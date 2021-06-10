package io.github.PiotrGamorski.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
class SingleSignOnController {
    @GetMapping(value = "/logout")
    String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "index";
    }
}
