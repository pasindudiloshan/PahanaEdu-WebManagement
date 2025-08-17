package com.pahanaedu.controller;

import com.pahanaedu.dao.ProductDao;
import com.pahanaedu.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/UpdateProduct")
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class UpdateProductServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        request.setCharacterEncoding("UTF-8");

	        int id = Integer.parseInt(request.getParameter("id"));
	        String itemId = request.getParameter("itemid");
	        String name = request.getParameter("name");
	        String description = request.getParameter("description");
	        String category = request.getParameter("category");  // NEW
	        double price = Double.parseDouble(request.getParameter("price"));
	        int quantity = Integer.parseInt(request.getParameter("quantity"));

	        Part photoPart = request.getPart("photo");
	        InputStream imageStream = (photoPart != null && photoPart.getSize() > 0)
	                ? photoPart.getInputStream() : null;

	        Product product = new Product();
	        product.setId(id);
	        product.setItemId(itemId);
	        product.setName(name);
	        product.setDescription(description);
	        product.setCategory(category);     // NEW
	        product.setPrice(price);
	        product.setQuantity(quantity);

	        boolean updated = ProductDao.updateProduct(product, imageStream);
	        if (updated) {
	            response.sendRedirect("productmanagement.jsp?status=update_success");
	        } else {
	            response.sendRedirect("productmanagement.jsp?status=failed");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendRedirect("productmanagement.jsp?status=error");
	    }
	}
}
