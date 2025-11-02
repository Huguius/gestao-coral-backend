package com.gestao.coral.servlet;

import com.gestao.coral.dao.AgendaDAO;
import com.gestao.coral.dao.CoristaDAO;
import com.gestao.coral.dao.MusicoDAO;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/dashboard")
public class DashboardServlet extends HttpServlet {

    private CoristaDAO coristaDAO = new CoristaDAO();
    private AgendaDAO agendaDAO = new AgendaDAO();
    private MusicoDAO musicoDAO = new MusicoDAO();
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

        Map<String, Object> dashboardData = new HashMap<>();
        String proximoEventoFormatado = "Nenhum evento futuro";

        try {
            long totalCoristas = coristaDAO.countAtivos();
            dashboardData.put("totalCoristas", totalCoristas);

            long totalMusicos = musicoDAO.countAtivos();
            dashboardData.put("totalMusicos", totalMusicos);

            Agenda proximoEvento = agendaDAO.findProximoEvento();
            if (proximoEvento != null && proximoEvento.getData() != null) {
                 SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy");
                 proximoEventoFormatado = sdfDisplay.format(proximoEvento.getData());
                 if (proximoEvento.getLocal() != null && !proximoEvento.getLocal().isEmpty()) {
                     proximoEventoFormatado += " - " + proximoEvento.getLocal();
                 }
            }
             dashboardData.put("proximoEvento", proximoEventoFormatado);
             dashboardData.put("proximoEnsaio", proximoEventoFormatado);
             dashboardData.put("proximaApresentacao", proximoEventoFormatado);

        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no DashboardServlet.doGet ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao buscar dados do dashboard: " + e.getMessage());
            return;
        }

        String json = this.gson.toJson(dashboardData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}