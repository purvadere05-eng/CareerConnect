package com.careerconnect.servlet;

import java.io.IOException;

import com.careerconnect.dao.StudentDao;
import com.careerconnect.entity.Student;
import com.careerconnect.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ObjectMapper objectMapper =
            new ObjectMapper();


    // =========================
    // GET PROFILE
    // =========================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if (session == null ||
                session.getAttribute("user") == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }


        User user =
                (User) session.getAttribute("user");


        StudentDao studentDao =
                new StudentDao();


        Student student =
                studentDao.getStudentByUserId(
                        user.getId()
                );


        if (student == null) {

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }


        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        response.getWriter().write(
                objectMapper.writeValueAsString(student)
        );
    }


    // =========================
    // UPDATE PROFILE
    // =========================

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if (session == null ||
                session.getAttribute("user") == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }


        User user =
                (User) session.getAttribute("user");


        StudentDao studentDao =
                new StudentDao();


        Student student =
                studentDao.getStudentByUserId(
                        user.getId()
                );


        if (student == null) {

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }


        Student updatedStudent =
                objectMapper.readValue(
                        request.getReader(),
                        Student.class
                );


        student.setFullName(
                updatedStudent.getFullName()
        );

        student.setEmail(
                updatedStudent.getEmail()
        );

        student.setPhone(
                updatedStudent.getPhone()
        );

        student.setQualification(
                updatedStudent.getQualification()
        );

        student.setSkills(
                updatedStudent.getSkills()
        );


        boolean updated =
                studentDao.updateStudent(student);


        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        if (updated) {

            response.getWriter().write(
                    "{\"message\":\"Profile updated successfully\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write(
                    "{\"message\":\"Profile update failed\"}"
            );
        }
    }
}