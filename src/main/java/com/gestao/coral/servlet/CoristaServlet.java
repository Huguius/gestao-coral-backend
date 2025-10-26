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

/**
 * Servlet que gere os pedidos da API para /api/coristas.
 * Responde a GET (listar), POST (criar), e DELETE (apagar).
 */
@WebServlet("/api/coristas/*") // Alterado para "/api/coristas/*" para apanhar IDs (ex: /api/coristas/3)
public class CoristaServlet extends HttpServlet {
    
    private CoristaDAO coristaDAO = new CoristaDAO();
    private Gson gson = new Gson();

    /**
     * Lida com pedidos GET (ex: carregar a lista de coristas).
     */
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

    /**
     * Lida com pedidos POST (ex: criar um novo corista).
     */
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

    /**
     * Lida com pedidos DELETE (ex: apagar um corista).
     * O 'script.js' chamará este método.
     * Espera um URL como /api/coristas/123
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 1. Obter a parte do URL que vem *depois* de "/api/coristas/"
            // ex: Se o URL for /api/coristas/5, pathInfo será "/5"
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do corista em falta no URL.");
                return;
            }

            // 2. Remover a barra "/" e converter o "5" para um inteiro
            int id = Integer.parseInt(pathInfo.substring(1));
            
            // 3. Usar o DAO para apagar
            coristaDAO.delete(id);
            
            // 4. Enviar uma resposta de sucesso (204 No Content)
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao apagar corista: " + e.getMessage());
        }
    }
}