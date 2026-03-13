package com.repositories;

import com.domains.Servidor;
import com.domains.enums.Provimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Long> {
    Page<Servidor> findByDepartamento_Id(Integer departamentoId, Pageable pageable);
    Page<Servidor> findByNome(String nome, Pageable pageable);

    Page<Servidor> findByProvimento(Provimento provimento,  Pageable pageable);
    List<Servidor> findByProvimento(Provimento provimento);
}
