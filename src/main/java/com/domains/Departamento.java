package com.domains;

import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Departamento")
@SequenceGenerator(
        name = "seq_Departamento",
        sequenceName = "seq_Departamento",
        allocationSize = 1
)

public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Departamento")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSecretaria", nullable = false)
    private Secretaria secretaria;

    public Departamento() {
    }

    public Departamento(Long id, String nome, Secretaria secretaria) {
        this.id = id;
        this.nome = nome;
        this.secretaria = secretaria;
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

    public Secretaria getSecretaria() {
        return secretaria;
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Departamento that = (Departamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}