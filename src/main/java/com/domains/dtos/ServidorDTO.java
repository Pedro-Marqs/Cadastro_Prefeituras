package com.domains.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ServidorDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = ServidorDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = ServidorDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Nome do servidor é obrigatório")
    @Size(max = 150, message = "Nome do servidor deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "CPF do servidor é obrigatório")
    @Size(max = 150, message = "CPF deve ter 11 caracteres")
    private String CPF;

    @Digits(integer = 12, fraction = 3, message = "Salário deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "Salário não pode ser negativo")
    private BigDecimal salario;

    @NotBlank(message = "Matrícula do servidor é obrigatório")
    @Size(max = 150, message = "Matrícula deve ter 15 caracteres")
    private String matricula;

    @NotNull(message = "Departamento é obrigatório")
    private Integer departamentoId;

    @Min(value = 0, message = "Provimento inválido: use 0 (EFETIVO) ou 1 (COMISSIONADO)")
    @Max(value = 1, message = "Provimento inválido: use 0 (EFETIVO) ou 1 (COMISSIONADO)")
    private Integer provimento;

    public ServidorDTO() {
    }

    public ServidorDTO(Long id, String nome, String CPF, BigDecimal salario, String matricula, Integer departamentoId, Integer provimento) {
        this.id = id;
        this.nome = nome;
        this.CPF = CPF;
        this.salario = salario;
        this.matricula = matricula;
        this.departamentoId = departamentoId;
        this.provimento = provimento;
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

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Integer departamentoId) {
        this.departamentoId = departamentoId;
    }

    public Integer getProvimento() {
        return provimento;
    }

    public void setProvimento(Integer provimento) {
        this.provimento = provimento;
    }
}