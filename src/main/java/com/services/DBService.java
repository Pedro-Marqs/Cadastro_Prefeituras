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

    @Autowired
    private SecretariaRepository secretariaRepo;

    @Autowired
    private DepartamentoRepository departamentoRepo;

    @Autowired
    private ServidorRepository servidorRepo;

    @Autowired
    private TelefoneRepository telefoneRepo;

    public void initDB() {

        try {
            // Criar prefeituras
            Prefeitura prefeitura1 = new Prefeitura(null, "Fernandópolis", "47842836000105", LocalDate.of(1939, 5, 22));
            Prefeitura prefeitura2 = new Prefeitura(null, "Pindamonhangaba", "12345678911134", LocalDate.of(1672, 8, 12));
            prefeituraRepo.save(prefeitura1);
            prefeituraRepo.save(prefeitura2);

            // Criar secretarias
            Secretaria secretaria1 = new Secretaria(null, "Secretaria de Gestão", prefeitura1);
            Secretaria secretaria2 = new Secretaria(null, "Secretaria de Obras", prefeitura2);
            secretariaRepo.save(secretaria1);
            secretariaRepo.save(secretaria2);

            // Criar departamentos
            Departamento departamento1 = new Departamento(null, "Almoxarifado Geral", secretaria1);
            Departamento departamento2 = new Departamento(null, "Cemitério",  secretaria2);
            departamentoRepo.save(departamento1);
            departamentoRepo.save(departamento2);

            // Criar servidores
            Servidor servidor1 = new Servidor(null, "Antonio Luis", "78945612399", new BigDecimal("2100.00"), "123456789112345", Provimento.EFETIVO, departamento1);
            Servidor servidor2 = new Servidor(null, "Maria Alice", "12345678911", new BigDecimal("4200.00"), "123123123123123", Provimento.COMISSIONADO, departamento2);
            servidorRepo.save(servidor1);
            servidorRepo.save(servidor2);

            // Criar telefones
            Telefone telefone1 = new Telefone(null, "17", "999999999", servidor1);
            Telefone telefone2 = new Telefone(null, "11",  "123123123", servidor2);
            telefoneRepo.save(telefone1);
            telefoneRepo.save(telefone2);



        } catch (Exception e) {
            // Logar o erro ou lançar uma exceção customizada
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
