package com.careerconnect.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careerconnect.entity.Job;
import com.careerconnect.util.HibernateUtil;

public class JobDao {

    // CREATE
    public boolean saveJob(Job job) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(job);

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


    // READ
    public List<Job> getAllJobs() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Job ORDER BY id DESC",
                    Job.class
            ).getResultList();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }


    // READ ONE
    public Job getJobById(int id) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Job.class, id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }


    // UPDATE
    public boolean updateJob(Job job) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(job);

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


    // DELETE
    public boolean deleteJob(int id) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Job job = session.get(Job.class, id);

            if (job != null) {

                session.remove(job);

                transaction.commit();

                return true;
            }

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }

        return false;
    }
}