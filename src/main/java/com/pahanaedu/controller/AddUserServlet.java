package com.pahanaedu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.pahanaedu.dao.UserDao;
import com.pahanaedu.model.User;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/adduser")
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class AddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Allowed roles list - adjust as needed
    private static final List<String> ALLOWED_ROLES = Arrays.asList("Admin", "Manager", "Cashier");

    @Override
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
        String plainPassword = request.getParameter("pass");
        String contact = request.getParameter("contact");
        String role = request.getParameter("role");

        // File upload
        Part filePart = request.getPart("photo");
        InputStream inputStream = null;
        if (filePart != null && filePart.getSize() > 0) {
            inputStream = filePart.getInputStream();
        }

        // Validate required fields
        if (uempid == null || uempid.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            plainPassword == null || plainPassword.trim().isEmpty() ||
            contact == null || contact.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {

            request.setAttribute("status", "failed");
            request.getRequestDispatcher("adduser.jsp").forward(request, response);
            return;
        }

        // Validate role against allowed roles
        if (!ALLOWED_ROLES.contains(role)) {
            request.setAttribute("status", "failed");
            request.getRequestDispatcher("adduser.jsp").forward(request, response);
            return;
        }

        // Check for duplicates
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

        // Hash password
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        // Create user and insert
        User newUser = new User(uempid, name, email, hashedPassword, contact, role, inputStream);
        boolean success = UserDao.addUser(newUser);

        request.setAttribute("status", success ? "success" : "failed");
        request.getRequestDispatcher("adduser.jsp").forward(request, response);
    }
}
