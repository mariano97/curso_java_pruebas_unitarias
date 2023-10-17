package com.mario.serranouruena.develop.mockito.repositories;

import com.mario.serranouruena.develop.mockito.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntasRepositoryImpl implements PreguntaRepository {
    @Override
    public List<String> findPreguntasByExamneId(Long id) {
        System.out.println("PreguntasRepositoryImpl.findPreguntasByExamneId");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("PreguntasRepositoryImpl.guardarVarias");
    }
}
