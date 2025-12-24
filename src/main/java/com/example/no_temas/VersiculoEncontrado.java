package com.example.no_temas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VersiculoEncontrado {
    private int capitulo;
    private int versiculo;
    private int numeroLibro;
    private String libro;
    private String texto;
    private String textoResaltado;

}
