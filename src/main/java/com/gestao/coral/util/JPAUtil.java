package com.gestao.coral.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitária para fornecer o EntityManager do JPA.
 * Isto substitui a antiga classe DB.java.
 */
public class JPAUtil {

    // Criamos a "fábrica" apenas uma vez, pois é um processo caro.
    // Usamos o nome "coral-pu" que definimos no persistence.xml
    private static final EntityManagerFactory FACTORY = 
            Persistence.createEntityManagerFactory("coral-pu");

    /**
     * Retorna um novo EntityManager para interagir com o banco de dados.
     */
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}