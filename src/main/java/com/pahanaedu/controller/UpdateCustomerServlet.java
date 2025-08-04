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
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB
public class UpdateCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String accountNumber = request.getParameter("account_number");
            String fullName = request.getParameter("full_name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String postalCode = request.getParameter("postal_code");

            Part photoPart = request.getPart("photo");
            byte[] imageBytes = null;

            if (photoPart != null && photoPart.getSize() > 0) {
                try (InputStream input = photoPart.getInputStream()) {
                    imageBytes = input.readAllBytes();
                }
            }

            Customer customer = new Customer();
            customer.setAccountNumber(accountNumber);
            customer.setFullName(fullName);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setCity(city);
            customer.setPostalCode(postalCode);
            customer.setImage(imageBytes); // may be null

            boolean success = CustomerDao.updateCustomer(customer);

            if (success) {
                response.sendRedirect("customermanagement.jsp?status=update_success");
            } else {
                response.sendRedirect("customermanagement.jsp?status=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customermanagement.jsp?status=error");
        }
    }
}
