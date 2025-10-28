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

    /**
     * Insere um novo músico na base de dados.
     * @param musico O objeto Musico a ser inserido.
     */
    public void insert(Musico musico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(musico); // Guarda o novo músico
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Atualiza os dados de um músico existente.
     * @param musico O objeto Musico com os dados atualizados (incluindo o ID).
     */
    public void update(Musico musico) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(musico); // Atualiza o músico existente
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Apaga um músico da base de dados pelo seu ID.
     * @param id O ID do músico a ser apagado.
     */
    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Musico musico = em.find(Musico.class, id); // Encontra o músico pelo ID
            if (musico != null) {
                em.remove(musico); // Apaga se encontrado
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}