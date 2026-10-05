package com.careerconnect.servlet;

import java.io.IOException;
import java.util.List;

import com.careerconnect.dao.ApplicationDao;
import com.careerconnect.dao.StudentDao;
import com.careerconnect.entity.Application;
import com.careerconnect.entity.Student;
import com.careerconnect.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/student/applications")
public class MyApplicationsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ApplicationDao applicationDao =
            new ApplicationDao();

    private StudentDao studentDao =
            new StudentDao();

    private ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(
                            new JavaTimeModule()
                    );

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(
            "========== MY APPLICATIONS DEBUG =========="
        );


        // 1. Get existing session

        HttpSession session =
                request.getSession(false);

        System.out.println(
            "SESSION = " + session
        );


        if (session == null) {

            System.out.println(
                "SESSION IS NULL"
            );

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Please login first."
            );

            return;
        }


        // 2. Get logged-in user

        User user =
                (User) session.getAttribute("user");

        System.out.println(
            "USER = " + user
        );


        if (user == null) {

            System.out.println(
                "USER IS NULL"
            );

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Please login first."
            );

            return;
        }


        System.out.println(
            "SESSION ID = " + session.getId()
        );

        System.out.println(
            "USERNAME = " + user.getUsername()
        );

        System.out.println(
            "ROLE = " + user.getRole()
        );


        // 3. Check student role

        if (!"STUDENT".equals(user.getRole())) {

            System.out.println(
                "USER IS NOT STUDENT"
            );

            response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Student access required."
            );

            return;
        }


        // 4. Get student profile

        Student student =
                studentDao.getStudentByUserId(
                    user.getId()
                );


        System.out.println(
            "STUDENT = " + student
        );


        if (student == null) {

            System.out.println(
                "STUDENT PROFILE NOT FOUND"
            );

            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Student profile not found."
            );

            return;
        }


        System.out.println(
            "STUDENT ID = " + student.getId()
        );


        // 5. Get applications

        List<Application> applications =
                applicationDao
                    .getApplicationsByStudentId(
                        student.getId()
                    );


        System.out.println(
            "APPLICATION COUNT = " +
            applications.size()
        );


        // 6. Return JSON

        response.setContentType(
            "application/json"
        );

        response.setCharacterEncoding(
            "UTF-8"
        );


        objectMapper.writeValue(
            response.getWriter(),
            applications
        );


        System.out.println(
            "========== MY APPLICATIONS END =========="
        );
    }
}