package com.gestao.coral.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "presencas_musicos")
public class PresencaMusico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_musico")
    private Musico musico;

    @ManyToOne
    @JoinColumn(name = "id_agenda")
    private Agenda agenda;

    private boolean presente;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Musico getMusico() {
        return musico;
    }
    public void setMusico(Musico musico) {
        this.musico = musico;
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