package com.pahanaedu.controller;

import com.pahanaedu.dao.UserDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String status;

        if (email != null && !email.trim().isEmpty()) {
            boolean isDeleted = UserDao.deleteUserByEmail(email);
            status = isDeleted ? "delete_success" : "failed";
        } else {
            status = "invalid";
        }

        response.sendRedirect("usermanagement.jsp?status=" + status);
    }
}

