package com.repositories;

import com.domains.Departamento;
import com.domains.Servidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Page<Departamento> findBySecretaria_Id(Long secretariaId, Pageable pageable);
    Page<Departamento> findByNome(String nome, Pageable pageable);
}
