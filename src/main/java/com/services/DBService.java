package com.services;

import com.domains.*;
import com.domains.enums.*;
import com.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class DBService {


    @Autowired
    private PrefeituraRepository prefeituraRepo;

    public void initDB() {

        try {
            // Criar prefeituras
            Prefeitura prefeitura1 = new Prefeitura(null, "Fernandópolis", "47842836000105", LocalDate.of(1939, 5, 22));
            Prefeitura prefeitura2 = new Prefeitura(null, "Pindamonhangaba", "12345678911134", LocalDate.of(1672, 8, 12));
            prefeituraRepo.save(prefeitura1);
            prefeituraRepo.save(prefeitura2);



        } catch (Exception e) {
            // Logar o erro ou lançar uma exceção customizada
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
