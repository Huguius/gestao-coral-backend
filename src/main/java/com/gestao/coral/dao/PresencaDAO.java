package com.gestao.coral.dao;

import com.gestao.coral.model.Agenda;
import com.gestao.coral.model.Corista;
import com.gestao.coral.model.Presenca;
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class PresencaDAO {

    public void marcarPresenca(int idCorista, int idAgenda, boolean presente) {
        EntityManager em = JPAUtil.getEntityManager();
        Presenca presenca = null;

        try {
            
            
            String jpql = "SELECT p FROM Presenca p WHERE p.corista.id = :idCorista AND p.agenda.id = :idAgenda";
            
            TypedQuery<Presenca> query = em.createQuery(jpql, Presenca.class);
            query.setParameter("idCorista", idCorista);
            query.setParameter("idAgenda", idAgenda);
            
            presenca = query.getSingleResult(); 

        } catch (NoResultException e) {
            
            
        }

        try {
            em.getTransaction().begin();

            if (presenca == null) {
                
                presenca = new Presenca();
                
                
                
                Corista corista = em.getReference(Corista.class, idCorista);
                Agenda agenda = em.getReference(Agenda.class, idAgenda);

                presenca.setCorista(corista);
                presenca.setAgenda(agenda);
                presenca.setPresente(presente);

                em.persist(presenca); 

            } else {
                
                presenca.setPresente(presente);
                em.merge(presenca); 
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}