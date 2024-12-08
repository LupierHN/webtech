package de.htwberlin.webtech.webtech.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SsrController {
    @GetMapping(path = "/")
    public ModelAndView showSsrPage(Model model){
        model.addAttribute("someData", "Value from Controller!");
        return new ModelAndView("template");
    }
}

