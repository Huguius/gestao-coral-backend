package com.gestao.coral.dao;

import com.gestao.coral.model.Agenda; // A entidade Agenda
import com.gestao.coral.util.JPAUtil; // O nosso utilitário
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Agenda.
 * Substitui o AgendaDAO antigo, usando JPA.
 */
public class AgendaDAO {

    /**
     * Lista todos os eventos da agenda.
     * Equivalente ao 'findAll' antigo.
     */
    public List<Agenda> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        
        String jpql = "SELECT a FROM Agenda a"; // Consultamos a classe Agenda
        List<Agenda> agendas = em.createQuery(jpql, Agenda.class).getResultList();
        
        em.close();
        return agendas;
    }

    /**
     * Insere um novo evento na agenda.
     * Equivalente ao 'insert' antigo.
     */
    public void insert(Agenda agenda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(agenda); // Guarda o objeto no banco de dados
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Apaga um evento da agenda pelo seu ID.
     * Equivalente ao 'delete' antigo.
     */
    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // 1. Encontrar o evento pelo ID
            Agenda agenda = em.find(Agenda.class, id);
            
            // 2. Se existir, apaga-o
            if (agenda != null) {
                em.remove(agenda);
            }
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    /**
     * Atualiza os dados de um evento da agenda existente.
     * @param agenda O objeto Agenda com os dados atualizados (incluindo o ID).
     */
    public void update(Agenda agenda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // O 'merge' procura pelo ID e atualiza os outros campos
            em.merge(agenda);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Encontra o próximo evento da agenda (ensaio ou apresentação)
     * a partir da data atual.
     * @return O objeto Agenda do próximo evento, ou null se não houver eventos futuros.
     */
    public Agenda findProximoEvento() {
        EntityManager em = JPAUtil.getEntityManager();
        Agenda proximoEvento = null;
        try {
            // Consulta JPQL para encontrar Agendas onde a data é maior ou igual à data atual
            // Ordena por data ascendente para pegar o mais próximo
            // setMaxResults(1) limita o resultado a apenas um item (o primeiro, que será o mais próximo)
            String jpql = "SELECT a FROM Agenda a WHERE a.data >= CURRENT_DATE ORDER BY a.data ASC";
            TypedQuery<Agenda> query = em.createQuery(jpql, Agenda.class)
                                         .setMaxResults(1); // Pega apenas o primeiro resultado

            // Usamos getResultList() e verificamos se está vazia para evitar NoResultException
            List<Agenda> resultados = query.getResultList();
            if (!resultados.isEmpty()) {
                proximoEvento = resultados.get(0);
            }

        } catch (Exception e) {
            System.err.println("### ERRO ao buscar próximo evento no AgendaDAO ###");
            e.printStackTrace();
            // Retorna null em caso de erro
        } finally {
            em.close();
        }
        return proximoEvento;
    }

    // Nota: Esta consulta assume que CURRENT_DATE funciona como esperado no seu dialeto
    // de base de dados configurado no Hibernate. Para MySQL, isto geralmente funciona.
    // Também não distinguimos entre "Ensaio" e "Apresentação" aqui, pegamos o próximo evento
    // de qualquer tipo. Poderíamos adicionar um campo 'tipo' à entidade Agenda se necessário.
}