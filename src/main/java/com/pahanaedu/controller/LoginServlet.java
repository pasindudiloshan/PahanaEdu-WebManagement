package com.pahanaedu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.pahanaedu.dao.UserDao;
import com.pahanaedu.model.User;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uemail = request.getParameter("username");
        String upwd = request.getParameter("password");

        if (uemail == null || uemail.isEmpty() || upwd == null || upwd.isEmpty()) {
            request.setAttribute("status", "failed");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        User user = UserDao.validateUser(uemail, upwd);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);  // Store entire user object
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("status", "failed");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
