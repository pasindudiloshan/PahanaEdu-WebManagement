package com.pahanaedu.controller;

import java.io.InputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.pahanaedu.dao.UserDao;
import com.pahanaedu.model.User;

@WebServlet("/UpdateUserServlet")
@MultipartConfig(maxFileSize = 16177215)  // 16MB max upload
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String originalEmail = request.getParameter("originalEmail");  // Hidden field from form
        String name = request.getParameter("name");
        String newEmail = request.getParameter("email");
        String role = request.getParameter("role");
        String contact = request.getParameter("contact");

        Part photoPart = request.getPart("photo");
        InputStream photoStream = null;
        if (photoPart != null && photoPart.getSize() > 0) {
            photoStream = photoPart.getInputStream();
        }

        User updatedUser = new User();
        updatedUser.setName(name);
        updatedUser.setEmail(newEmail);
        updatedUser.setMobile(contact);
        updatedUser.setRole(role);

        boolean updated = UserDao.updateUser(originalEmail, updatedUser, photoStream);

        if (updated) {
            response.sendRedirect("usermanagement.jsp?status=success");
        } else {
            response.sendRedirect("usermanagement.jsp?status=failed");
        }
    }
}

