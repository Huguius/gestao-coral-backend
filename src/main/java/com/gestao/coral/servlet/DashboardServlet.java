package com.gestao.coral.servlet;

import com.gestao.coral.dao.AgendaDAO;
import com.gestao.coral.dao.CoristaDAO;
import com.gestao.coral.model.Agenda;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder; // Para lidar com a data da Agenda
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat; // Para formatar a data para exibição
import java.util.HashMap; // Para criar o objeto JSON de resposta
import java.util.Map;

/**
 * Servlet para fornecer dados dinâmicos para o Dashboard.
 */
@WebServlet("/api/dashboard") // URL para os dados do dashboard
public class DashboardServlet extends HttpServlet {

    private CoristaDAO coristaDAO = new CoristaDAO();
    private AgendaDAO agendaDAO = new AgendaDAO();
    private Gson gson;

    @Override
    public void init() {
        // Configura o GSON para formatar a data da agenda corretamente
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd") // Como o DAO devolve
                .create();
    }

    /**
     * Processa o pedido GET para buscar os dados do dashboard.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> dashboardData = new HashMap<>();
        String proximoEventoFormatado = "Nenhum evento futuro"; // Valor padrão

        try {
            // 1. Buscar o total de coristas ativos
            long totalCoristas = coristaDAO.countAtivos();
            dashboardData.put("totalCoristas", totalCoristas);

            // 2. Buscar o próximo evento
            Agenda proximoEvento = agendaDAO.findProximoEvento();
            if (proximoEvento != null && proximoEvento.getData() != null) {
                // Formata a data e a descrição para exibição no dashboard
                // (Ex: "dd/MM/yyyy - Local: Descrição")
                 SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy");
                 proximoEventoFormatado = sdfDisplay.format(proximoEvento.getData());
                 if (proximoEvento.getLocal() != null && !proximoEvento.getLocal().isEmpty()) {
                     proximoEventoFormatado += " - " + proximoEvento.getLocal();
                 }
                // Nota: Assumimos que o próximo evento pode ser tanto ensaio como apresentação
                // Se precisássemos de distinguir, teríamos de adicionar um campo 'tipo' na Agenda
            }
             // Adiciona a string formatada ao mapa (sempre, mesmo que seja o valor padrão)
             dashboardData.put("proximoEvento", proximoEventoFormatado);
             // Para simplificar, usamos o mesmo evento para "Próximo Ensaio" e "Próxima Apresentação"
             dashboardData.put("proximoEnsaio", proximoEventoFormatado);
             dashboardData.put("proximaApresentacao", proximoEventoFormatado);


        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no DashboardServlet.doGet ###");
            e.printStackTrace(); // Log detalhado no servidor
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao buscar dados do dashboard: " + e.getMessage());
            return; // Sai se houver erro
        }

        // 3. Converter o mapa para JSON e enviar a resposta
        String json = this.gson.toJson(dashboardData);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}