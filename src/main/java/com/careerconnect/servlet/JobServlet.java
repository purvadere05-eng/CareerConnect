package com.careerconnect.servlet;

import java.io.IOException;
import java.util.List;

import com.careerconnect.dao.ApplicationDao;
import com.careerconnect.dao.JobDao;
import com.careerconnect.entity.Job;
import com.careerconnect.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/jobs")
public class JobServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private JobDao jobDao = new JobDao();

    private ApplicationDao applicationDao =
            new ApplicationDao();


    // =========================
    // ADMIN AUTHORIZATION
    // =========================

    private boolean isAdmin(HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            System.out.println(
                    "JOB SESSION IS NULL"
            );

            return false;
        }

        User user =
                (User) session.getAttribute("user");

        if (user == null) {

            System.out.println(
                    "JOB USER IS NULL"
            );

            return false;
        }

        System.out.println(
                "JOB USER = "
                + user.getUsername()
        );

        System.out.println(
                "JOB ROLE = ["
                + user.getRole()
                + "]"
        );

        return "ADMIN".equals(user.getRole());
    }


    // =========================
    // GET - ALL JOBS / ONE JOB
    // =========================

    @Override
    protected void doGet(
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


        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        String idParameter =
                request.getParameter("id");


        ObjectMapper mapper =
                new ObjectMapper();


        // =========================
        // GET ONE JOB
        // =========================

        if (idParameter != null) {

            int id;

            try {

                id =
                        Integer.parseInt(idParameter);

            } catch (NumberFormatException e) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid Job ID."
                );

                return;
            }


            Job job =
                    jobDao.getJobById(id);


            if (job == null) {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                response.getWriter().write(
                        "{\"message\":\"Job not found\"}"
                );

                return;
            }


            mapper.writeValue(
                    response.getWriter(),
                    job
            );


        } else {

            // =========================
            // GET ALL JOBS
            // =========================

            List<Job> jobs =
                    jobDao.getAllJobs();


            mapper.writeValue(
                    response.getWriter(),
                    jobs
            );
        }
    }


    // =========================
    // POST - ADD JOB
    // =========================

    @Override
    protected void doPost(
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


        ObjectMapper mapper =
                new ObjectMapper();


        Job job =
                mapper.readValue(
                        request.getReader(),
                        Job.class
                );


        boolean saved =
                jobDao.saveJob(job);


        response.setContentType(
                "application/json"
        );


        if (saved) {

            response.getWriter().write(
                    "{\"message\":\"Job added successfully\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write(
                    "{\"message\":\"Job could not be added\"}"
            );
        }
    }


 // =========================
 // PUT - UPDATE / CLOSE JOB
 // =========================

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

     ObjectMapper mapper =
             new ObjectMapper();

     Job updatedJob =
             mapper.readValue(
                     request.getReader(),
                     Job.class
             );

     // ==========================================
     // CLOSE JOB
     // ==========================================

     String action =
             request.getParameter("action");

     if ("CLOSE".equalsIgnoreCase(action)) {

         Job existingJob =
                 jobDao.getJobById(
                         updatedJob.getId()
                 );

         if (existingJob == null) {

             response.setStatus(
                     HttpServletResponse.SC_NOT_FOUND
             );

             response.setContentType(
                     "application/json"
             );

             response.getWriter().write(
                     "{\"message\":\"Job not found\"}"
             );

             return;
         }

         // Change only the status
         existingJob.setStatus("CLOSED");

         boolean closed =
                 jobDao.updateJob(existingJob);

         response.setContentType(
                 "application/json"
         );

         response.setCharacterEncoding(
                 "UTF-8"
         );

         if (closed) {

             response.getWriter().write(
                     "{\"message\":\"Job closed successfully\"}"
             );

         } else {

             response.setStatus(
                     HttpServletResponse.SC_INTERNAL_SERVER_ERROR
             );

             response.getWriter().write(
                     "{\"message\":\"Unable to close job\"}"
             );
         }

         return;
     }


     // ==========================================
     // EXISTING UPDATE JOB LOGIC
     // ==========================================

     Job existingJob =
             jobDao.getJobById(
                     updatedJob.getId()
             );

     if (existingJob == null) {

         response.setStatus(
                 HttpServletResponse.SC_NOT_FOUND
         );

         response.getWriter().write(
                 "{\"message\":\"Job not found\"}"
         );

         return;
     }


     existingJob.setTitle(
             updatedJob.getTitle()
     );

     existingJob.setCompany(
             updatedJob.getCompany()
     );

     existingJob.setLocation(
             updatedJob.getLocation()
     );

     existingJob.setSalary(
             updatedJob.getSalary()
     );

     existingJob.setSkills(
             updatedJob.getSkills()
     );

     existingJob.setJobType(
             updatedJob.getJobType()
     );

     existingJob.setDescription(
             updatedJob.getDescription()
     );


     boolean updated =
             jobDao.updateJob(existingJob);


     response.setContentType(
             "application/json"
     );

     response.setCharacterEncoding(
             "UTF-8"
     );


     if (updated) {

         response.getWriter().write(
                 "{\"message\":\"Job updated successfully\"}"
         );

     } else {

         response.setStatus(
                 HttpServletResponse.SC_INTERNAL_SERVER_ERROR
         );

         response.getWriter().write(
                 "{\"message\":\"Job update failed\"}"
         );
     }
 }

    // =========================
    // DELETE - DELETE JOB
    // =========================

    @Override
    protected void doDelete(
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


        // =========================
        // CHECK JOB ID
        // =========================

        if (idParameter == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Job ID is required."
            );

            return;
        }


        int id;

        try {

            id =
                    Integer.parseInt(idParameter);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid Job ID."
            );

            return;
        }


        // =========================
        // CHECK APPLICATIONS
        // =========================

        boolean hasApplications =
                applicationDao.hasApplicationsForJob(id);


        if (hasApplications) {

            response.setStatus(
                    HttpServletResponse.SC_CONFLICT
            );

            response.setContentType(
                    "application/json"
            );

            response.setCharacterEncoding(
                    "UTF-8"
            );

            response.getWriter().write(
                    "{\"message\":\"Cannot delete this job because students have already applied.\"}"
            );

            return;
        }


        // =========================
        // DELETE JOB
        // =========================

        boolean deleted =
                jobDao.deleteJob(id);


        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        if (deleted) {

            response.getWriter().write(
                    "{\"message\":\"Job deleted successfully\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            response.getWriter().write(
                    "{\"message\":\"Job not found\"}"
            );
        }
    }
}