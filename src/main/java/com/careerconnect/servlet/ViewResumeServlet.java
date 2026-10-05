package com.careerconnect.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.careerconnect.entity.User;

@WebServlet("/admin/resume")
public class ViewResumeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR =
            "C:\\CareerConnectUploads\\applications";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Check session
        HttpSession session =
                request.getSession(false);

        if (session == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please login first."
            );
            return;
        }

        // Check admin
        User user =
                (User) session.getAttribute("user");

        if (user == null ||
                !"ADMIN".equals(user.getRole())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );
            return;
        }

        // Get file name
        String fileName =
                request.getParameter("file");
        System.out.println("DEBUG RESUME FILE: " + fileName);

        if (fileName == null ||
                fileName.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Resume file name is required."
            );
            return;
        }

        // Security: only use the file name
        fileName =
                Paths.get(fileName)
                        .getFileName()
                        .toString();

        // Create file path
        Path filePath =
                Paths.get(UPLOAD_DIR)
                        .resolve(fileName);
        
        System.out.println("DEBUG UPLOAD DIR: " + UPLOAD_DIR);
        System.out.println("DEBUG FILE NAME: " + fileName);
        System.out.println("DEBUG FILE PATH: " + filePath);
        System.out.println("DEBUG FILE EXISTS: " + Files.exists(filePath));
        

        // Check file exists
        if (!Files.exists(filePath)) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Resume not found."
            );
            return;
        }

        // Set content type
        String contentType =
                Files.probeContentType(filePath);

        if (contentType == null) {
            contentType =
                    "application/octet-stream";
        }

        response.setContentType(contentType);

        // Open in browser
        response.setHeader(
                "Content-Disposition",
                "inline; filename=\"" +
                fileName +
                "\""
        );

        // Send file
        try (OutputStream outputStream =
                     response.getOutputStream()) {

            Files.copy(
                    filePath,
                    outputStream
            );
        }
    }
}