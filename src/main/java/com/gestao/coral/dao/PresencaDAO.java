package com.gestao.coral.dao;

import com.gestao.coral.model.Agenda;
import com.gestao.coral.model.Corista;
import com.gestao.coral.model.Presenca;
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * DAO (Data Access Object) para a entidade Presenca.
 * Substitui o PresencaDAO antigo, usando JPA.
 */
public class PresencaDAO {

    /**
     * Marca (ou atualiza) a presença de um corista numa agenda.
     * Equivalente ao 'marcarPresenca' antigo.
     */
    public void marcarPresenca(int idCorista, int idAgenda, boolean presente) {
        EntityManager em = JPAUtil.getEntityManager();
        Presenca presenca = null;

        try {
            // 1. Tenta encontrar uma presença existente
            // Usamos JPQL para consultar com base nos IDs dos objetos relacionados
            String jpql = "SELECT p FROM Presenca p WHERE p.corista.id = :idCorista AND p.agenda.id = :idAgenda";
            
            TypedQuery<Presenca> query = em.createQuery(jpql, Presenca.class);
            query.setParameter("idCorista", idCorista);
            query.setParameter("idAgenda", idAgenda);
            
            presenca = query.getSingleResult(); // Tenta obter o resultado

        } catch (NoResultException e) {
            // Se não encontrou (NoResultException), 'presenca' continua null.
            // Isso significa que precisamos criar um novo registo.
        }

        try {
            em.getTransaction().begin();

            if (presenca == null) {
                // 2. Se NÃO encontrou, cria um novo objeto Presenca
                presenca = new Presenca();
                
                // Vai buscar as *referências* dos objetos Corista e Agenda
                // (em.getReference() é mais eficiente que em.find() aqui)
                Corista corista = em.getReference(Corista.class, idCorista);
                Agenda agenda = em.getReference(Agenda.class, idAgenda);

                presenca.setCorista(corista);
                presenca.setAgenda(agenda);
                presenca.setPresente(presente);

                em.persist(presenca); // Guarda o *novo* registo

            } else {
                // 3. Se ENCONTROU, apenas atualiza o estado 'presente'
                presenca.setPresente(presente);
                em.merge(presenca); // Atualiza o registo *existente*
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}