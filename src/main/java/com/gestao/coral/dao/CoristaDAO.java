package com.gestao.coral.dao;

import com.gestao.coral.model.Corista;
import com.gestao.coral.util.JPAUtil; // Importamos o nosso utilitário
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Corista.
 * Substitui o CoristaDAO antigo, usando JPA em vez de JDBC.
 */
public class CoristaDAO {

    /**
     * Lista todos os coristas da base de dados.
     * Equivalente ao 'findAll' antigo.
     */
    public List<Corista> findAll() {
        // 1. Obter o gestor de entidades
        EntityManager em = JPAUtil.getEntityManager();
        
        // 2. Criar a consulta (Query)
        // Note que consultamos a *Classe* "Corista", não a *tabela* "coristas"
        String jpql = "SELECT c FROM Corista c";
        List<Corista> coristas = em.createQuery(jpql, Corista.class).getResultList();
        
        // 3. Fechar o gestor
        em.close();
        
        // 4. Retornar a lista
        return coristas;
    }

    /**
     * Insere um novo corista na base de dados.
     * Equivalente ao 'insert' antigo.
     */
    public void insert(Corista corista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // 1. Iniciar a transação
            em.getTransaction().begin();
            // 2. Persistir (guardar) o objeto
            em.persist(corista);
            // 3. Commit (confirmar) a transação
            em.getTransaction().commit();
        } finally {
            // 4. Fechar o gestor
            em.close();
        }
    }

    /**
     * Apaga um corista da base de dados pelo seu ID.
     * Equivalente ao 'delete' antigo.
     */
    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // 1. Encontrar o corista pelo ID
            Corista corista = em.find(Corista.class, id);
            
            // 2. Se existir, apaga-o
            if (corista != null) {
                em.remove(corista);
            }
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    // Poderíamos adicionar o 'update' aqui, mas vamos manter simples por agora.
}