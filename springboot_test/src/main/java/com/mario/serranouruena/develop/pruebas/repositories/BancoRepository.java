package com.mario.serranouruena.develop.pruebas.repositories;

import com.mario.serranouruena.develop.pruebas.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    //List<Banco> findAll();
    //Banco findById(Long id);
    //void update(Banco banco);

}
