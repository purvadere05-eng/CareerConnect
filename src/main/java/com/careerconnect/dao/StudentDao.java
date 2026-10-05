package com.careerconnect.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careerconnect.entity.Student;
import com.careerconnect.util.HibernateUtil;

public class StudentDao {

    public Student getStudentByUserId(int userId) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Student WHERE user.id = :userId";

            return session.createQuery(hql, Student.class)
                    .setParameter("userId", userId)
                    .uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }


    public boolean updateStudent(Student student) {

        Transaction transaction = null;

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(student);

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
    
    public Student getStudentById(int id) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Student.class, id);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}