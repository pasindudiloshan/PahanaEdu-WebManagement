package com.pahanaedu.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.pahanaedu.dao.UserDao;
import com.pahanaedu.model.User;

@WebServlet("/adduser")
@MultipartConfig(maxFileSize = 16177215) // 16MB max
public class AddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Session and role check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User loggedUser = (User) session.getAttribute("user");
        if (!"Admin".equalsIgnoreCase(loggedUser.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        // Get form fields
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        String contact = request.getParameter("contact");
        String role = request.getParameter("role");

        // Handle uploaded photo
        Part filePart = request.getPart("photo");
        InputStream inputStream = null;
        if (filePart != null && filePart.getSize() > 0) {
            inputStream = filePart.getInputStream();
        }

        // Input validation
        if (name == null || email == null || password == null || contact == null || role == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() ||
            contact.trim().isEmpty() || role.trim().isEmpty()) {

            request.setAttribute("status", "failed");
            request.getRequestDispatcher("adduser.jsp").forward(request, response);
            return;
        }

        // Check for duplicate email
        if (UserDao.isEmailExists(email)) {
            request.setAttribute("status", "email_exists");
            request.getRequestDispatcher("adduser.jsp").forward(request, response);
            return;
        }

        // Create new User object (password is plain text here; hash it in production)
        User newUser = new User(name, email, password, contact, role, inputStream);

        boolean success = UserDao.addUser(newUser);

        if (success) {
            request.setAttribute("status", "success");
        } else {
            request.setAttribute("status", "failed");
        }

        request.getRequestDispatcher("adduser.jsp").forward(request, response);
    }
}