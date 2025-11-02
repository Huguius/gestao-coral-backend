package com.gestao.coral.dao;

import com.gestao.coral.model.Usuario;
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UsuarioDAO {

    public Usuario authenticate(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        Usuario usuario = null;
        try {
            
            String jpql = "SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("username", username);
            query.setParameter("password", password); 

            usuario = query.getSingleResult(); 

        } catch (NoResultException e) {
            
            
            System.out.println("Autenticação falhou para o usuário: " + username);
        } finally {
            em.close(); 
        }
        return usuario; 
    }
}