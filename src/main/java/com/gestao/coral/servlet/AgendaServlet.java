package com.gestao.coral.servlet;

import com.gestao.coral.dao.AgendaDAO;
import com.gestao.coral.model.Agenda;
import com.google.gson.Gson;

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

@WebServlet("/api/agenda/*") 
public class AgendaServlet extends HttpServlet {
    
    private AgendaDAO agendaDAO = new AgendaDAO();
    private Gson gson;

    @Override
    public void init() {
        
        
        
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            
            
            
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do evento em falta no URL.");
                return;
            }

            
            int id = Integer.parseInt(pathInfo.substring(1));
            
            agendaDAO.delete(id);
            
            
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao apagar evento: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do evento em falta no URL para atualização.");
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));

            
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            
            Agenda agendaAtualizada = this.gson.fromJson(jsonPayload, Agenda.class);

            
            agendaAtualizada.setId(id);

            
            agendaDAO.update(agendaAtualizada); 

            
            response.setStatus(HttpServletResponse.SC_OK);
            String jsonResposta = this.gson.toJson(agendaAtualizada);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonResposta);
            response.getWriter().flush();

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido no URL.");
        } catch (com.google.gson.JsonSyntaxException e) {
            System.err.println("### ERRO ao processar JSON no AgendaServlet.doPut ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erro no formato JSON ou na data (use yyyy-MM-dd): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no AgendaServlet.doPut ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao atualizar evento: " + e.getMessage());
        }
    }
}