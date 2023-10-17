package com.mario.serranouruena.develop.mockito.repositories;

import com.mario.serranouruena.develop.mockito.Datos;
import com.mario.serranouruena.develop.mockito.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositorioImpl implements ExamenRepository {
    @Override
    public Examen save(Examen examen) {
        System.out.println("ExamenRepositorioImpl.save");
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        System.out.println("ExamenRepositorioImpl.findAll");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Datos.EXAMENES;
    }
}
