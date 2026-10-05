package com.careerconnect.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.careerconnect.dao.ApplicationDao;
import com.careerconnect.dao.JobDao;
import com.careerconnect.dao.StudentDao;
import com.careerconnect.entity.Application;
import com.careerconnect.entity.Job;
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

@WebServlet("/student/submit-application")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class SubmitApplicationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ApplicationDao applicationDao =
            new ApplicationDao();

    private StudentDao studentDao =
            new StudentDao();

    private JobDao jobDao =
            new JobDao();

    private static final String UPLOAD_DIR =
            "C:\\CareerConnectUploads\\application-resumes";


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(
                "========== SUBMIT APPLICATION =========="
        );


        // ==========================================
        // 1. CHECK SESSION
        // ==========================================

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please login first."
            );

            return;
        }


        // ==========================================
        // 2. GET LOGGED-IN USER
        // ==========================================

        User user =
                (User) session.getAttribute("user");

        if (user == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please login first."
            );

            return;
        }


        // ==========================================
        // 3. CHECK STUDENT ROLE
        // ==========================================

        if (!"STUDENT".equals(user.getRole())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Student access required."
            );

            return;
        }


        // ==========================================
        // 4. GET JOB ID
        // ==========================================

        String jobIdParameter =
                request.getParameter("jobId");

        if (jobIdParameter == null ||
                jobIdParameter.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Job ID is required."
            );

            return;
        }

        int jobId;

        try {

            jobId =
                    Integer.parseInt(jobIdParameter);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid Job ID."
            );

            return;
        }


        // ==========================================
        // 5. GET STUDENT
        // ==========================================

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


        // ==========================================
        // 6. GET JOB
        // ==========================================

        Job job =
                jobDao.getJobById(jobId);

        if (job == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Job not found."
            );

            return;
        }


        // ==========================================
        // 7. CHECK DUPLICATE APPLICATION
        // ==========================================

        if (applicationDao.hasAlreadyApplied(
                student.getId(),
                jobId)) {

            response.setStatus(
                    HttpServletResponse.SC_CONFLICT
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{\"message\":\"You have already applied for this job.\"}"
            );

            return;
        }


        // ==========================================
        // 8. GET COVER LETTER
        // ==========================================

        String coverLetter =
                request.getParameter("coverLetter");

        if (coverLetter == null ||
                coverLetter.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Cover letter is required."
            );

            return;
        }


        // ==========================================
        // 9. GET RESUME
        // ==========================================

        Part resumePart =
                request.getPart("resume");

        if (resumePart == null ||
                resumePart.getSize() == 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please upload your resume."
            );

            return;
        }


        // ==========================================
        // 10. CHECK FILE SIZE
        // ==========================================

        if (resumePart.getSize() >
                5 * 1024 * 1024) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Resume must be less than 5 MB."
            );

            return;
        }


        // ==========================================
        // 11. CHECK FILE TYPE
        // ==========================================

        String contentType =
                resumePart.getContentType();

        String originalFileName =
                resumePart.getSubmittedFileName();

        if (contentType == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid resume file."
            );

            return;
        }


        boolean validType =
                contentType.equals("application/pdf")
                || contentType.equals(
                        "application/msword")
                || contentType.equals(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document");


        if (!validType) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Only PDF, DOC and DOCX resumes are allowed."
            );

            return;
        }


        // ==========================================
        // 12. CREATE UPLOAD DIRECTORY
        // ==========================================

        Path uploadPath =
                Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(uploadPath);
        }


        // ==========================================
        // 13. CREATE UNIQUE FILE NAME
        // ==========================================

        String extension = "";

        if (originalFileName != null &&
                originalFileName.contains(".")) {

            extension =
                    originalFileName.substring(
                            originalFileName.lastIndexOf(".")
                    );
        }


        String fileName =
                "student_"
                + student.getId()
                + "_"
                + UUID.randomUUID()
                + extension;


        // ==========================================
        // 14. SAVE RESUME
        // ==========================================

        Path filePath =
                uploadPath.resolve(fileName);

        try (InputStream inputStream =
                     resumePart.getInputStream()) {

            Files.copy(
                    inputStream,
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }


        // ==========================================
        // 15. CREATE APPLICATION
        // ==========================================

        Application application =
                new Application(student, job);

        application.setCoverLetter(
                coverLetter.trim()
        );

        application.setResumeFileName(
                fileName
        );


        // ==========================================
        // 16. SAVE APPLICATION
        // ==========================================

        boolean saved =
                applicationDao.saveApplication(
                        application
                );


        if (!saved) {

            // Delete uploaded resume
            Files.deleteIfExists(filePath);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to submit application."
            );

            return;
        }


        // ==========================================
        // SUCCESS
        // ==========================================

        System.out.println(
                "Application submitted successfully."
        );

        System.out.println(
                "Student = "
                + student.getFullName()
        );

        System.out.println(
                "Job = "
                + job.getTitle()
        );

        System.out.println(
                "Resume = "
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
                "{\"message\":\"Application submitted successfully!\"}"
        );
    }
}