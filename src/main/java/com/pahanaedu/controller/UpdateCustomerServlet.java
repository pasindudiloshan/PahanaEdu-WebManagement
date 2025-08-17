package com.pahanaedu.controller;

import com.pahanaedu.dao.CustomerDao;
import com.pahanaedu.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/UpdateCustomer")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB limit
public class UpdateCustomerServlet extends HttpServlet {
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

            String redirectURL = "editcustomer.jsp?account=" + accountNumber;

            // Check for duplicate email (excluding current account)
            if (email != null && !email.isEmpty() && CustomerDao.emailExistsForOther(accountNumber, email)) {
                redirectURL += "&status=duplicate_email";
                response.sendRedirect(redirectURL);
                return;
            }

            // Get uploaded photo (optional)
            Part photoPart = request.getPart("photo");
            byte[] imageBytes = null;
            if (photoPart != null && photoPart.getSize() > 0) {
                try (InputStream input = photoPart.getInputStream()) {
                    imageBytes = input.readAllBytes();
                }
            }

            // Prepare Customer object
            Customer customer = new Customer();
            customer.setAccountNumber(accountNumber);
            customer.setFullName(fullName);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setCity(city);
            customer.setPostalCode(postalCode);
            customer.setImage(imageBytes); // may be null

            // Update customer
            boolean success = CustomerDao.updateCustomer(customer);

            if (success) {
                redirectURL += "&status=update_success";
            } else {
                redirectURL += "&status=failed";
            }

            response.sendRedirect(redirectURL);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("editcustomer.jsp?account=" + request.getParameter("account_number") + "&status=error");
        }
    }
}


