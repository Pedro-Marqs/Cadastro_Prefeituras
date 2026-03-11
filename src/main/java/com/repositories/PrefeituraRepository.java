package com.repositories;

import com.domains.Prefeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefeituraRepository extends JpaRepository<Prefeitura, Long> {
}

