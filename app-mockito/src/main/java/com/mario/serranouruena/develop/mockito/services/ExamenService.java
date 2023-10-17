package com.mario.serranouruena.develop.mockito.services;

import com.mario.serranouruena.develop.mockito.models.Examen;

import java.util.Optional;

public interface ExamenService {

    Optional<Examen> findExamenByName(String nombre);
    Examen findExamenPorNombreConPreguntas(String nombre);

    Examen guardar(Examen examen);

}
