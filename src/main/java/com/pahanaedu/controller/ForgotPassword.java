package com.pahanaedu.controller;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/forgotPassword")
public class ForgotPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        RequestDispatcher dispatcher = null;

        if(email != null && !email.trim().isEmpty()) {
            Random rand = new Random();
            int otpvalue = 100000 + rand.nextInt(900000); // 6 digit OTP

            HttpSession session = request.getSession();
            session.setAttribute("otp", otpvalue);
            session.setAttribute("email", email);

            // Email sending setup
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("pahanaeducation@gmail.com", "vhyunfosligbmqpy");
                }
            });

            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress("pahanaeducation@gmail.com")); // Sender email
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("OTP for Password Reset");
                message.setText("Your OTP for password reset is: " + otpvalue);
                Transport.send(message);
                System.out.println("OTP email sent successfully.");

                request.setAttribute("message", "OTP sent to your email address.");
                dispatcher = request.getRequestDispatcher("EnterOtp.jsp");
            } catch (MessagingException e) {
                e.printStackTrace();
                request.setAttribute("message", "Error sending OTP email. Try again.");
                dispatcher = request.getRequestDispatcher("forgotPassword.jsp");
            }
        } else {
            request.setAttribute("message", "Please enter a valid email.");
            dispatcher = request.getRequestDispatcher("forgotPassword.jsp");
        }

        dispatcher.forward(request, response);
    }
}


