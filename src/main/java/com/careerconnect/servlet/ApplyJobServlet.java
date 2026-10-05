package com.careerconnect.servlet;

import java.io.IOException;

import com.careerconnect.dao.ApplicationDao;
import com.careerconnect.dao.JobDao;
import com.careerconnect.dao.StudentDao;
import com.careerconnect.entity.Application;
import com.careerconnect.entity.Job;
import com.careerconnect.entity.Student;
import com.careerconnect.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

@WebServlet("/student/apply")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ApplyJobServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private static final String UPLOAD_DIR =
            "C:\\CareerConnectUploads\\applications";

    private ApplicationDao applicationDao =
            new ApplicationDao();

    private JobDao jobDao =
            new JobDao();

    private StudentDao studentDao =
            new StudentDao();


    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session =
                request.getSession(false);


        // 1. Check session

        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Please login first."
            );

            return;
        }


        // 2. Get logged-in user

        User user =
                (User) session.getAttribute("user");


        // 3. Check student role

        if (user == null ||
            !"STUDENT".equals(user.getRole())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Student access required."
            );

            return;
        }


        // 4. Get job ID

        String jobIdParameter =
                request.getParameter("jobId");


        if (jobIdParameter == null) {

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


        // 5. Get Student

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


        // 6. Get Job

        Job job =
                jobDao.getJobById(jobId);


        if (job == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Job not found."
            );

            return;
        }


        // 7. Check duplicate application

        boolean alreadyApplied =
                applicationDao.hasAlreadyApplied(
                        student.getId(),
                        job.getId()
                );


        if (alreadyApplied) {

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


        // 8. Create application

        Application application =
                new Application(
                        student,
                        job
                );
        
     // ==========================================
     // 8A. GET COVER LETTER
     // ==========================================

     String coverLetter =
             request.getParameter("coverLetter");

     if (coverLetter == null) {
         coverLetter = "";
     }

     application.setCoverLetter(coverLetter);


     // ==========================================
     // 8B. GET RESUME
     // ==========================================

     Part resumePart =
             request.getPart("resume");

     if (resumePart == null ||
         resumePart.getSize() == 0) {

         response.setStatus(
                 HttpServletResponse.SC_BAD_REQUEST
         );

         response.setContentType("application/json");

         response.getWriter().write(
                 "{\"message\":\"Please upload your resume.\"}"
         );

         return;
     }


     // ==========================================
     // 8C. CHECK RESUME SIZE
     // ==========================================

     if (resumePart.getSize() > 5 * 1024 * 1024) {

         response.setStatus(
                 HttpServletResponse.SC_BAD_REQUEST
         );

         response.setContentType("application/json");

         response.getWriter().write(
                 "{\"message\":\"Resume size must be less than 5 MB.\"}"
         );

         return;
     }


     // ==========================================
     // 8D. CHECK RESUME TYPE
     // ==========================================

     String contentType =
             resumePart.getContentType();

     if (contentType == null ||
         (!contentType.equals("application/pdf")
         && !contentType.equals("application/msword")
         && !contentType.equals(
             "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

         response.setStatus(
                 HttpServletResponse.SC_BAD_REQUEST
         );

         response.setContentType("application/json");

         response.getWriter().write(
                 "{\"message\":\"Only PDF, DOC and DOCX resumes are allowed.\"}"
         );

         return;
     }


     // ==========================================
     // 8E. CREATE RESUME FOLDER
     // ==========================================

     String uploadDir =
             "C:\\CareerConnectUploads\\applications";

     Path uploadPath =
             Paths.get(uploadDir);

     if (!Files.exists(uploadPath)) {
         Files.createDirectories(uploadPath);
     }


     // ==========================================
     // 8F. CREATE UNIQUE FILE NAME
     // ==========================================

     String extension;

     if (contentType.equals("application/pdf")) {

         extension = ".pdf";

     } else if (contentType.equals("application/msword")) {

         extension = ".doc";

     } else {

         extension = ".docx";
     }


     String resumeFileName =
             "application_"
             + student.getId()
             + "_"
             + job.getId()
             + "_"
             + UUID.randomUUID()
             + extension;


     // ==========================================
     // 8G. SAVE RESUME FILE
     // ==========================================

     Path resumePath =
             uploadPath.resolve(resumeFileName);

     try (InputStream inputStream =
             resumePart.getInputStream()) {

         Files.copy(
                 inputStream,
                 resumePath,
                 StandardCopyOption.REPLACE_EXISTING
         );
     }


     // ==========================================
     // 8H. SAVE FILE NAME IN APPLICATION
     // ==========================================

     application.setResumeFileName(
             resumeFileName
     );


        // 9. Save application

        boolean saved =
                applicationDao.saveApplication(
                        application
                );


        // 10. Response

        response.setContentType(
                "application/json"
        );


        if (saved) {

            response.getWriter().write(
                    "{\"message\":\"Application submitted successfully.\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write(
                    "{\"message\":\"Application submission failed.\"}"
            );
        }
    }
}