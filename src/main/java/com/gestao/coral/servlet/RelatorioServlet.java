package com.gestao.coral.servlet;

import com.gestao.coral.dao.RelatorioDAO;
import com.gestao.coral.model.Presenca;
import com.gestao.coral.model.PresencaMusico;
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
import java.util.ArrayList;
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

    private static class RelatorioItem {
        private String data;
        private String local;
        private String nomeParticipante;
        private String tipoParticipante;
        private String presente;

        public RelatorioItem(Presenca p) {
            if (p.getAgenda() != null) {
                this.data = (p.getAgenda().getData() != null) ? p.getAgenda().getData().toString() : "N/D";
                this.local = p.getAgenda().getLocal();
            }
            if (p.getCorista() != null) {
                this.nomeParticipante = p.getCorista().getNome();
                this.tipoParticipante = "Corista (" + (p.getCorista().getTipoVoz() != null ? p.getCorista().getTipoVoz() : "N/D") + ")";
            }
            this.presente = p.isPresente() ? "Sim" : "Não";
        }

        public RelatorioItem(PresencaMusico p) {
            if (p.getAgenda() != null) {
                this.data = (p.getAgenda().getData() != null) ? p.getAgenda().getData().toString() : "N/D";
                this.local = p.getAgenda().getLocal();
            }
            if (p.getMusico() != null) {
                this.nomeParticipante = p.getMusico().getNome();
                this.tipoParticipante = "Músico (" + (p.getMusico().getInstrumento() != null ? p.getMusico().getInstrumento() : "N/D") + ")";
            }
            this.presente = p.isPresente() ? "Sim" : "Não";
        }
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

        List<RelatorioItem> relatorioCombinado = new ArrayList<>();

        try {
            List<Presenca> presencasCoristas = relatorioDAO.findPresencasByDateRange(dataInicio, dataFim);
            for (Presenca p : presencasCoristas) {
                relatorioCombinado.add(new RelatorioItem(p));
            }

            List<PresencaMusico> presencasMusicos = relatorioDAO.findPresencasMusicosByDateRange(dataInicio, dataFim);
            for (PresencaMusico p : presencasMusicos) {
                relatorioCombinado.add(new RelatorioItem(p));
            }

            relatorioCombinado.sort((item1, item2) -> {
                int dateCompare = item2.data.compareTo(item1.data);
                if (dateCompare == 0) {
                    return item1.nomeParticipante.compareTo(item2.nomeParticipante);
                }
                return dateCompare;
            });

        } catch (Exception e) {
            System.err.println("### ERRO GRAVE no RelatorioServlet.doGet ###");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao buscar dados do relatório: " + e.getMessage());
            return;
        }

        String json = this.gson.toJson(relatorioCombinado);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}