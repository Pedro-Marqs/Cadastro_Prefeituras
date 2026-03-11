package com.domains.enums;

public enum Provimento {
    EFETIVO (0, "EFETIVO"), COMISSIONADO (1, "COMISSIONADO");
    private Integer id;
    private String descricao;

    Provimento(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static Provimento toEnum(Integer id){
        if(id == null) return null;
        for(Provimento provimento : Provimento.values()){
            if(id.equals(provimento.getId())){
                return provimento;
            }
        }
        throw new IllegalArgumentException("Provimento inválido!");
    }
}
