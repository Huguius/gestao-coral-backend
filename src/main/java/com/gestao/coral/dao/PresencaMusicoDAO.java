package com.gestao.coral.dao;

import com.gestao.coral.model.Agenda;
import com.gestao.coral.model.Musico;
import com.gestao.coral.model.PresencaMusico;
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class PresencaMusicoDAO {

    public void marcarPresenca(int idMusico, int idAgenda, boolean presente) {
        EntityManager em = JPAUtil.getEntityManager();
        PresencaMusico presenca = null;

        try {
            String jpql = "SELECT p FROM PresencaMusico p WHERE p.musico.id = :idMusico AND p.agenda.id = :idAgenda";
            
            TypedQuery<PresencaMusico> query = em.createQuery(jpql, PresencaMusico.class);
            query.setParameter("idMusico", idMusico);
            query.setParameter("idAgenda", idAgenda);
            
            presenca = query.getSingleResult();

        } catch (NoResultException e) {
        }

        try {
            em.getTransaction().begin();

            if (presenca == null) {
                presenca = new PresencaMusico();
                
                Musico musico = em.getReference(Musico.class, idMusico);
                Agenda agenda = em.getReference(Agenda.class, idAgenda);

                presenca.setMusico(musico);
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