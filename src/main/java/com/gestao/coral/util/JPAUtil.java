package com.gestao.coral.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static EntityManagerFactory FACTORY = null; // Começa como null

    // Usamos um bloco estático para inicializar a FACTORY
    // e apanhar *qualquer* erro que aconteça durante a inicialização
    static {
        System.out.println("### JPAUtil: Tentando criar EntityManagerFactory...");
        try {
            FACTORY = Persistence.createEntityManagerFactory("coral-pu");
            System.out.println("### JPAUtil: EntityManagerFactory criada com SUCESSO!");
        } catch (Throwable ex) {
            // Se *qualquer* erro acontecer aqui, ele será impresso
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("### ERRO CRÍTICO ao criar EntityManagerFactory ###");
            ex.printStackTrace(); // Imprime o erro detalhado nos logs
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            // Lança uma exceção para impedir que a aplicação continue
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        // Se a factory não foi criada, não podemos continuar
        if (FACTORY == null) {
            System.err.println("### ERRO: EntityManagerFactory é null. A inicialização falhou.");
            throw new IllegalStateException("EntityManagerFactory não foi inicializada.");
        }
        System.out.println("### JPAUtil: Criando novo EntityManager...");
        return FACTORY.createEntityManager();
    }
}