package com.gestao.coral.dao;

import com.gestao.coral.model.Presenca; // Importamos a entidade Presenca
import com.gestao.coral.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.sql.Date; // Usaremos java.sql.Date para os filtros
import java.util.List;

/**
 * DAO para buscar dados para relatórios, especificamente presenças.
 */
public class RelatorioDAO {

    /**
     * Busca registos de presença dentro de um intervalo de datas.
     * Inclui informações do Corista e da Agenda associados.
     * @param dataInicio A data de início do período (inclusive).
     * @param dataFim A data de fim do período (inclusive).
     * @return Uma lista de objetos Presenca que correspondem aos critérios.
     */
    public List<Presenca> findPresencasByDateRange(Date dataInicio, Date dataFim) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Presenca> presencas = null;

        try {
            // Consulta JPQL para buscar Presencas
            // Usamos JOIN FETCH para carregar os dados relacionados (Corista e Agenda)
            // na mesma consulta, evitando o problema de "N+1 queries".
            // Filtramos pela data do evento da agenda (p.agenda.data).
            String jpql = "SELECT p FROM Presenca p " +
                          "JOIN FETCH p.corista c " +
                          "JOIN FETCH p.agenda a " +
                          "WHERE a.data BETWEEN :dataInicio AND :dataFim " +
                          "ORDER BY a.data DESC, c.nome ASC"; // Ordena por data e nome

            TypedQuery<Presenca> query = em.createQuery(jpql, Presenca.class);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);

            presencas = query.getResultList();

        } catch (Exception e) {
            System.err.println("### ERRO ao buscar presenças por data no RelatorioDAO ###");
            e.printStackTrace();
            // Retorna uma lista vazia ou null em caso de erro, dependendo da preferência
            presencas = new java.util.ArrayList<>();
        } finally {
            em.close();
        }
        return presencas;
    }

    // Poderíamos adicionar outros métodos aqui para diferentes tipos de relatórios,
    // como buscar presenças por corista específico, etc.
}