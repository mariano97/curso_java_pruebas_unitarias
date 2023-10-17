package com.mario.serranouruena.develop.mockito.repositories;

import java.util.List;

public interface PreguntaRepository {

    public List<String> findPreguntasByExamneId(Long id);

    void guardarVarias(List<String> preguntas);

}
