package com.careerconnect.servlet;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/student/jobs")
public class StudentJobServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private JobDao jobDao = new JobDao();

    private boolean isStudent(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            System.out.println("STUDENT JOB SESSION IS NULL");
            return false;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            System.out.println("STUDENT JOB USER IS NULL");
            return false;
        }

        System.out.println(
            "STUDENT JOB USER = " + user.getUsername()
        );

        System.out.println(
            "STUDENT JOB ROLE = " + user.getRole()
        );

        return "STUDENT".equals(user.getRole());
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isStudent(request)) {

            response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Student access required."
            );

            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        // Check if job ID is provided
        String idParameter = request.getParameter("id");

        // ==========================================
        // GET ONE JOB
        // ==========================================

        if (idParameter != null) {

            int id;

            try {

                id = Integer.parseInt(idParameter);

            } catch (NumberFormatException e) {

                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid Job ID."
                );

                return;
            }

            Job job = jobDao.getJobById(id);

            if (job == null) {

                response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
                );

                mapper.writeValue(
                    response.getWriter(),
                    java.util.Map.of(
                        "message",
                        "Job not found"
                    )
                );

                return;
            }

            System.out.println(
                "STUDENT JOB DETAILS ID = " + id
            );

            mapper.writeValue(
                response.getWriter(),
                job
            );

            return;
        }

        // ==========================================
        // GET ALL JOBS
        // ==========================================

        List<Job> jobs = jobDao.getAllJobs();

        mapper.writeValue(
            response.getWriter(),
            jobs
        );
    }
}