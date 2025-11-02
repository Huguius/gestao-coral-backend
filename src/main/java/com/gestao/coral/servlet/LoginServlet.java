package com.gestao.coral.servlet;
import com.gestao.coral.dao.UsuarioDAO;
import com.gestao.coral.model.Usuario;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; 
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.stream.Collectors;


@WebServlet("/api/auth/login") 
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        
        LoginRequest loginData = gson.fromJson(jsonPayload, LoginRequest.class);

        Usuario usuarioAutenticado = usuarioDAO.authenticate(loginData.username, loginData.password);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (usuarioAutenticado != null) {
            
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", usuarioAutenticado.getId());
            session.setAttribute("username", usuarioAutenticado.getUsername());

            LoginResponse successResponse = new LoginResponse(true, "Login bem-sucedido!", usuarioAutenticado.getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(gson.toJson(successResponse));

        } else {
            
            LoginResponse errorResponse = new LoginResponse(false, "Usuário ou senha inválidos.", null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
            response.getWriter().print(gson.toJson(errorResponse));
        }
        response.getWriter().flush();
    }

    
    private static class LoginRequest {
        String username;
        String password;
    }

    private static class LoginResponse {
        boolean success;
        String message;
        String username;

        LoginResponse(boolean success, String message, String username) {
            this.success = success;
            this.message = message;
            this.username = username;
        }
    }
}