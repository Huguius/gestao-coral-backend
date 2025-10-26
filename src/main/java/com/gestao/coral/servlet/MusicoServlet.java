package com.gestao.coral.servlet;

import com.gestao.coral.dao.MusicoDAO;
import com.gestao.coral.model.Musico;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet que gere os pedidos da API para /api/musicos.
 * (Implementa apenas o doGet, de acordo com o MusicoDAO)
 */
@WebServlet("/api/musicos") // Responde no URL "/api/musicos"
public class MusicoServlet extends HttpServlet {
    
    private MusicoDAO musicoDAO = new MusicoDAO();
    private Gson gson = new Gson();

    /**
     * Lida com pedidos GET (ex: carregar a lista de músicos).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Ir buscar os dados ao DAO
        List<Musico> musicos = musicoDAO.findAll();
        
        // 2. Converter a lista para JSON
        String json = this.gson.toJson(musicos);
        
        // 3. Enviar a resposta JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
    
    // Nota: Não implementamos doPost ou doDelete aqui
    // porque o DAO original não os tinha.
}