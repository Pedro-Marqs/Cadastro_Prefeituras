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
@Table(name="Telefone")
@SequenceGenerator(
        name = "seq_Telefone",
        sequenceName = "seq_Telefone",
        allocationSize = 1
)

public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Telefone")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 2)
    private String DDD;

    @NotBlank
    @Column(nullable = false, length = 9)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idServidor", nullable = false)
    private Servidor servidor;

    public Telefone() {
    }

    public Telefone(Long id, String DDD, String numero, Servidor servidor) {
        this.id = id;
        this.DDD = DDD;
        this.numero = numero;
        this.servidor = servidor;
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

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Telefone telefone = (Telefone) o;
        return Objects.equals(id, telefone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
