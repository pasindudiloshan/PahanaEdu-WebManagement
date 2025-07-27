package com.pahanaedu.controller;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.pahanaedu.util.DBUtil;

@WebServlet("/UpdateUserServlet")
@MultipartConfig(maxFileSize = 16177215)  // 16MB max upload
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String role = request.getParameter("role");
        String contact = request.getParameter("contact");

        InputStream photoStream = null;
        Part photoPart = request.getPart("photo");
        boolean isPhotoUploaded = photoPart != null && photoPart.getSize() > 0;

        if (isPhotoUploaded) {
            photoStream = photoPart.getInputStream();
        }

        try (Connection con = DBUtil.getConnection()) {
            String sql;
            PreparedStatement pst;

            if (isPhotoUploaded) {
                sql = "UPDATE users SET uname = ?, umobile = ?, urole = ?, uphoto = ? WHERE uemail = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, contact);
                pst.setString(3, role);
                pst.setBlob(4, photoStream);
                pst.setString(5, email);
            } else {
                sql = "UPDATE users SET uname = ?, umobile = ?, urole = ? WHERE uemail = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, contact);
                pst.setString(3, role);
                pst.setString(4, email);
            }

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("usermanagement.jsp?status=success");
            } else {
                response.sendRedirect("usermanagement.jsp?status=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error during update: " + e.getMessage());
        }
    }
}
