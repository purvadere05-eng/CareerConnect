package com.careerconnect.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.careerconnect.dao.RegistrationDao;
import com.careerconnect.entity.Student;
import com.careerconnect.entity.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String fullName =
                request.getParameter("fullName");

        String username =
                request.getParameter("username");

        String password =
                request.getParameter("password");

        String email =
                request.getParameter("email");

        String phone =
                request.getParameter("phone");

        String qualification =
                request.getParameter("qualification");

        String skills = "";

        User user =
                new User(username, password, "STUDENT");

        Student student =
                new Student(
                    fullName,
                    email,
                    phone,
                    qualification,
                    "",
                    user
                );

        RegistrationDao registrationDao =
                new RegistrationDao();

        boolean registered =
                registrationDao.registerStudent(user, student);

        if (registered) {

            response.sendRedirect("login.html?role=STUDENT");

        } else {

            response.getWriter()
                    .println("Registration failed.");
        }
    }
}