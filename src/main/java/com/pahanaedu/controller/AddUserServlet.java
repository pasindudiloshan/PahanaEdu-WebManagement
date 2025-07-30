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
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class AddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

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

        // Collect form data
        String uempid = request.getParameter("uempid");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        String contact = request.getParameter("contact");
        String role = request.getParameter("role");

        // File upload
        Part filePart = request.getPart("photo");
        InputStream inputStream = null;
        if (filePart != null && filePart.getSize() > 0) {
            inputStream = filePart.getInputStream();
        }

        // Validation
        if (uempid == null || uempid.trim().isEmpty() ||
            name == null || email == null || password == null || contact == null || role == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() ||
            contact.trim().isEmpty() || role.trim().isEmpty()) {

            request.setAttribute("status", "failed");
            request.getRequestDispatcher("adduser.jsp").forward(request, response);
            return;
        }

        // Duplicate checks
        if (UserDao.isEmailExists(email)) {
            request.setAttribute("status", "email_exists");
            request.getRequestDispatcher("adduser.jsp").forward(request, response);
            return;
        }

        if (UserDao.isUempidExists(uempid)) {
            request.setAttribute("status", "uempid_exists");
            request.getRequestDispatcher("adduser.jsp").forward(request, response);
            return;
        }

        // Create and save user
        User newUser = new User(uempid, name, email, password, contact, role, inputStream);
        boolean success = UserDao.addUser(newUser);

        request.setAttribute("status", success ? "success" : "failed");
        request.getRequestDispatcher("adduser.jsp").forward(request, response);
    }
}
