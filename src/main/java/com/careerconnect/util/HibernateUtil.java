package com.careerconnect.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionfactory;
	
	static {
		try {
			Configuration cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			
			sessionfactory = cfg.buildSessionFactory();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SessionFactory getSessionFactory()
	{
		return sessionfactory;
		
	}
	
	public static void closeSessionFactory()
	{
		if(sessionfactory != null)
		{
			sessionfactory.close();
		}
	}

	

}
