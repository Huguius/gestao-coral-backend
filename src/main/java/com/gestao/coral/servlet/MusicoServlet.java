package com.gestao.coral.servlet;

import com.gestao.coral.dao.MusicoDAO;
import com.gestao.coral.model.Musico; // A entidade Musico
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors; // Para ler o corpo do pedido

/**
 * Servlet que gere os pedidos da API para /api/musicos.
 * Responde a GET (listar), POST (criar), PUT (atualizar) e DELETE (apagar).
 */
@WebServlet("/api/musicos/*") // Alterado para apanhar IDs
public class MusicoServlet extends HttpServlet {

    private MusicoDAO musicoDAO = new MusicoDAO(); // Usa o MusicoDAO atualizado
    private Gson gson = new Gson();

    /**
     * Lida com pedidos GET (listar todos os músicos).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Nota: Ainda não implementámos GET por ID, apenas listar todos.
        // Se pathInfo for diferente de null ou "/", poderíamos devolver um erro 404
        // ou implementar a busca por ID no DAO e aqui.

        List<Musico> musicos = musicoDAO.findAll();
        String json = this.gson.toJson(musicos);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * Lida com pedidos POST (criar um novo músico).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Musico novoMusico = this.gson.fromJson(jsonPayload, Musico.class);

            musicoDAO.insert(novoMusico); // Usa o método insert do DAO

            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            String jsonResposta = this.gson.toJson(novoMusico);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonResposta);
            response.getWriter().flush();

        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no MusicoServlet.doPost ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erro ao criar músico: " + e.getMessage());
        }
    }

    /**
     * Lida com pedidos PUT (atualizar um músico existente).
     * Espera um URL como /api/musicos/123
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obter ID do URL
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do músico em falta no URL para atualização.");
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));

            // Ler JSON do corpo
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Musico musicoAtualizado = this.gson.fromJson(jsonPayload, Musico.class);

            // Garantir que o ID está correto no objeto
            musicoAtualizado.setId(id);

            // Atualizar no banco de dados
            musicoDAO.update(musicoAtualizado); // Usa o método update do DAO

            // Enviar resposta OK com objeto atualizado
            response.setStatus(HttpServletResponse.SC_OK);
            String jsonResposta = this.gson.toJson(musicoAtualizado);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(jsonResposta);
            response.getWriter().flush();

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido no URL.");
        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no MusicoServlet.doPut ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao atualizar músico: " + e.getMessage());
        }
    }

    /**
     * Lida com pedidos DELETE (apagar um músico).
     * Espera um URL como /api/musicos/123
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obter ID do URL
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do músico em falta no URL.");
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));

            // Apagar no banco de dados
            musicoDAO.delete(id); // Usa o método delete do DAO

            // Enviar resposta de sucesso (204 No Content)
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no MusicoServlet.doDelete ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao apagar músico: " + e.getMessage());
        }
    }
}