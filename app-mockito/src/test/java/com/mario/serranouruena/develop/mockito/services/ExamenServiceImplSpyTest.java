package com.mario.serranouruena.develop.mockito.services;

import com.mario.serranouruena.develop.mockito.Datos;
import com.mario.serranouruena.develop.mockito.models.Examen;
import com.mario.serranouruena.develop.mockito.repositories.ExamenRepositorioImpl;
import com.mario.serranouruena.develop.mockito.repositories.ExamenRepository;
import com.mario.serranouruena.develop.mockito.repositories.PreguntaRepository;
import com.mario.serranouruena.develop.mockito.repositories.PreguntasRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    @Spy
    ExamenRepositorioImpl repository;
    @Spy
    PreguntasRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;



    @Test
    void testSpy() {
        /*ExamenRepository examenRepository = Mockito.spy(ExamenRepositorioImpl.class);
        PreguntaRepository preguntasRepository = Mockito.spy(PreguntasRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(repository, preguntaRepository);*/

        List<String> preguntas = Arrays.asList("aritmetica");
        // Mockito.when(preguntasRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(preguntas);
        Mockito.doReturn(preguntas).when(preguntaRepository).findPreguntasByExamneId(Mockito.anyLong());


        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        Assertions.assertEquals(5L, examen.getId());
        Assertions.assertEquals("Matematicas", examen.getNombre());
        Assertions.assertEquals(1, examen.getPreguntas().size());
        Assertions.assertTrue(examen.getPreguntas().contains("aritmetica"));

        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.anyLong());
    }
}