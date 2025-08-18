package com.pahanaedu.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ValidateOtp")
public class ValidateOtp extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String enteredOtp = request.getParameter("otp");
        HttpSession session = request.getSession();
        String sessionOtp = String.valueOf(session.getAttribute("otp"));

        RequestDispatcher dispatcher = null;

        if (enteredOtp != null && enteredOtp.equals(sessionOtp)) {

            session.setAttribute("otpStatus", "success");
            session.setAttribute("otpMessage", "OTP verified successfully. You can reset your password now.");
            dispatcher = request.getRequestDispatcher("newPassword.jsp");


            session.removeAttribute("otp");

        } else {

            request.setAttribute("status", "error");
            request.setAttribute("message", "Invalid OTP. Please try again.");
            dispatcher = request.getRequestDispatcher("EnterOtp.jsp");
        }

        dispatcher.forward(request, response);
    }
}


