package com.gestao.coral.dao;

import com.gestao.coral.model.Musico;
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class MusicoDAO {

    public List<Musico> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        String jpql = "SELECT m FROM Musico m";
        List<Musico> musicos = em.createQuery(jpql, Musico.class).getResultList();
        em.close();
        return musicos;
    }

    public void insert(Musico musico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(musico); 
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void update(Musico musico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(musico); 
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Musico musico = em.find(Musico.class, id); 
            if (musico != null) {
                em.remove(musico); 
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Conta o número total de músicos ativos.
     * @return O número de músicos com 'ativo' = true.
     */
    public long countAtivos() {
        EntityManager em = JPAUtil.getEntityManager();
        long count = 0;
        try {
            // Consulta JPQL para contar Musicos onde ativo é true
            String jpql = "SELECT COUNT(m) FROM Musico m WHERE m.ativo = true";
            count = em.createQuery(jpql, Long.class)
                      .getSingleResult(); // Retorna um único número (Long)
        } catch (Exception e) {
            System.err.println("### ERRO ao contar músicos ativos no MusicoDAO ###");
            e.printStackTrace();
            // Retorna 0 em caso de erro
        } finally {
            em.close();
        }
        return count;
    }
}