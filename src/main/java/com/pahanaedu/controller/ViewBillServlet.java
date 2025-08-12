package com.pahanaedu.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pahanaedu.dao.BillDao;
import com.pahanaedu.model.Bill;
import com.pahanaedu.util.DBUtil;

@WebServlet("/ViewBillServlet")
public class ViewBillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String idParam = request.getParameter("id");

        if (idParam == null || !idParam.matches("\\d+")) {
            // Invalid or missing id parameter
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bill ID.");
            return;
        }

        int billId = Integer.parseInt(idParam);
        try (Connection conn = DBUtil.getConnection()) {
            BillDao dao = new BillDao(conn);
            Bill bill = dao.getBillById(billId);

            if (bill == null) {
                // Bill not found
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bill not found.");
                return;
            }

            request.setAttribute("bill", bill);
            request.setAttribute("billItems", dao.getBillItems(billId));
            request.getRequestDispatcher("/viewbill.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error loading bill", e);
        }
    }
}
