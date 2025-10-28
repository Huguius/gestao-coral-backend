package com.gestao.coral.servlet;

import com.gestao.coral.dao.RelatorioDAO;
import com.gestao.coral.model.Presenca;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder; // Para configurar formato de data
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date; // Usaremos java.sql.Date
import java.util.Collections; // Para retornar lista vazia em caso de erro
import java.util.List;

/**
 * Servlet para gerar relatórios de presença (UC06).
 */
@WebServlet("/api/relatorios/presenca") // URL para o relatório de presença
public class RelatorioServlet extends HttpServlet {

    private RelatorioDAO relatorioDAO = new RelatorioDAO();
    private Gson gson;

    @Override
    public void init() {
        // Configura o GSON para lidar corretamente com java.sql.Date
        // e também para serializar os objetos Corista e Agenda dentro de Presenca
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd") // Formato esperado para as datas
                // .excludeFieldsWithoutExposeAnnotation() // Descomente se usar @Expose nas entidades
                .create();
    }

    /**
     * Processa o pedido GET para buscar presenças por intervalo de datas.
     * Espera parâmetros 'dataInicio' e 'dataFim' no formato yyyy-MM-dd.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obter parâmetros da URL
        String dataInicioStr = request.getParameter("dataInicio");
        String dataFimStr = request.getParameter("dataFim");

        // 2. Validar e converter parâmetros para java.sql.Date
        Date dataInicio = null;
        Date dataFim = null;
        try {
            if (dataInicioStr == null || dataInicioStr.isEmpty() || dataFimStr == null || dataFimStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetros 'dataInicio' e 'dataFim' são obrigatórios (formato yyyy-MM-dd).");
                return;
            }
            // valueOf converte 'yyyy-MM-dd' para java.sql.Date
            dataInicio = Date.valueOf(dataInicioStr);
            dataFim = Date.valueOf(dataFimStr);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de data inválido. Use yyyy-MM-dd.");
            return;
        }

        // 3. Chamar o DAO para buscar os dados
        List<Presenca> presencas = Collections.emptyList(); // Começa com lista vazia
        try {
            presencas = relatorioDAO.findPresencasByDateRange(dataInicio, dataFim);
        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no RelatorioServlet.doGet ###");
            e.printStackTrace(); // Log detalhado no servidor
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao buscar dados do relatório: " + e.getMessage());
            return;
        }

        // 4. Converter resultado para JSON e enviar resposta
        String json = this.gson.toJson(presencas);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}