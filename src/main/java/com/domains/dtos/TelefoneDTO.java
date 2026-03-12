package com.domains.dtos;

import jakarta.validation.constraints.*;

public class TelefoneDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = TelefoneDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = TelefoneDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "DDD é obrigatório")
    @Size(max = 2, message = "DDD deve ter no 2 caracteres")
    private String DDD;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 9, message = "Número deve ter no 9 caracteres")
    private String numero;

    @NotNull(message = "Servidor é obrigatório")
    private Integer servidorId;

    public TelefoneDTO() {
    }

    public TelefoneDTO(Long id, String DDD, String numero, Integer servidorId) {
        this.id = id;
        this.DDD = DDD;
        this.numero = numero;
        this.servidorId = servidorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDDD() {
        return DDD;
    }

    public void setDDD(String DDD) {
        this.DDD = DDD;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getServidorId() {
        return servidorId;
    }

    public void setServidorId(Integer servidorId) {
        this.servidorId = servidorId;
    }
}