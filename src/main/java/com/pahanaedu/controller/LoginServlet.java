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

    private static final int MAX_ATTEMPTS = 4; // maximum allowed login attempts

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uemail = request.getParameter("username");
        String upwd = request.getParameter("password");

        HttpSession session = request.getSession();

        // Get current attempt count (default 0 if not set)
        Integer attempts = (Integer) session.getAttribute("loginAttempts");
        if (attempts == null) {
            attempts = 0;
        }

        // Check if attempts already exceeded
        if (attempts >= MAX_ATTEMPTS) {
            request.setAttribute("status", "locked");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Empty field validation
        if (uemail == null || uemail.isEmpty() || upwd == null || upwd.isEmpty()) {
            request.setAttribute("status", "failed");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Validate user from DB
        User user = UserDao.validateUser(uemail, upwd);

        if (user != null) {
            // Reset attempts on successful login
            session.setAttribute("loginAttempts", 0);

            // ✅ Set user in session
            session.setAttribute("user", user);

            // ✅ Set login success status
            session.setAttribute("status", "success");

            // ✅ Set session timeout (2 minutes for testing)
            session.setMaxInactiveInterval(2 * 60); 

            response.sendRedirect("index.jsp");
        } else {
            // Increase attempts
            attempts++;
            session.setAttribute("loginAttempts", attempts);

            if (attempts >= MAX_ATTEMPTS) {
                request.setAttribute("status", "locked");
            } else {
                request.setAttribute("status", "failed");
            }

            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

