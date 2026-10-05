package com.careerconnect.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.careerconnect.dao.UserDao;
import com.careerconnect.entity.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username =
                request.getParameter("username");

        String password =
                request.getParameter("password");


        UserDao userDao = new UserDao();

        User user =
                userDao.login(username, password);
        if (user != null) {

            HttpSession session = request.getSession();

            session.setAttribute("user", user);

            System.out.println("========== LOGIN DEBUG ==========");
            System.out.println("SESSION ID: " + session.getId());
            System.out.println("USERNAME: " + user.getUsername());
            System.out.println("ROLE: " + user.getRole());
            System.out.println("=================================");

            if ("ADMIN".equals(user.getRole())) {

                response.sendRedirect("admin-dashboard.html");

            } else {

                response.sendRedirect("student-dashboard.html");
            }


        } else {

            response.getWriter()
                    .println("Invalid username or password.");
        }
    }
}