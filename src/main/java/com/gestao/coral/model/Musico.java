package com.gestao.coral.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "musicos") 
public class Musico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
    
    private String nome;
    
    private String instrumento;
    
    private boolean ativo;

    public Musico() {}
    
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getNome(){return nome;}
    public void setNome(String nome){this.nome=nome;}
    public String getInstrumento(){return instrumento;}
    public void setInstrumento(String instrumento){this.instrumento=instrumento;}
    public boolean isAtivo(){return ativo;}
    public void setAtivo(boolean ativo){this.ativo=ativo;}
}