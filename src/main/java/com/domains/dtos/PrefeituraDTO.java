package com.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PrefeituraDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Cidade é obrigatório")
    @Size(max = 120, message = "Cidade deve ter no máximo 120 caracteres")
    private String cidade;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 120, message = "CNPJ deve ter 14 caracteres")
    private String CNPJ;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate fundadaEm = LocalDate.now();

    public PrefeituraDTO() {
    }

    public PrefeituraDTO(Long id, String cidade, String CNPJ, LocalDate fundadaEm) {
        this.id = id;
        this.cidade = cidade;
        this.CNPJ = CNPJ;
        this.fundadaEm = fundadaEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public LocalDate getFundadaEm() {
        return fundadaEm;
    }

    public void setFundadaEm(LocalDate fundadaEm) {
        this.fundadaEm = fundadaEm;
    }

    @Override
    public String toString() {
        return "PrefeituraDTO{" +
                "id=" + id +
                ", cidade='" + cidade + '\'' +
                ", CNPJ='" + CNPJ + '\'' +
                ", fundadaEm=" + fundadaEm +
                '}';
    }
}
