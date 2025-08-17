package com.pahanaedu.controller;

import com.pahanaedu.dao.CustomerDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/DeleteCustomer")
public class DeleteCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String account = request.getParameter("account");

        if (account == null || account.trim().isEmpty()) {
            response.sendRedirect("customermanagement.jsp?status=invalid");
            return;
        }

        boolean success = CustomerDao.deleteCustomer(account);

        if (success) {
            response.sendRedirect("customermanagement.jsp?status=delete_success");
        } else {
            response.sendRedirect("customermanagement.jsp?status=failed");
        }
    }
}
