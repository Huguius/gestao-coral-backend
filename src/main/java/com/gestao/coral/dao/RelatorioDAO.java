package com.gestao.coral.dao;

import com.gestao.coral.model.Presenca; 
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.sql.Date; 
import java.util.List;

public class RelatorioDAO {

    public List<Presenca> findPresencasByDateRange(Date dataInicio, Date dataFim) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Presenca> presencas = null;

        try {
            
            
            
            
            String jpql = "SELECT p FROM Presenca p " +
                          "JOIN FETCH p.corista c " +
                          "JOIN FETCH p.agenda a " +
                          "WHERE a.data BETWEEN :dataInicio AND :dataFim " +
                          "ORDER BY a.data DESC, c.nome ASC"; 

            TypedQuery<Presenca> query = em.createQuery(jpql, Presenca.class);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            presencas = query.getResultList();

        } catch (Exception e) {
            System.err.println("### ERRO ao buscar presenças por data no RelatorioDAO ###");
            e.printStackTrace();
            
            presencas = new java.util.ArrayList<>();
        } finally {
            em.close();
        }
        return presencas;
    }

    
    
}