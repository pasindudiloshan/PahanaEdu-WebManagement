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
            // Trim inputs
            String itemId = request.getParameter("itemid").trim();
            String name = request.getParameter("name").trim();
            String description = request.getParameter("description").trim();
            String category = request.getParameter("category").trim();
            String priceStr = request.getParameter("price").trim();
            String quantityStr = request.getParameter("quantity").trim();

            // Validate item ID format
            if (!itemId.matches("PEB-ITEM-\\d{3,}")) {
                response.sendRedirect("addproduct.jsp?status=invalid_input");
                return;
            }

            // Check for duplicate item ID
            if (ProductDao.itemIdExists(itemId)) {
                response.sendRedirect("addproduct.jsp?status=duplicate");
                return;
            }

            // Validate price and quantity
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            if (price < 0 || quantity < 0) {
                response.sendRedirect("addproduct.jsp?status=invalid_input");
                return;
            }

            // Validate image
            Part imagePart = request.getPart("photo");
            InputStream imageStream = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                String contentType = imagePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    response.sendRedirect("addproduct.jsp?status=invalid_image");
                    return;
                }
                imageStream = imagePart.getInputStream();
            } else {
                response.sendRedirect("addproduct.jsp?status=no_image");
                return;
            }

            // Create Product object
            Product product = new Product();
            product.setItemId(itemId);
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);
            product.setPrice(price);
            product.setQuantity(quantity);

            // Add product
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

