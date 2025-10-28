package com.gestao.coral.dao;

import com.gestao.coral.model.Usuario;
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * DAO para a entidade Usuario, focado na autenticação.
 */
public class UsuarioDAO {

    /**
     * Autentica um usuário pelo username e password.
     * Retorna o objeto Usuario se encontrar, ou null se não encontrar.
     * Baseado na lógica do LoginFrame antigo.
     */
    public Usuario authenticate(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        Usuario usuario = null;
        try {
            // Consulta JPQL: Seleciona o Usuario 'u' ONDE u.username = :username E u.password = :password
            String jpql = "SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("username", username);
            query.setParameter("password", password); // ATENÇÃO: Senha em texto plano

            usuario = query.getSingleResult(); // Tenta buscar um resultado único

        } catch (NoResultException e) {
            // Se getSingleResult não encontrar nada, lança esta exceção.
            // Neste caso, 'usuario' permanece null, indicando falha no login.
            System.out.println("Autenticação falhou para o usuário: " + username);
        } finally {
            em.close(); // Fecha sempre o EntityManager
        }
        return usuario; // Retorna o usuário encontrado ou null
    }
}