package com.example.no_temas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class LibroController {

    private final BuscadorBibliaService service;

    public LibroController(BuscadorBibliaService service) {
        this.service = service;
    }

    @GetMapping("/libro/{numeroLibro}")
    public String verLibro(@PathVariable int numeroLibro, Model model) {

        List<VersiculoEncontrado> versiculos =
                service.obtenerLibroCompleto(numeroLibro);

        model.addAttribute("libro", service.obtenerNombreLibro(numeroLibro));
        model.addAttribute("versiculos", versiculos);

        return "libro";
    }
}
