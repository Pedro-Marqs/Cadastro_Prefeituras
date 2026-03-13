package com.repositories;

import com.domains.Servidor;
import com.domains.Telefone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    Page<Telefone> findByServidor_Id(Integer servidorId, Pageable pageable);
    Page<Telefone> findByNumero(String numero, Pageable pageable);
}
