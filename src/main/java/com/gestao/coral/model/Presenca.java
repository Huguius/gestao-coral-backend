package com.gestao.coral.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidade JPA que mapeia a tabela 'presencas'.
 * Esta é uma tabela de associação que liga Corista e Agenda.
 */
@Entity
@Table(name = "presencas") // Liga à tabela 'presencas'
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relacionamento: Muitas (Many) presenças para Um (One) Corista.
    @ManyToOne
    @JoinColumn(name = "id_corista") // Diz ao JPA qual coluna usar para "juntar"
    private Corista corista;

    // Relacionamento: Muitas (Many) presenças para Uma (One) Agenda.
    @ManyToOne
    @JoinColumn(name = "id_agenda") // Diz ao JPA qual coluna usar para "juntar"
    private Agenda agenda;

    private boolean presente;

    // Getters e Setters
    
    public Presenca() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Corista getCorista() {
        return corista;
    }

    public void setCorista(Corista corista) {
        this.corista = corista;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }
}