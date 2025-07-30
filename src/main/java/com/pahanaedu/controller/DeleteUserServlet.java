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

        String uempid = request.getParameter("uempid");
        String status;

        if (uempid != null && !uempid.trim().isEmpty()) {
            boolean isDeleted = UserDao.deleteUserByUempid(uempid);
            status = isDeleted ? "delete_success" : "failed";
        } else {
            status = "invalid";
        }

        response.sendRedirect("usermanagement.jsp?status=" + status);
    }
}
