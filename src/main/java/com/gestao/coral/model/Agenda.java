package com.gestao.coral.model;

// Precisamos de importar anotações para datas
import java.sql.Date; // Vamos manter o java.sql.Date do projeto original
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entidade JPA que mapeia para a tabela 'agenda_apresentacoes'.
 */
@Entity
@Table(name = "agenda_apresentacoes") // O nome da tabela no SQL é 'agenda_apresentacoes'
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // @Temporal diz ao JPA como mapear o tipo de data
    // (neste caso, apenas a Data, sem hora)
    @Temporal(TemporalType.DATE)
    private Date data;

    private String local;

    private String descricao;
    
    // Construtores, Getters e Setters (iguais ao do projeto antigo)

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