package com.gestao.coral.servlet;

import com.gestao.coral.dao.AgendaDAO;
import com.gestao.coral.model.Agenda;
import com.google.gson.Gson;
// Precisamos disto para lidar com datas no JSON
import com.google.gson.GsonBuilder; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet que gere os pedidos da API para /api/agenda.
 * Responde a GET (listar), POST (criar), e DELETE (apagar).
 */
@WebServlet("/api/agenda/*") // Mapeia para /api/agenda E /api/agenda/id
public class AgendaServlet extends HttpServlet {
    
    private AgendaDAO agendaDAO = new AgendaDAO();
    private Gson gson;

    @Override
    public void init() {
        // O tipo 'java.sql.Date' precisa de um formato específico.
        // Usamos o GsonBuilder para garantir que as datas
        // são formatadas como "yyyy-MM-dd" (padrão ISO).
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    /**
     * Lida com pedidos GET (ex: carregar a lista de eventos da agenda).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Agenda> agendas = agendaDAO.findAll();
        String json = this.gson.toJson(agendas);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * Lida com pedidos POST (ex: criar um novo evento na agenda).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            
            // O GSON com o formato de data "yyyy-MM-dd"
            // vai converter o JSON para o objeto Agenda corretamente
            Agenda novaAgenda = this.gson.fromJson(jsonPayload, Agenda.class);
            
            agendaDAO.insert(novaAgenda);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            String jsonResposta = this.gson.toJson(novaAgenda);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonResposta);
            
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erro ao criar evento na agenda: " + e.getMessage());
        }
    }

    /**
     * Lida com pedidos DELETE (ex: apagar um evento da agenda).
     * Espera um URL como /api/agenda/123
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do evento em falta no URL.");
                return;
            }

            // Obter o ID do URL (ex: "/5" -> 5)
            int id = Integer.parseInt(pathInfo.substring(1));
            
            agendaDAO.delete(id);
            
            // Enviar resposta de sucesso
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao apagar evento: " + e.getMessage());
        }
    }
}