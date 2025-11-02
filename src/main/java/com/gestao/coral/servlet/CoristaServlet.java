package com.gestao.coral.servlet;

import com.gestao.coral.dao.CoristaDAO;
import com.gestao.coral.model.Corista;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/coristas/*") 
public class CoristaServlet extends HttpServlet {
    
    private CoristaDAO coristaDAO = new CoristaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Corista> coristas = coristaDAO.findAll();
        String json = this.gson.toJson(coristas);
        
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
            Corista novoCorista = this.gson.fromJson(jsonPayload, Corista.class);
            
            coristaDAO.insert(novoCorista);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            String jsonResposta = this.gson.toJson(novoCorista);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonResposta);
            
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erro ao criar corista: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            
            
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do corista em falta no URL.");
                return;
            }
            
            int id = Integer.parseInt(pathInfo.substring(1));
            
            coristaDAO.delete(id);
            
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao apagar corista: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do corista em falta no URL para atualização.");
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));

            
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Corista coristaAtualizado = this.gson.fromJson(jsonPayload, Corista.class);
            
            coristaAtualizado.setId(id);
            
            coristaDAO.update(coristaAtualizado);

            response.setStatus(HttpServletResponse.SC_OK);
            String jsonResposta = this.gson.toJson(coristaAtualizado);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonResposta);
            response.getWriter().flush();

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido no URL.");
        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no CoristaServlet.doPut ###");
            e.printStackTrace(); 
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao atualizar corista: " + e.getMessage());
        }
    }
}