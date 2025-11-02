package com.gestao.coral.dao;

import com.gestao.coral.model.Agenda; 
import com.gestao.coral.util.JPAUtil; 
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AgendaDAO {

    public List<Agenda> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        
        String jpql = "SELECT a FROM Agenda a"; 
        List<Agenda> agendas = em.createQuery(jpql, Agenda.class).getResultList();
        
        em.close();
        return agendas;
    }

    public void insert(Agenda agenda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(agenda); 
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            
            Agenda agenda = em.find(Agenda.class, id);
            
           
            if (agenda != null) {
                em.remove(agenda);
            }
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public void update(Agenda agenda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            em.merge(agenda);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    
    public Agenda findProximoEvento() {
        EntityManager em = JPAUtil.getEntityManager();
        Agenda proximoEvento = null;
        try {
            
            
            
            String jpql = "SELECT a FROM Agenda a WHERE a.data >= CURRENT_DATE ORDER BY a.data ASC";
            TypedQuery<Agenda> query = em.createQuery(jpql, Agenda.class)
                                         .setMaxResults(1); 

            
            List<Agenda> resultados = query.getResultList();
            if (!resultados.isEmpty()) {
                proximoEvento = resultados.get(0);
            }

        } catch (Exception e) {
            System.err.println("### ERRO ao buscar próximo evento no AgendaDAO ###");
            e.printStackTrace();
            
        } finally {
            em.close();
        }
        return proximoEvento;
    }

    
    
    
    
}