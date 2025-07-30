package com.pahanaedu.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pahanaedu.dao.UserDao;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/newPassword")
public class NewPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confPassword");

        RequestDispatcher dispatcher = null;

        if (password != null && password.equals(confPassword)) {
            // ✅ Hash the password before saving
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            boolean success = UserDao.updatePassword(email, hashedPassword);
            if (success) {
                session.removeAttribute("email");
                session.removeAttribute("otp");
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("message", "Error updating password. Try again.");
                dispatcher = request.getRequestDispatcher("newPassword.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("message", "Passwords do not match.");
            dispatcher = request.getRequestDispatcher("newPassword.jsp");
            dispatcher.forward(request, response);
        }
    }
}

