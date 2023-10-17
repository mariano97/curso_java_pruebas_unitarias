package com.mario.serranouruena.develop.pruebas;

import com.mario.serranouruena.develop.pruebas.models.Cuenta;
import com.mario.serranouruena.develop.pruebas.repositories.CuentaRepository;
import org.aspectj.apache.bcel.util.Repository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integracion_jpa")
//@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DataJpaTest
//@ExtendWith(SpringExtension.class)
public class integracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andrés", cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Andrés");
        assertTrue(cuenta.isPresent());
        assertEquals("Andrés", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rod");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuenta = cuentaRepository.findAll();
        assertFalse(cuenta.isEmpty());
        assertEquals(2, cuenta.size());
    }

    @Test
    void testSave() {
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        //Cuenta cuenta = cuentaRepository.findByPersona("Pepe").orElseThrow();
        Cuenta cuenta = cuentaRepository.save(cuentaPepe);
        //Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        // assertEquals(3L, cuenta.getId());
    }

    @Test
    void testUpdate() {
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        //Cuenta cuenta = cuentaRepository.findByPersona("Pepe").orElseThrow();
        Cuenta cuenta = cuentaRepository.save(cuentaPepe);
        //Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        // assertEquals(3L, cuenta.getId());

        cuenta.setSaldo(new BigDecimal("3800"));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        assertEquals("Pepe", cuentaActualizada.getPersona());
        assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());
        assertEquals(cuenta.getId(), cuentaActualizada.getId());
    }

    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();

        assertEquals("Jhon", cuenta.getPersona());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> {
            //cuentaRepository.findByPersona("Jhon").orElseThrow();
            cuentaRepository.findById(2L).orElseThrow();
        });
        assertEquals(1, cuentaRepository.findAll().size());

    }
}
