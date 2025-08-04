package com.pahanaedu.controller;

import com.pahanaedu.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

@WebServlet("/userImage")
public class UserImageServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is required");
            return;
        }

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT uphoto FROM users WHERE uemail = ?")) {

            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Blob photoBlob = rs.getBlob("uphoto");
                    if (photoBlob != null) {
                        response.setContentType("image/jpeg"); // You may want to store mime type in DB for flexibility
                        try (InputStream is = photoBlob.getBinaryStream();
                             OutputStream os = response.getOutputStream()) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                os.write(buffer, 0, bytesRead);
                            }
                        }
                    } else {
                        // No photo found, send 404 or default image here if preferred
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
