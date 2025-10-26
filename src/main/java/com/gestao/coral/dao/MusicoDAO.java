package com.gestao.coral.dao;

import com.gestao.coral.model.Musico; // A entidade Musico
import com.gestao.coral.util.JPAUtil; // O nosso utilitário
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Musico.
 * Usa JPA para implementar o 'findAll' do projeto original.
 */
public class MusicoDAO {

    /**
     * Lista todos os músicos da base de dados.
     * Equivalente ao 'findAll' antigo.
     */
    public List<Musico> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        
        // Novamente, usamos JPQL para consultar a classe "Musico"
        String jpql = "SELECT m FROM Musico m";
        List<Musico> musicos = em.createQuery(jpql, Musico.class).getResultList();
        
        em.close();
        
        return musicos;
    }
    
    // O DAO original não tinha 'insert' ou 'delete',
    // por isso vamos manter assim por agora.
}