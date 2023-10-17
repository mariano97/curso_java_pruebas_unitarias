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
class ExamenServiceImplTest {

    @Mock
    ExamenRepositorioImpl repository;
    @Mock
    PreguntasRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this); // ---> Una forma para habilitar anotaciones
        /*repository = Mockito.mock(ExamenRepository.class);
        preguntaRepository = Mockito.mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(repository, preguntaRepository);*/

    }

    @Test
    void findExamenByName() {

        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = service.findExamenByName("Matematicas");

        Assertions.assertTrue(examen.isPresent());
        Assertions.assertEquals(5L, examen.orElseThrow().getId());
        Assertions.assertEquals("Matematicas", examen.get().getNombre());

    }

    @Test
    void findExamenByNameListaVacia() {
        List<Examen> datos = Collections.emptyList();
        Mockito.when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenByName("Matematicas");

        Assertions.assertFalse(examen.isPresent());

    }

    @Test
    void TestPreguntasExamen() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        Assertions.assertEquals(5, examen.getPreguntas().size());
        Assertions.assertTrue(examen.getPreguntas().contains("aritmetica"));
    }

    @Test
    void TestPreguntasExamenVerify() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        Assertions.assertEquals(5, examen.getPreguntas().size());
        Assertions.assertTrue(examen.getPreguntas().contains("aritmetica"));
        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(5L);
    }

    @Test
    void TestNoExisteExamenVerify() {
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        Assertions.assertNull(examen);
        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.anyLong());
    }

    @Test
    void guardarExamneTest() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        Mockito.when(repository.save(Mockito.any(Examen.class))).then(new Answer<Examen>() {

            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });
        // When
        Examen examen = service.guardar(newExamen);
        // Then
        Assertions.assertNotNull(examen.getId());
        Assertions.assertEquals(8L, examen.getId());
        Assertions.assertEquals("Fisica", examen.getNombre());
        Mockito.verify(repository).save(Mockito.any(Examen.class));
        Mockito.verify(preguntaRepository).guardarVarias(Mockito.anyList());
    }

    @Test
    void testManejoException() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.anyLong());
    }

    @Test
    void testManejoExceptionIdNull() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.isNull())).thenThrow(IllegalArgumentException.class);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.isNull());
    }

    @Test
    void testArgumentMatchers() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        Mockito.verify(repository).findAll();
        //Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.argThat(arg -> arg != null && arg.equals(5L)));
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.argThat(arg -> arg != null && arg >= 5L));
        //Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.eq(5L));

    }

    @Test
    void testArgumentMatchers2() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES_IDS_NEGATICVOS);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.argThat(arg -> arg != null && arg > 0));

    }

    @Test
    void testArgumentMatchers3() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES_IDS_NEGATICVOS);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        Mockito.verify(repository).findAll();
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.argThat(new MiArgumentMatchers()));

    }


    public static class MiArgumentMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long aLong) {
            this.argument = aLong;
            return aLong != null && aLong > 0;
        }

        @Override
        public String toString() {
            return "Es para un mensaje personalizado que se imprime cuando falla el test, " +
                    argument + " debe ser un número positivo";
        }
    }


    @Test
    void testArgumentCaptor() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        // ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(captor.capture());

        Assertions.assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        Mockito.doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(Mockito.anyList());
        // Mockito.when(preguntaRepository.guardarVarias(Mockito.anyList())).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAnswer() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        Mockito.doAnswer(invocation -> {
            Long id  = invocation.getArgument(0);
            return id == 5L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasByExamneId(Mockito.anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");


        Assertions.assertEquals(5, examen.getPreguntas().size());
        Assertions.assertTrue(examen.getPreguntas().contains("geometria"));
        Assertions.assertEquals(5L, examen.getId());
        Assertions.assertEquals("Matematicas", examen.getNombre());

        Mockito.verify(preguntaRepository).findPreguntasByExamneId(Mockito.anyLong());

    }

    @Test
    void testDoAnswerGuardarExamneTest() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        Mockito.doAnswer(new Answer<Examen>() {

            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).save(Mockito.any(Examen.class));

        // When
        Examen examen = service.guardar(newExamen);
        // Then
        Assertions.assertNotNull(examen.getId());
        Assertions.assertEquals(8L, examen.getId());
        Assertions.assertEquals("Fisica", examen.getNombre());
        Mockito.verify(repository).save(Mockito.any(Examen.class));
        Mockito.verify(preguntaRepository).guardarVarias(Mockito.anyList());
    }

    @Test
    void testDoCallRealMethod() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //Mockito.when(preguntaRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        Mockito.doCallRealMethod().when(preguntaRepository).findPreguntasByExamneId(Mockito.anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        Assertions.assertEquals(5L, examen.getId());
        Assertions.assertEquals("Matematicas", examen.getNombre());

    }

    @Test
    void testSpy() {
        ExamenRepository examenRepository = Mockito.spy(ExamenRepositorioImpl.class);
        PreguntaRepository preguntasRepository = Mockito.spy(PreguntasRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntasRepository);

        List<String> preguntas = Arrays.asList("aritmetica");
        // Mockito.when(preguntasRepository.findPreguntasByExamneId(Mockito.anyLong())).thenReturn(preguntas);
        Mockito.doReturn(preguntas).when(preguntasRepository).findPreguntasByExamneId(Mockito.anyLong());


        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        Assertions.assertEquals(5L, examen.getId());
        Assertions.assertEquals("Matematicas", examen.getNombre());
        Assertions.assertEquals(1, examen.getPreguntas().size());
        Assertions.assertTrue(examen.getPreguntas().contains("aritmetica"));

        Mockito.verify(examenRepository).findAll();
        Mockito.verify(preguntasRepository).findPreguntasByExamneId(Mockito.anyLong());
    }

    @Test
    void testOrdenInvocaciones() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = Mockito.inOrder(preguntaRepository);
        inOrder.verify(preguntaRepository).findPreguntasByExamneId(5L);
        inOrder.verify(preguntaRepository).findPreguntasByExamneId(6L);

    }

    @Test
    void testOrdenInvocaciones2() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = Mockito.inOrder(repository, preguntaRepository);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasByExamneId(5L);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasByExamneId(6L);

    }

    @Test
    void testNumeroInvocaciones() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");
        Mockito.verify(preguntaRepository).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.times(1)).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.atLeast(1)).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.atLeastOnce()).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.atMost(10)).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.atMostOnce()).findPreguntasByExamneId(5L);
    }

    @Test
    void testNumeroInvocaciones2() {
        Mockito.when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");
        //Mockito.verify(preguntaRepository).findPreguntasByExamneId(5L); // Falla
        Mockito.verify(preguntaRepository, Mockito.times(2)).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.atLeast(1)).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.atLeastOnce()).findPreguntasByExamneId(5L);
        Mockito.verify(preguntaRepository, Mockito.atMost(2)).findPreguntasByExamneId(5L);
        //Mockito.verify(preguntaRepository, Mockito.atMostOnce()).findPreguntasByExamneId(5L); Falla
    }

    @Test
    void testNumeroInvaciones3() {
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        service.findExamenPorNombreConPreguntas("Matematicas");
        Mockito.verify(preguntaRepository, Mockito.never()).findPreguntasByExamneId(Mockito.anyLong());
        Mockito.verifyNoInteractions(preguntaRepository);
        Mockito.verify(repository).findAll();
        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(repository, Mockito.atLeast(1)).findAll();
        Mockito.verify(repository, Mockito.atLeastOnce()).findAll();
        Mockito.verify(repository, Mockito.atMost(10)).findAll();
        Mockito.verify(repository, Mockito.atMostOnce()).findAll();
    }
}