package com.mario.serranouruena.develop.mockito;

import com.mario.serranouruena.develop.mockito.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays
            .asList(new Examen(5L, "Matematicas"),
                    new Examen(6L, "Lenguaje"),
                    new Examen(7L, "Historia"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays
            .asList(new Examen(null, "Matematicas"),
                    new Examen(null, "Lenguaje"),
                    new Examen(null, "Historia"));

    public final static List<Examen> EXAMENES_IDS_NEGATICVOS = Arrays
            .asList(new Examen(-5L, "Matematicas"),
                    new Examen(-6L, "Lenguaje"),
                    new Examen(-7L, "Historia"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmetica", "derivadas", "integrales", "trigonometria", "geometria");

    public final static Examen EXAMEN = new Examen(null, "Fisica");

}
