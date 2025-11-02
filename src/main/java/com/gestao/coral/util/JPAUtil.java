package com.gestao.coral.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static EntityManagerFactory FACTORY = null; 

    static {
        System.out.println("### JPAUtil: Tentando criar EntityManagerFactory...");
        try {
            FACTORY = Persistence.createEntityManagerFactory("coral-pu");
            System.out.println("### JPAUtil: EntityManagerFactory criada com SUCESSO!");
        } catch (Throwable ex) {
            
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("### ERRO CRÍTICO ao criar EntityManagerFactory ###");
            ex.printStackTrace(); 
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        
        if (FACTORY == null) {
            System.err.println("### ERRO: EntityManagerFactory é null. A inicialização falhou.");
            throw new IllegalStateException("EntityManagerFactory não foi inicializada.");
        }
        System.out.println("### JPAUtil: Criando novo EntityManager...");
        return FACTORY.createEntityManager();
    }
}