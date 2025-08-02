package com.pahanaedu.controller;

import com.pahanaedu.dao.ProductDao;
import com.pahanaedu.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/AddProduct")
@MultipartConfig(maxFileSize = 16177215)  // ~16MB
public class AddProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get all parameters including category
            String itemId = request.getParameter("itemid");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String category = request.getParameter("category");  // NEW
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Part imagePart = request.getPart("photo");
            InputStream imageStream = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                imageStream = imagePart.getInputStream();
            } else {
                response.sendRedirect("addproduct.jsp?status=no_image");
                return;
            }

            Product product = new Product();
            product.setItemId(itemId);
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);  // NEW
            product.setPrice(price);
            product.setQuantity(quantity);

            boolean success = ProductDao.addProduct(product, imageStream);

            if (success) {
                response.sendRedirect("addproduct.jsp?status=success");
            } else {
                response.sendRedirect("addproduct.jsp?status=failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("addproduct.jsp?status=invalid_input");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addproduct.jsp?status=failed");
        }
    }
}
