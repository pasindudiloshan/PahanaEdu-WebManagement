package com.pahanaedu.controller;

import com.pahanaedu.dao.CustomerDao;
import com.pahanaedu.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/AddCustomer")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB limit
public class AddCustomerServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String accountNumber = request.getParameter("account_number").trim();
            String fullName = request.getParameter("full_name").trim();
            String email = request.getParameter("email") != null ? request.getParameter("email").trim() : null;
            String phone = request.getParameter("phone") != null ? request.getParameter("phone").trim() : null;
            String address = request.getParameter("address") != null ? request.getParameter("address").trim() : null;
            String city = request.getParameter("city") != null ? request.getParameter("city").trim() : null;
            String postalCode = request.getParameter("postal_code") != null ? request.getParameter("postal_code").trim() : null;

            Part imagePart = request.getPart("photo");
            byte[] imageBytes = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                try (InputStream input = imagePart.getInputStream()) {
                    imageBytes = input.readAllBytes();
                }
            } else {
                response.sendRedirect("addcustomer.jsp?status=no_image");
                return;
            }

            // Check for duplicate account number
            if (CustomerDao.getCustomerByAccount(accountNumber) != null) {
                response.sendRedirect("addcustomer.jsp?status=duplicate");
                return;
            }

            // Prepare customer object
            Customer customer = new Customer();
            customer.setAccountNumber(accountNumber);
            customer.setFullName(fullName);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setCity(city);
            customer.setPostalCode(postalCode);
            customer.setImage(imageBytes);

            boolean result = CustomerDao.addCustomer(customer);

            if (result) {
                response.sendRedirect("addcustomer.jsp?status=success");
            } else {
                response.sendRedirect("addcustomer.jsp?status=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addcustomer.jsp?status=failed");
        }
    }
}

