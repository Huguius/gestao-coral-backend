package com.gestao.coral.dao;

import com.gestao.coral.model.Agenda; // A entidade Agenda
import com.gestao.coral.util.JPAUtil; // O nosso utilitário
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Agenda.
 * Substitui o AgendaDAO antigo, usando JPA.
 */
public class AgendaDAO {

    /**
     * Lista todos os eventos da agenda.
     * Equivalente ao 'findAll' antigo.
     */
    public List<Agenda> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        
        String jpql = "SELECT a FROM Agenda a"; // Consultamos a classe Agenda
        List<Agenda> agendas = em.createQuery(jpql, Agenda.class).getResultList();
        
        em.close();
        return agendas;
    }

    /**
     * Insere um novo evento na agenda.
     * Equivalente ao 'insert' antigo.
     */
    public void insert(Agenda agenda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(agenda); // Guarda o objeto no banco de dados
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Apaga um evento da agenda pelo seu ID.
     * Equivalente ao 'delete' antigo.
     */
    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // 1. Encontrar o evento pelo ID
            Agenda agenda = em.find(Agenda.class, id);
            
            // 2. Se existir, apaga-o
            if (agenda != null) {
                em.remove(agenda);
            }
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}