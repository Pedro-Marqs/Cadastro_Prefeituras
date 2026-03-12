package com.domains;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Secretaria")
@SequenceGenerator(
        name = "seq_Secretaria",
        sequenceName = "seq_Secretaria",
        allocationSize = 1
)

public class Secretaria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Secretaria")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPrefeitura", nullable = false)
    private Prefeitura prefeitura;

    public Secretaria() {
    }

    public Secretaria(Long id, String nome, Prefeitura prefeitura) {
        this.id = id;
        this.nome = nome;
        this.prefeitura = prefeitura;
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

    public Prefeitura getPrefeitura() {
        return prefeitura;
    }

    public void setPrefeitura(Prefeitura prefeitura) {
        this.prefeitura = prefeitura;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Secretaria that = (Secretaria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
