package com.careerconnect.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careerconnect.entity.Student;
import com.careerconnect.entity.User;
import com.careerconnect.util.HibernateUtil;

public class RegistrationDao {

    public boolean registerStudent(User user, Student student) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            student.setUser(user);

            session.persist(student);

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
}