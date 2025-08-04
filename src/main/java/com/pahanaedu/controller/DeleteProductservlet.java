package com.pahanaedu.controller;

import com.pahanaedu.dao.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteProduct")
public class DeleteProductservlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            // Redirect with failure status
            response.sendRedirect("productmanagement.jsp?status=failed");
            return;
        }
        
        try {
            int productId = Integer.parseInt(idParam);
            
            boolean deleted = ProductDao.deleteProductById(productId);
            
            if (deleted) {
                response.sendRedirect("productmanagement.jsp?status=delete_success");
            } else {
                response.sendRedirect("productmanagement.jsp?status=failed");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("productmanagement.jsp?status=failed");
        }
    }
}
