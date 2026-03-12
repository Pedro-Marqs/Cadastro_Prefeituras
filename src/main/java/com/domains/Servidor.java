package com.domains;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Servidor")
@SequenceGenerator(
        name = "seq_Servidor",
        sequenceName = "seq_Servidor",
        allocationSize = 1
)

public class Servidor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Servidor")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @NotBlank
    @Column(nullable = false, length = 11)
    private String CPF;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal salario;

    @NotNull
    @Column(precision = 18, scale = 3, nullable = false)
    private Integer matricula;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idDepartamento", nullable = false)
    private Departamento departamento;

    public Servidor() {
    }

    public Servidor(Long id, String nome, String CPF, BigDecimal salario, Integer matricula, Departamento departamento) {
        this.id = id;
        this.nome = nome;
        this.CPF = CPF;
        this.salario = salario;
        this.matricula = matricula;
        this.departamento = departamento;
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

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servidor servidor = (Servidor) o;
        return Objects.equals(id, servidor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
