package com.gestao.coral.model;


import java.sql.Date; 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "agenda_apresentacoes") 
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    
    
    @Temporal(TemporalType.DATE)
    private Date data;

    private String local;

    private String descricao;
    
    public Agenda() {}
    
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public Date getData(){return data;}
    public void setData(Date data){this.data=data;}
    public String getLocal(){return local;}
    public void setLocal(String local){this.local=local;}
    public String getDescricao(){return descricao;}
    public void setDescricao(String descricao){this.descricao=descricao;}
}