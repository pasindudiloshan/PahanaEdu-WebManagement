package com.pahanaedu.controller;

import com.pahanaedu.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

@WebServlet("/customerImage")
public class CustomerImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String account = request.getParameter("account");

        if (account == null || account.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing customer account");
            return;
        }

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT image FROM customers WHERE account_number = ?")) {

            pst.setString(1, account);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    InputStream imageStream = rs.getBinaryStream("image");
                    if (imageStream != null) {
                        response.setContentType("image/jpeg");

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = imageStream.read(buffer)) != -1) {
                            response.getOutputStream().write(buffer, 0, bytesRead);
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "No image found for customer");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading customer image");
        }
    }
}
