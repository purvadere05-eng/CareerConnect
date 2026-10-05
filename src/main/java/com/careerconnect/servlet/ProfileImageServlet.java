package com.careerconnect.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.careerconnect.dao.StudentDao;
import com.careerconnect.entity.Student;
import com.careerconnect.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile-image")
public class ProfileImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR =
            "C:\\CareerConnectUploads\\profile-images";


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // =========================================
        // CHECK SESSION
        // =========================================

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please login first."
            );

            return;
        }


        // =========================================
        // GET LOGGED-IN USER
        // =========================================

        User user =
                (User) session.getAttribute("user");

        if (user == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please login first."
            );

            return;
        }


        // =========================================
        // GET STUDENT
        // =========================================

        StudentDao studentDao =
                new StudentDao();

        Student student =
                studentDao.getStudentByUserId(
                        user.getId()
                );

        if (student == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Student profile not found."
            );

            return;
        }


        // =========================================
        // GET PHOTO FILE NAME
        // =========================================

        String fileName =
                student.getProfilePhoto();

        if (fileName == null ||
                fileName.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Profile photo not found."
            );

            return;
        }


        // =========================================
        // GET FILE
        // =========================================

        Path filePath =
                Paths.get(UPLOAD_DIR)
                     .resolve(fileName)
                     .normalize();


        // Security check
        Path uploadPath =
                Paths.get(UPLOAD_DIR)
                     .toAbsolutePath()
                     .normalize();

        if (!filePath.toAbsolutePath()
                .startsWith(uploadPath)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return;
        }


        if (!Files.exists(filePath)) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Profile photo file not found."
            );

            return;
        }


        // =========================================
        // SET CONTENT TYPE
        // =========================================

        String contentType =
                Files.probeContentType(filePath);

        if (contentType == null) {

            contentType = "image/jpeg";
        }

        response.setContentType(contentType);


        // =========================================
        // SEND IMAGE TO BROWSER
        // =========================================

        try (InputStream inputStream =
                     Files.newInputStream(filePath)) {

            inputStream.transferTo(
                    response.getOutputStream()
            );
        }
    }
}