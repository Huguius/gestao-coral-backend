package com.gestao.coral.model;

// Importamos as anotações do Jakarta Persistence (JPA)
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

/**
 * Esta é uma Entidade JPA que mapeia para a tabela 'coristas'.
 */
@Entity
@Table(name = "coristas") // Diz ao JPA exatamente qual tabela usar
public class Corista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usa o AUTO_INCREMENT do MySQL
    private int id;

    private String nome;

    // Usamos @Column porque o nome no Java (tipoVoz)
    // é diferente do nome na tabela (tipo_voz)
    @Column(name = "tipo_voz")
    private String tipoVoz;

    private boolean ativo;

    // Construtores, Getters e Setters (JPA precisa deles)
    // (Estes são iguais aos do projeto antigo)

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