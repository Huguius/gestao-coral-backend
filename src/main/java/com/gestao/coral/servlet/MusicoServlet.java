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
import java.util.stream.Collectors; 

@WebServlet("/api/musicos/*") 
public class MusicoServlet extends HttpServlet {

    private MusicoDAO musicoDAO = new MusicoDAO(); 
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Musico> musicos = musicoDAO.findAll();
        String json = this.gson.toJson(musicos);

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
            Musico novoMusico = this.gson.fromJson(jsonPayload, Musico.class);

            musicoDAO.insert(novoMusico); 

            response.setStatus(HttpServletResponse.SC_CREATED); 
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do músico em falta no URL para atualização.");
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));

            
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Musico musicoAtualizado = this.gson.fromJson(jsonPayload, Musico.class);

            musicoAtualizado.setId(id);

            musicoDAO.update(musicoAtualizado); 

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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do músico em falta no URL.");
                return;
            }
            int id = Integer.parseInt(pathInfo.substring(1));
            
            musicoDAO.delete(id); 
            
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