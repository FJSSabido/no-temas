package com.example.no_temas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/biblia")
public class BibliaController {

    private final BuscadorBibliaService service;

    public BibliaController(BuscadorBibliaService service) {
        this.service = service;
    }

    @GetMapping("/buscar")
    public List<VersiculoEncontrado> buscar(
            @RequestParam String frase, @RequestParam Integer page) {
        return service.buscar(frase, page);
    }
}
