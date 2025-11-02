package com.gestao.coral.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "coristas") 
public class Corista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    private String nome;

    
    
    @Column(name = "tipo_voz")
    private String tipoVoz;

    private boolean ativo;
    
    public Corista() {}
    
    public Corista(int id, String nome, String tipoVoz, boolean ativo){
        this.id=id; this.nome=nome; this.tipoVoz=tipoVoz; this.ativo=ativo;
    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getNome(){return nome;}
    public void setNome(String nome){this.nome=nome;}
    public String getTipoVoz(){return tipoVoz;}
    public void setTipoVoz(String tipoVoz){this.tipoVoz=tipoVoz;}
    public boolean isAtivo(){return ativo;}
    public void setAtivo(boolean ativo){this.ativo=ativo;}
}