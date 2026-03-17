package com.domains.dtos;

import jakarta.validation.constraints.*;

public class SecretariaDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = SecretariaDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = SecretariaDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Nome da secretario é obrigatória")
    @Size(max = 150, message = "Nome da secretaria deve ter no máximo 150 caracteres")
    private String nome;

    @NotNull(message = "Prefeitura é obrigatório")
    private Integer prefeituraId;

    public SecretariaDTO() {
    }

    public SecretariaDTO(Long id, String nome, Integer prefeituraId) {
        this.id = id;
        this.nome = nome;
        this.prefeituraId = prefeituraId;
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

    public Integer getPrefeituraId() {
        return prefeituraId;
    }

    public void setPrefeituraId(Integer prefeituraId) {
        this.prefeituraId = prefeituraId;
    }
}
