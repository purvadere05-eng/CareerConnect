package com.careerconnect.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.careerconnect.dao.ApplicationDao;
import com.careerconnect.entity.Application;
import com.careerconnect.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/applications")
public class AdminApplicationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ApplicationDao applicationDao =
            new ApplicationDao();

    private ObjectMapper objectMapper =
            new ObjectMapper();


    private boolean isAdmin(HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        User user =
                (User) session.getAttribute("user");

        return user != null &&
               "ADMIN".equals(user.getRole());
    }


    // ==========================================
    // GET ALL APPLICATIONS
    // ==========================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(
                "========== ADMIN APPLICATIONS GET =========="
        );


        // ADMIN AUTHORIZATION

        if (!isAdmin(request)) {

            System.out.println(
                    "ADMIN APPLICATIONS: ACCESS DENIED"
            );

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );

            return;
        }


        HttpSession session =
                request.getSession(false);

        User user =
                (User) session.getAttribute("user");

        System.out.println(
                "ADMIN APPLICATIONS USER = "
                + user.getUsername()
        );

        System.out.println(
                "ADMIN APPLICATIONS ROLE = "
                + user.getRole()
        );


        // GET APPLICATIONS

        List<Application> applications =
                applicationDao.getAllApplications();


        System.out.println(
                "TOTAL APPLICATIONS = "
                + applications.size()
        );


        // ==========================================
        // CREATE SIMPLE JSON DATA
        // ==========================================

        List<Map<String, Object>> result =
                new ArrayList<>();


        for (Application application : applications) {

            Map<String, Object> data =
                    new HashMap<>();


            // Application ID

            data.put(
                    "id",
                    application.getId()
            );


            // Student information

            if (application.getStudent() != null) {

                data.put(
                        "studentName",
                        application.getStudent().getFullName()
                );

                data.put(
                        "studentEmail",
                        application.getStudent().getEmail()
                );

            } else {

                data.put(
                        "studentName",
                        "Unknown"
                );

                data.put(
                        "studentEmail",
                        "N/A"
                );
            }


            // Job information

            if (application.getJob() != null) {

                data.put(
                        "jobTitle",
                        application.getJob().getTitle()
                );

                data.put(
                        "company",
                        application.getJob().getCompany()
                );

                data.put(
                        "location",
                        application.getJob().getLocation()
                );

                data.put(
                        "salary",
                        application.getJob().getSalary()
                );

                data.put(
                        "skills",
                        application.getJob().getSkills()
                );

                data.put(
                        "jobType",
                        application.getJob().getJobType()
                );

            } else {

                data.put(
                        "jobTitle",
                        "Unknown"
                );

                data.put(
                        "company",
                        "N/A"
                );

                data.put(
                        "location",
                        "N/A"
                );

                data.put(
                        "salary",
                        "N/A"
                );

                data.put(
                        "skills",
                        "N/A"
                );

                data.put(
                        "jobType",
                        "N/A"
                );
            }


            // Application status

            data.put(
                    "status",
                    application.getStatus()
            );
            
         // Resume information

            String resumeFileName =
                    application.getResumeFileName();

            if (resumeFileName != null &&
                    !resumeFileName.trim().isEmpty()) {

                data.put(
                        "resumeFileName",
                        resumeFileName
                );

            } else {

                data.put(
                        "resumeFileName",
                        null
                );
            }


            // Applied date

            if (application.getAppliedAt() != null) {

                data.put(
                        "appliedAt",
                        application.getAppliedAt()
                                .toString()
                );

            } else {

                data.put(
                        "appliedAt",
                        "N/A"
                );
            }


            result.add(data);
        }


        // ==========================================
        // SEND JSON
        // ==========================================

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        objectMapper.writeValue(
                response.getWriter(),
                result
        );
    }


    // ==========================================
    // UPDATE APPLICATION STATUS
    // ==========================================

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        if (!isAdmin(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );

            return;
        }


        String idParameter =
                request.getParameter("id");

        String status =
                request.getParameter("status");


        if (idParameter == null ||
            status == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Application ID and status are required."
            );

            return;
        }


        int applicationId;

        try {

            applicationId =
                    Integer.parseInt(idParameter);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid application ID."
            );

            return;
        }


        status =
                status.trim().toUpperCase();


        if (!status.equals("PENDING") &&
            !status.equals("SHORTLISTED") &&
            !status.equals("REJECTED") &&
            !status.equals("SELECTED")) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid application status."
            );

            return;
        }


        boolean updated =
                applicationDao.updateApplicationStatus(
                        applicationId,
                        status
                );


        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        if (updated) {

            response.getWriter().write(
                    "{\"message\":\"Application status updated successfully.\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            response.getWriter().write(
                    "{\"message\":\"Application not found.\"}"
            );
        }
    }
}