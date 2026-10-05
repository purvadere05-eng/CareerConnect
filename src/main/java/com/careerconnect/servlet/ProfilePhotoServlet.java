package com.careerconnect.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.careerconnect.dao.StudentDao;
import com.careerconnect.entity.Student;
import com.careerconnect.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/profile-photo")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ProfilePhotoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Photo storage location
    private static final String UPLOAD_DIR =
            "C:\\CareerConnectUploads\\profile-images";

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("========== PROFILE PHOTO UPLOAD ==========");

        // =========================================
        // 1. CHECK SESSION
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
        // 2. GET LOGGED-IN USER
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

        System.out.println(
                "Logged-in user: "
                + user.getUsername()
        );


        // =========================================
        // 3. GET STUDENT
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
        // 4. GET UPLOADED FILE
        // =========================================

        Part filePart =
                request.getPart("profilePhoto");

        if (filePart == null ||
                filePart.getSize() == 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please select a profile photo."
            );

            return;
        }


        // =========================================
        // 5. CHECK FILE SIZE
        // =========================================

        if (filePart.getSize() >
                5 * 1024 * 1024) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "File size must be less than 5 MB."
            );

            return;
        }


        // =========================================
        // 6. CHECK CONTENT TYPE
        // =========================================

        String contentType =
                filePart.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png"))) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Only JPG, JPEG and PNG images are allowed."
            );

            return;
        }


        // =========================================
        // 7. CREATE UPLOAD DIRECTORY
        // =========================================

        Path uploadPath =
                Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(uploadPath);
        }


        // =========================================
        // 8. CREATE UNIQUE FILE NAME
        // =========================================

        String extension;

        if (contentType.equals("image/png")) {

            extension = ".png";

        } else {

            extension = ".jpg";
        }


        String fileName =
                "user_"
                + student.getId()
                + "_"
                + UUID.randomUUID()
                + extension;


        // =========================================
        // 9. SAVE FILE
        // =========================================

        Path filePath =
                uploadPath.resolve(fileName);

        try (InputStream inputStream =
                     filePart.getInputStream()) {

            Files.copy(
                    inputStream,
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }


        // =========================================
        // 10. SAVE FILE NAME IN DATABASE
        // =========================================

        student.setProfilePhoto(fileName);

        boolean updated =
                studentDao.updateStudent(student);


        if (!updated) {

            // Delete uploaded file if DB update fails

            Files.deleteIfExists(filePath);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not save profile photo."
            );

            return;
        }


        // =========================================
        // SUCCESS
        // =========================================

        System.out.println(
                "Profile photo saved: "
                + fileName
        );

        System.out.println(
                "=========================================="
        );


        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );

        response.getWriter().write(
                "{\"message\":\"Profile photo uploaded successfully\"}"
        );
    }
}