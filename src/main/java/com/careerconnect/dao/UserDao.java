package com.careerconnect.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careerconnect.entity.User;
import com.careerconnect.util.HibernateUtil;

public class UserDao {

    public void saveUser(User user) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

    // Login method
    public User login(String username, String password) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM User WHERE username = :username AND password = :password";

            User user = session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}