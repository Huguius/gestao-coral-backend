package com.gestao.coral.servlet;
import com.gestao.coral.dao.RelatorioDAO;
import com.gestao.coral.model.Presenca;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date; 
import java.util.Collections; 
import java.util.List;

@WebServlet("/api/relatorios/presenca") 
public class RelatorioServlet extends HttpServlet {

    private RelatorioDAO relatorioDAO = new RelatorioDAO();
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

        String dataInicioStr = request.getParameter("dataInicio");
        String dataFimStr = request.getParameter("dataFim");

        Date dataInicio = null;
        Date dataFim = null;
        try {
            if (dataInicioStr == null || dataInicioStr.isEmpty() || dataFimStr == null || dataFimStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetros 'dataInicio' e 'dataFim' são obrigatórios (formato yyyy-MM-dd).");
                return;
            }
            
            dataInicio = Date.valueOf(dataInicioStr);
            dataFim = Date.valueOf(dataFimStr);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de data inválido. Use yyyy-MM-dd.");
            return;
        }

        List<Presenca> presencas = Collections.emptyList(); 
        try {
            presencas = relatorioDAO.findPresencasByDateRange(dataInicio, dataFim);
        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no RelatorioServlet.doGet ###");
            e.printStackTrace(); 
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao buscar dados do relatório: " + e.getMessage());
            return;
        }

        String json = this.gson.toJson(presencas);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}