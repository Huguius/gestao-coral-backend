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


@WebServlet("/api/presencas") 
public class PresencaServlet extends HttpServlet {

    private PresencaDAO presencaDAO = new PresencaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        
        PresencaRequest presencaData = gson.fromJson(jsonPayload, PresencaRequest.class);

        try {
            
            presencaDAO.marcarPresenca(presencaData.idCorista, presencaData.idAgenda, presencaData.presente);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"message\":\"Presença registrada com sucesso.\"}");
            response.getWriter().flush();

        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no PresencaServlet.doPost ###");
            e.printStackTrace(); 
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao registrar presença: " + e.getMessage());
        }
    }

    private static class PresencaRequest {
        int idCorista;
        int idAgenda;
        boolean presente;
    }
    
}