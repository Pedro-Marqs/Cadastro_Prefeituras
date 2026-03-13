package com.repositories;

import com.domains.Prefeitura;
import com.domains.Secretaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefeituraRepository extends JpaRepository<Prefeitura, Long> {
    Page<Prefeitura> findByCidade(String cidade, Pageable pageable);
    Page<Prefeitura> findByNome(String nome, Pageable pageable);
}

