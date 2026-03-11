package com.domains;

import java.time.LocalDate;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="prefeitura")
@SequenceGenerator(
        name = "seq_prefeitura", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_prefeitura", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class Prefeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prefeitura")
    private Long id;

    @NotBlank
    @Column(nullable=false, length=120)
    private String cidade;

    @NotBlank
    @Column(nullable=false, length=14)
    private String CNPJ;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate fundadaEm;

    public Prefeitura() {
    }

    public Prefeitura(Long id, String cidade, String CNPJ, LocalDate fundadaEm) {
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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prefeitura that = (Prefeitura) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
