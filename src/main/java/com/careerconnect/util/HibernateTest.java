package com.careerconnect.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateTest {

    public static void main(String[] args) {

        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.openSession();

        System.out.println("Hibernate connection successful!");

        session.close();
        factory.close();
    }
}