package com.gestao.coral.dao;

import com.gestao.coral.model.Corista;
import com.gestao.coral.util.JPAUtil; 
import jakarta.persistence.EntityManager;
import java.util.List;


public class CoristaDAO {


    public List<Corista> findAll() {
        
        EntityManager em = JPAUtil.getEntityManager();
        
        String jpql = "SELECT c FROM Corista c";
        List<Corista> coristas = em.createQuery(jpql, Corista.class).getResultList();
        
        
        em.close();
        
        
        return coristas;
    }

    public void insert(Corista corista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            
            em.getTransaction().begin();
            
            em.persist(corista);
            
            em.getTransaction().commit();
        } finally {
            
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            
            Corista corista = em.find(Corista.class, id);
            
            
            if (corista != null) {
                em.remove(corista);
            }
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void update(Corista corista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            
            
            em.merge(corista);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public long countAtivos() {
        EntityManager em = JPAUtil.getEntityManager();
        long count = 0;
        try {
            
            String jpql = "SELECT COUNT(c) FROM Corista c WHERE c.ativo = true";
            count = em.createQuery(jpql, Long.class)
                      .getSingleResult(); 
        } catch (Exception e) {
            System.err.println("### ERRO ao contar coristas ativos no CoristaDAO ###");
            e.printStackTrace();
            
        } finally {
            em.close();
        }
        return count;
    }
}