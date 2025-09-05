package com.programacionNCapas.SReynaProgramacionNCapas.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("Demo")
public class DemoController {

    @GetMapping("HolaMundo")
    public String HolaMundo() {
        return "HolaMundo";
    }

    @GetMapping("/persona/{persona}")
    public String Persona(@PathVariable String persona, Model model) {
        model.addAttribute("persona", persona);
        return "HolaMundo";
    }

    @GetMapping("/lugar")
    public String Lugar(@RequestParam(name = "lugar", required = false, defaultValue = "lugar") String lugar, Model model) {
        model.addAttribute("lugar", lugar);
        return "HolaMundo";
    }

}
