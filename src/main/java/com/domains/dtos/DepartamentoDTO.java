package com.domains.dtos;

import jakarta.validation.constraints.*;

public class DepartamentoDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = DepartamentoDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = DepartamentoDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Nome do departamento é obrigatório")
    @Size(max = 150, message = "Nome do departamento deve ter no máximo 150 caracteres")
    private String nome;

    @NotNull(message = "Secretaria é obrigatório")
    private Integer secretariaId;

    public DepartamentoDTO() {
    }

    public DepartamentoDTO(Long id, String nome, Integer secretariaId) {
        this.id = id;
        this.nome = nome;
        this.secretariaId = secretariaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSecretariaId() {
        return secretariaId;
    }

    public void setSecretariaId(Integer secretariaId) {
        this.secretariaId = secretariaId;
    }
}