package com.gestao.coral.servlet;

import com.gestao.coral.dao.PresencaMusicoDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/api/presencas-musicos")
public class PresencaMusicoServlet extends HttpServlet {

    private PresencaMusicoDAO presencaMusicoDAO = new PresencaMusicoDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        PresencaMusicoRequest presencaData = gson.fromJson(jsonPayload, PresencaMusicoRequest.class);

        try {
            presencaMusicoDAO.marcarPresenca(presencaData.idMusico, presencaData.idAgenda, presencaData.presente);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"message\":\"Presença de músico registrada com sucesso.\"}");
            response.getWriter().flush();

        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no PresencaMusicoServlet.doPost ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao registrar presença do músico: " + e.getMessage());
        }
    }

    private static class PresencaMusicoRequest {
        int idMusico;
        int idAgenda;
        boolean presente;
    }
}