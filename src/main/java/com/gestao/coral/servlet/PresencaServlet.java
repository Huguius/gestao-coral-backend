package com.gestao.coral.servlet;

import com.gestao.coral.dao.PresencaDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Servlet para lidar com o registo de presenças (UC05).
 */
@WebServlet("/api/presencas") // URL para marcar presença
public class PresencaServlet extends HttpServlet {

    private PresencaDAO presencaDAO = new PresencaDAO();
    private Gson gson = new Gson();

    /**
     * Processa o pedido POST para marcar ou atualizar uma presença.
     * Espera um JSON no corpo com idCorista, idAgenda, e presente.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        // Usamos uma classe auxiliar para receber os dados do JSON
        PresencaRequest presencaData = gson.fromJson(jsonPayload, PresencaRequest.class);

        try {
            // Chama o método do DAO que faz a lógica de inserir/atualizar
            presencaDAO.marcarPresenca(presencaData.idCorista, presencaData.idAgenda, presencaData.presente);

            // Envia uma resposta de sucesso (200 OK)
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // Podemos enviar uma mensagem simples de sucesso
            response.getWriter().print("{\"message\":\"Presença registrada com sucesso.\"}");
            response.getWriter().flush();

        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no PresencaServlet.doPost ###");
            e.printStackTrace(); // Log detalhado no servidor
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao registrar presença: " + e.getMessage());
        }
    }

    // Classe auxiliar simples para receber o JSON do pedido
    private static class PresencaRequest {
        int idCorista;
        int idAgenda;
        boolean presente;
    }

    // Poderíamos adicionar um método doGet aqui se precisarmos de buscar
    // dados de presença existentes, mas para marcar presença, o POST é suficiente.
}