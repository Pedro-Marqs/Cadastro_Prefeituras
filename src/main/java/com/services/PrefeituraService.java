package com.services;

import com.domains.dtos.PrefeituraDTO;
import com.mappers.PrefeituraMapper;
import com.repositories.PrefeituraRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrefeituraService {

    private final PrefeituraRepository prefeituraRepo;

    // Injeção por construtor (Spring injeta automaticamente se houver só um construtor público)
    public PrefeituraService(PrefeituraRepository prefeituraRepo) {
        this.prefeituraRepo = prefeituraRepo;
    }

    @Transactional(readOnly = true)
    public List<PrefeituraDTO> findAll(){
        //retorna uma lista de ProdutoDTO
        return PrefeituraMapper.toDtoList(prefeituraRepo.findAll());
    }

}
