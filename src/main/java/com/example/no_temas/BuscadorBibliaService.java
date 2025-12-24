package com.example.no_temas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class BuscadorBibliaService {

    private static final int PAGE_SIZE = 30;

    private static final Pattern LINEA_PATTERN =
            Pattern.compile("^\\((\\d+),\\s*(\\d+),\\s*(\\d+),\\s*'(.*)'\\),?$");

    private List<VersiculoEncontrado> buscarTodos(String frase) {
        List<VersiculoEncontrado> resultados = new ArrayList<>();
        String fraseNormalizada = normalizar(frase);

        Pattern palabraExacta = Pattern.compile(
                "\\b" + Pattern.quote(fraseNormalizada) + "\\b"
        );

        File carpeta = new File("src/main/resources/ReinaValera");

        if (!carpeta.exists() || !carpeta.isDirectory()) {
            throw new RuntimeException("Carpeta ReinaValera no encontrada");
        }

        for (File archivo : carpeta.listFiles()) {
            if (archivo.isFile() && archivo.getName().endsWith(".txt")) {
                buscarEnArchivo(archivo, palabraExacta, frase, resultados);
            }
        }

        return resultados;
    }

    public List<VersiculoEncontrado> obtenerLibroCompleto(int numeroLibro) {

        List<VersiculoEncontrado> versiculos = new ArrayList<>();

        File carpeta = new File("src/main/resources/ReinaValera");

        for (File archivo : carpeta.listFiles()) {
            if (!archivo.getName().endsWith(".txt")) continue;

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

                String linea;
                while ((linea = br.readLine()) != null) {

                    Matcher m = LINEA_PATTERN.matcher(linea.trim());
                    if (!m.matches()) continue;

                    int libro = Integer.parseInt(m.group(1));
                    if (libro != numeroLibro) continue;

                    int capitulo = Integer.parseInt(m.group(2));
                    int versiculo = Integer.parseInt(m.group(3));
                    String texto = m.group(4);
                    texto = texto.replace("/n", "\n");

                    versiculos.add(new VersiculoEncontrado(
                            capitulo,
                            versiculo,
                            numeroLibro,
                            obtenerNombreLibro(libro),
                            texto,
                            texto   // sin resaltado aquí
                    ));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return versiculos;
    }

    public List<VersiculoEncontrado> buscar(String frase, int page) {

        List<VersiculoEncontrado> todos = buscarTodos(frase);

        int fromIndex = page * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, todos.size());

        if (fromIndex >= todos.size()) {
            return List.of();
        }

        return todos.subList(fromIndex, toIndex);
    }

    public int totalResultados(String frase) {
        return buscarTodos(frase).size();
    }

    public int totalPaginas(String frase) {
        int total = buscarTodos(frase).size();
        return (int) Math.ceil((double) total / PAGE_SIZE);
    }

    private void buscarEnArchivo(File archivo, Pattern palabraExacta, String frase, List<VersiculoEncontrado> resultados)
    {

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;
            while ((linea = br.readLine()) != null) {

                Matcher m = LINEA_PATTERN.matcher(linea.trim());
                if (!m.matches()) continue;

                int libro = Integer.parseInt(m.group(1));
                int capitulo = Integer.parseInt(m.group(2));
                int versiculo = Integer.parseInt(m.group(3));
                String texto = m.group(4);
                texto = texto.replace("/n", "\n");
                String textoNormalizado = normalizar(texto);

                if (palabraExacta.matcher(textoNormalizado).find()) {

                    String textoResaltado = texto.replaceAll(
                            "(?i)\\b" + Pattern.quote(frase) + "\\b",
                            "<mark>$0</mark>"
                    );

                    VersiculoEncontrado v = new VersiculoEncontrado(
                            capitulo,
                            versiculo,
                            libro,
                            obtenerNombreLibro(libro),
                            texto,
                            textoResaltado
                    );

                    resultados.add(v);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo " + archivo.getName(), e);
        }
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

    public String obtenerNombreLibro(int numeroLibro) {
        return switch (numeroLibro) {
            case 1 -> "Génesis";
            case 2 -> "Éxodo";
            case 3 -> "Levítico";
            case 4 -> "Números";
            case 5 -> "Deuteronomio";
            case 6 -> "Josué";
            case 7 -> "Jueces";
            case 8 -> "Rut";
            case 9 -> "1 Samuel";
            case 10 -> "2 Samuel";
            case 11 -> "1 Reyes";
            case 12 -> "2 Reyes";
            case 13 -> "1 Crónicas";
            case 14 -> "2 Crónicas";
            case 15 -> "Esdras";
            case 16 -> "Nehemías";
            case 17 -> "Ester";
            case 18 -> "Job";
            case 19 -> "Salmos";
            case 20 -> "Proverbios";
            case 21 -> "Eclesiastés";
            case 22 -> "Cantares";
            case 23 -> "Isaías";
            case 24 -> "Jeremías";
            case 25 -> "Lamentaciones";
            case 26 -> "Ezequiel";
            case 27 -> "Daniel";
            case 28 -> "Oseas";
            case 29 -> "Joel";
            case 30 -> "Amós";
            case 31 -> "Abdías";
            case 32 -> "Jonás";
            case 33 -> "Miqueas";
            case 34 -> "Nahúm";
            case 35 -> "Habacuc";
            case 36 -> "Sofonías";
            case 37 -> "Hageo";
            case 38 -> "Zacarías";
            case 39 -> "Malaquías";
            case 40 -> "Mateo";
            case 41 -> "Marcos";
            case 42 -> "Lucas";
            case 43 -> "Juan";
            case 44 -> "Hechos";
            case 45 -> "Romanos";
            case 46 -> "1 Corintios";
            case 47 -> "2 Corintios";
            case 48 -> "Gálatas";
            case 49 -> "Efesios";
            case 50 -> "Filipenses";
            case 51 -> "Colosenses";
            case 52 -> "1 Tesalonicenses";
            case 53 -> "2 Tesalonicenses";
            case 54 -> "1 Timoteo";
            case 55 -> "2 Timoteo";
            case 56 -> "Tito";
            case 57 -> "Filemón";
            case 58 -> "Hebreos";
            case 59 -> "Santiago";
            case 60 -> "1 Pedro";
            case 61 -> "2 Pedro";
            case 62 -> "1 Juan";
            case 63 -> "2 Juan";
            case 64 -> "3 Juan";
            case 65 -> "Judas";
            case 66 -> "Apocalipsis";
            default -> "Libro " + numeroLibro;
        };
    }
}
