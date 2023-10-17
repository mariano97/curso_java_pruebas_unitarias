package com.mario.serranouruena.develop.mockito.repositories;

import com.mario.serranouruena.develop.mockito.models.Examen;

import java.util.List;

public interface ExamenRepository {


    Examen save(Examen examen);

    List<Examen> findAll();

}
