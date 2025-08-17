package com.pahanaedu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.pahanaedu.dao.UserDao;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/newPassword")
public class NewPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email"); // Email saved in session during OTP
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confPassword");

        if (password != null && password.equals(confPassword)) {
            // Hash the password securely with BCrypt before updating
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            boolean success = UserDao.updatePassword(email, hashedPassword);
            if (success) {
                // Remove session attributes after successful password reset
                session.removeAttribute("email");
                session.removeAttribute("otp");

                // Use session for SweetAlert message
                session.setAttribute("resetStatus", "success");
                session.setAttribute("resetMessage", "Password updated successfully. Please login.");
                
                // Redirect to login.jsp so popup will trigger
                response.sendRedirect("login.jsp");
            } else {
                session.setAttribute("resetStatus", "error");
                session.setAttribute("resetMessage", "Error updating password. Please try again.");
                response.sendRedirect("newPassword.jsp");
            }
        } else {
            // Passwords do not match
            session.setAttribute("resetStatus", "error");
            session.setAttribute("resetMessage", "Passwords do not match.");
            response.sendRedirect("newPassword.jsp");
        }
    }
}

