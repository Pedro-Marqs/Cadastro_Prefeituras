package com.repositories;

import com.domains.Secretaria;
import com.domains.Servidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {
    Page<Secretaria> findByPrefeitura_Id(Long prefeituraId, Pageable pageable);
    Page<Secretaria> findByNome(String nome, Pageable pageable);
}
