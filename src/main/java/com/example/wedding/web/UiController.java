package com.example.wedding.web;

import com.example.wedding.service.WeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UiController {

    private final WeddingService service;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("prestations", service.prestations());
        model.addAttribute("scenarios", service.scenarios());
        // Les items/totaux seront récupérés via fetch côté JS pour éviter de recharger toute la page.
        return "index";
    }
}
