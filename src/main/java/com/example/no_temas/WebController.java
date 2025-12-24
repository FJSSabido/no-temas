package com.example.no_temas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final BuscadorBibliaService service;

    public WebController(BuscadorBibliaService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam String palabra,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        model.addAttribute("palabra", palabra);
        model.addAttribute("resultados", service.buscar(palabra, page));
        model.addAttribute("page", page);
        model.addAttribute("totalPaginas", service.totalPaginas(palabra));

        // 👉 AÑADIR ESTO
        model.addAttribute("totalResultados", service.totalResultados(palabra));

        return "resultado";
    }

}
