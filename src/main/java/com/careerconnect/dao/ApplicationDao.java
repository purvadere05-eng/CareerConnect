package com.careerconnect.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careerconnect.entity.Application;
import com.careerconnect.util.HibernateUtil;

public class ApplicationDao {


    // ==========================================
    // CHECK WHETHER STUDENT ALREADY APPLIED
    // ==========================================

    public boolean hasAlreadyApplied(
            int studentId,
            int jobId) {

        try (Session session =
                HibernateUtil.getSessionFactory()
                        .openSession()) {

            Long count =
                    session.createQuery(
                            "SELECT COUNT(a) " +
                            "FROM Application a " +
                            "WHERE a.student.id = :studentId " +
                            "AND a.job.id = :jobId",
                            Long.class
                    )
                    .setParameter(
                            "studentId",
                            studentId
                    )
                    .setParameter(
                            "jobId",
                            jobId
                    )
                    .uniqueResult();

            return count != null && count > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // ==========================================
    // SAVE APPLICATION
    // ==========================================

    public boolean saveApplication(
            Application application) {

        Transaction transaction = null;

        try (Session session =
                HibernateUtil.getSessionFactory()
                        .openSession()) {

            transaction =
                    session.beginTransaction();


            session.persist(application);


            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null) {

                transaction.rollback();
            }

            e.printStackTrace();

            return false;
        }
    }


    // ==========================================
    // GET APPLICATIONS BY STUDENT
    // ==========================================

    public List<Application>
    getApplicationsByStudentId(
            int studentId) {

        try (Session session =
                HibernateUtil.getSessionFactory()
                        .openSession()) {

            return session.createQuery(
                    "FROM Application a " +
                    "WHERE a.student.id = :studentId " +
                    "ORDER BY a.appliedAt DESC",
                    Application.class
            )
            .setParameter(
                    "studentId",
                    studentId
            )
            .getResultList();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }


    // ==========================================
    // GET ALL APPLICATIONS
    // ==========================================

    public List<Application>
    getAllApplications() {

        try (Session session =
                HibernateUtil.getSessionFactory()
                        .openSession()) {

            return session.createQuery(
                    "FROM Application a " +
                    "ORDER BY a.appliedAt DESC",
                    Application.class
            )
            .getResultList();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }


    // ==========================================
    // UPDATE APPLICATION STATUS
    // ==========================================

    public boolean updateApplicationStatus(
            int applicationId,
            String status) {

        Transaction transaction = null;

        try (Session session =
                HibernateUtil.getSessionFactory()
                        .openSession()) {

            transaction =
                    session.beginTransaction();


            Application application =
                    session.get(
                            Application.class,
                            applicationId
                    );


            if (application == null) {

                transaction.rollback();

                return false;
            }


            application.setStatus(status);


            session.merge(application);


            transaction.commit();

            return true;

        } catch (Exception e) {

            if (transaction != null) {

                transaction.rollback();
            }

            e.printStackTrace();

            return false;
        }
    }


    // ==========================================
    // CHECK APPLICATIONS FOR JOB
    // ==========================================

    public boolean hasApplicationsForJob(
            int jobId) {

        try (Session session =
                HibernateUtil.getSessionFactory()
                        .openSession()) {

            Long count =
                    session.createQuery(
                            "SELECT COUNT(a) " +
                            "FROM Application a " +
                            "WHERE a.job.id = :jobId",
                            Long.class
                    )
                    .setParameter(
                            "jobId",
                            jobId
                    )
                    .uniqueResult();


            return count != null && count > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}