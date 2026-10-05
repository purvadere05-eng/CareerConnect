# 🚀 CareerConnect – Job & Placement Management Portal

 **A centralized web-based platform for managing job opportunities, student profiles, and placement applications.**

CareerConnect is a **Job and Placement Management Portal** developed using Java and web technologies. The system provides separate modules for **Students** and **Administrators** with role-based access.

Students can register, manage their profiles, browse available jobs, apply for suitable opportunities, upload resumes, and track their application status. Administrators can manage job postings, review student applications, view resumes, and update application statuses.

## 📌 Project Overview

CareerConnect simplifies the job application and placement management process by providing a centralized platform for students and administrators.

### 👨‍🎓 Student Flow

Register
   ↓
Login
   ↓
Student Dashboard
   ↓
Manage Profile
   ↓
Browse Jobs
   ↓
View Job Details
   ↓
Apply for Job
   ↓
Upload Resume + Cover Letter
   ↓
Track Application Status

###👨‍💼 Admin Flow
Admin Login
   ↓
Admin Dashboard
   ↓
Manage Jobs
   ↓
Add / Edit Jobs
   ↓
Manage Applications
   ↓
Review Student Applications
   ↓
View Resume
   ↓
Update Application Status

✨ Key Features

👨‍🎓 Student Module
🔐 Student Registration
🔑 Student Login
🏠 Student Dashboard
👤 View Student Profile
✏️ Edit Profile
🖼️ Profile Photo Management
🔍 Browse and Search Jobs
📄 View Job Details
📝 Apply for Jobs
📎 Resume Upload
✉️ Cover Letter Submission
📋 View My Applications
📊 Track Application Status
🚪 Logout

👨‍💼 Admin Module
🔐 Admin Login
🏠 Admin Dashboard
➕ Add New Jobs
✏️ Edit Job Details
💼 Manage Job Listings
📋 View Student Applications
👁️ View Application Details
📄 View Uploaded Resumes
🔄 Update Application Status
📊 Application Status

Administrators can update applications using:

🟡 PENDING
🔵 SHORTLISTED
🟢 SELECTED
🔴 REJECTED

🛠️ Technologies Used
Technology	      Purpose
☕ Java	          Backend development
🧩 OOP	          Object-oriented application design
🌐 Servlets     	Request handling and backend processing
🔗 Hibernate	    ORM and database operations
🔌 JDBC     	    Database connectivity
🗄️ MySQL        	Data storage and management
🏗️ HTML5	        Web page structure
🎨 CSS3         	User interface styling
⚡JavaScript	    Client-side functionality
📱 Bootstrap 5.3.3	Responsive user interface
📦 Maven         	Dependency and project management
🐱Apache Tomcat 10.1.57	Application server
💻 Eclipse IDE	Development environment
☕ JDK 25	    Java development

🏗️ Application Architecture

CareerConnect follows a layered architecture where each layer has a specific responsibility.

                 👤 USER
                   │
                   ▼
       ┌─────────────────────────┐
       │      🎨 FRONTEND        │
       │ HTML + CSS + JS +       │
       │ Bootstrap               │
       └────────────┬────────────┘
                    │
                    ▼
       ┌─────────────────────────┐
       │    🌐 SERVLET LAYER     │
       │ Request Handling &      │
       │ Business Logic          │
       └────────────┬────────────┘
                    │
                    ▼
       ┌─────────────────────────┐
       │       📦 DAO LAYER      │
       │ Database Operations     │
       └────────────┬────────────┘
                    │
                    ▼
       ┌─────────────────────────┐
       │     🔗 HIBERNATE ORM    │
       │ Entity Mapping & CRUD   │
       └────────────┬────────────┘
                    │
                    ▼
       ┌─────────────────────────┐
       │       🗄️ MYSQL DB       │
       │ Persistent Data Storage │
       └─────────────────────────┘

🔹 Main Layers

🎨 Presentation Layer

HTML pages
CSS
JavaScript
Bootstrap

Responsible for the user interface and user interaction.

🌐 Servlet Layer

Handles HTTP requests
Processes user actions
Connects frontend with backend logic
Implements authentication and application workflows

📦 DAO Layer

Handles database-related operations
Performs CRUD operations
Communicates with Hibernate

🔗 Hibernate Layer

Maps Java entities to database tables
Manages object-relational mapping
Simplifies database operations

🗄️ Database Layer

MySQL database
Stores users, students, jobs, and applications
🧩 Main Components
📦 Entity Layer

The project contains the following Hibernate entities:

👤 User
🎓 Student
💼 Job
📝 Application
🗃️ DAO Layer

Database operations are handled through:

UserDao
StudentDao
JobDao
ApplicationDao
RegistrationDao
🌐 Servlet Layer

Important Servlets include:

LoginServlet
RegisterServlet
LogoutServlet
ProfileServlet
ProfileImageServlet
JobServlet
StudentJobServlet
ApplyJobServlet
SubmitApplicationServlet
MyApplicationsServlet
AdminApplicationServlet
ViewResumeServlet
⚙️ Utility Layer
HibernateUtil
HibernateTest

🗄️ Database Design

Database Name: careerconnect_db

📌 Main Entities
👤 User
🎓 Student
💼 Job
📝 Application
🔗 Entity Relationships
        👤 User
           │
           │ 1 : 1
           ▼
       🎓 Student
           │
           │ 1 : Many
           ▼
      📝 Application
           │
           │ Many : 1
           ▼
        💼 Job
Relationship Details
👤 User → Student : One-to-One
🎓 Student → Application : One-to-Many
💼 Job → Application : One-to-Many
📝 Application → Student : Many-to-One
📝 Application → Job : Many-to-One

Hibernate manages these relationships using JPA/Hibernate annotations.

🔐 Role-Based Access

CareerConnect provides different access levels based on the user's role.

👨‍🎓 Student Access

Students can:

Register and Login
Manage their profile
Browse jobs
View job details
Apply for jobs
Upload resumes
Submit cover letters
Track applications

👨‍💼 Admin Access

Administrators can:

Login to Admin Dashboard
Add jobs
Edit jobs
Manage job listings
View student applications
View resumes
Update application status

🔒 Students do not have access to administrative job and application management functionality.

📁 Project Structure
careerconnect/
│
├── 📄 pom.xml
├── 📄 .gitignore
├── 📄 README.md
│
└── 📂 src/
    │
    └── 📂 main/
        │
        ├── 📂 java/
        │   │
        │   └── 📂 com/careerconnect/
        │       │
        │       ├── 📂 dao/
        │       │   ├── ApplicationDao.java
        │       │   ├── JobDao.java
        │       │   ├── RegistrationDao.java
        │       │   ├── StudentDao.java
        │       │   └── UserDao.java
        │       │
        │       ├── 📂 entity/
        │       │   ├── Application.java
        │       │   ├── Job.java
        │       │   ├── Student.java
        │       │   └── User.java
        │       │
        │       ├── 📂 servlet/
        │       │   ├── LoginServlet.java
        │       │   ├── RegisterServlet.java
        │       │   ├── LogoutServlet.java
        │       │   ├── ProfileServlet.java
        │       │   ├── JobServlet.java
        │       │   ├── StudentJobServlet.java
        │       │   ├── ApplyJobServlet.java
        │       │   ├── SubmitApplicationServlet.java
        │       │   ├── MyApplicationsServlet.java
        │       │   ├── AdminApplicationServlet.java
        │       │   └── ViewResumeServlet.java
        │       │
        │       └── 📂 util/
        │           ├── HibernateUtil.java
        │           └── HibernateTest.java
        │
        ├── 📂 resources/
        │   └── hibernate.cfg.xml
        │
        └── 📂 webapp/
            │
            ├── 📂 css/
            │   └── style.css
            │
            ├── 📂 js/
            │   ├── admin-applications.js
            │   ├── admin-jobs.js
            │   ├── application-form.js
            │   ├── edit-job.js
            │   ├── edit-profile.js
            │   ├── job-details.js
            │   ├── my-applications.js
            │   ├── profile.js
            │   └── student-jobs.js
            │
            ├── 📂 WEB-INF/
            │   └── web.xml
            │
            └── 📄 HTML Pages

⚙️ Setup & Installation
📋 Prerequisites

Install the following software:

☕ JDK 25
💻 Eclipse IDE
🗄️ MySQL
🐱 Apache Tomcat 10.1
📦 Maven
🌐 Web Browser
1️⃣ Clone the Repository
git clone https://github.com/purvadere05-eng/CareerConnect.git

Navigate to the project:

cd CareerConnect
2️⃣ Create MySQL Database

Open MySQL and execute:

CREATE DATABASE careerconnect_db;
3️⃣ Configure Database Connection

Open:

src/main/resources/hibernate.cfg.xml

Configure your local MySQL credentials:

<property name="hibernate.connection.url">
    jdbc:mysql://localhost:3306/careerconnect_db
</property>

<property name="hibernate.connection.username">
    root
</property>

<property name="hibernate.connection.password">
    YOUR_MYSQL_PASSWORD
</property>

Replace YOUR_MYSQL_PASSWORD with your local MySQL password.

🔒 Security: Never upload real database passwords, API keys, or other sensitive credentials to GitHub.

4️⃣ Import Project into Eclipse
Open Eclipse IDE
Select File → Import
Select Maven → Existing Maven Projects
Select the cloned CareerConnect folder
Click Finish
Wait for Maven dependencies to download
5️⃣ Configure Apache Tomcat
Add Apache Tomcat 10.1 to Eclipse.
Right-click the CareerConnect project.
Select Run As → Run on Server.
Select Tomcat 10.1.
Start the application.
6️⃣ Open the Application
http://localhost:8080/careerconnect/
🎯 Problem Statement

Students often need to search for job opportunities across multiple platforms, making it difficult to manage and track their applications.

Placement administrators also need an efficient system to manage job postings and student applications.

💡 Solution

CareerConnect provides a centralized platform where:

🎓 Students can discover suitable job opportunities.
📝 Students can apply for jobs online.
📄 Students can upload resumes and cover letters.
📊 Students can track application status.
👨‍💼 Administrators can manage job postings.
📋 Administrators can review student applications.
🔄 Administrators can update application status.
🔄 Complete Application Workflow
                    🚀 CareerConnect
                          │
             ┌────────────┴────────────┐
             │                         │
             ▼                         ▼
        👨‍🎓 STUDENT               👨‍💼 ADMIN
             │                         │
             ▼                         ▼
        🔐 Register/Login          🔐 Login
             │                         │
             ▼                         ▼
       🏠 Dashboard              🏠 Dashboard
             │                         │
       ┌─────┼─────┐             ┌─────┴─────┐
       │     │     │             │           │
       ▼     ▼     ▼             ▼           ▼
    👤Profile 💼Jobs 📋Apps    💼Jobs     📋Applications
              │                  │             │
              ▼                  ▼             ▼
         📄 Job Details      ✏️ Add/Edit    👁️ Review
              │                              │
              ▼                              ▼
          📝 Apply                       🔄 Update
              │                           Status
              ▼
        📎 Resume
        ✉️ Cover Letter
              │
              ▼
       📊 Track Status

💡 Benefits
🎯 Centralized job and placement management
🔍 Easy job discovery for students
📝 Simple online application process
📎 Resume upload functionality
✉️ Cover letter submission
📊 Application status tracking
👨‍💼 Efficient admin management
🔐 Role-based access
🗄️ Persistent database storage
📱 Responsive user interface
🧩 Modular and maintainable architecture

🧪 Testing

The following major functionalities were tested during development:

👨‍🎓 Student Testing
✅ Student Registration
✅ Student Login
✅ Student Dashboard
✅ Profile Loading
✅ Profile Update
✅ Job Search
✅ Job Details
✅ Job Application
✅ Resume Upload
✅ Cover Letter Submission
✅ My Applications
✅ Application Status Tracking
✅ Logout
👨‍💼 Admin Testing
✅ Admin Login
✅ Admin Dashboard
✅ Add Job
✅ Edit Job
✅ Manage Jobs
✅ View Applications
✅ View Resume
✅ Update Application Status
🗄️ Database Testing
✅ User data persistence
✅ Student profile persistence
✅ Job data persistence
✅ Application data persistence
✅ Hibernate entity relationships
✅ CRUD operations

🎓 Learning Outcomes

This project provided practical experience in:

☕ Core Java and OOP concepts
🌐 Java Servlet development
🔌 JDBC connectivity
🔗 Hibernate ORM
🗄️ MySQL database management
🔄 CRUD operations
🔍 HQL queries
🔗 Entity relationships
🔐 Role-based access
📎 File upload handling
🌐 Frontend-backend integration
📦 Maven project management
🏗️ Layered architecture
🧪 Application testing
🐞 Debugging and error handling

🔮 Future Scope

CareerConnect can be enhanced with:

📧 Email notifications for application updates
🔍 Advanced job filtering and sorting
🤖 AI-based job recommendations
📄 AI-powered resume parsing
🎯 Skill-based job matching
🏢 Company and Recruiter accounts
📅 Interview scheduling
🧠 Online aptitude and technical assessments
📊 Application analytics and reports
☁️ Cloud deployment
🔗 REST API integration
🔐 JWT / Spring Security authentication
📱 Mobile application
🔔 Real-time notifications

🚀 Future Enhancement

The application can be further migrated towards a modern enterprise architecture using:

Current
Java + Servlets + Hibernate + MySQL
                ↓
Future
Spring Boot + Spring Security + REST API
                ↓
React Frontend
                ↓
Cloud Deployment

This would improve scalability, security, maintainability, and deployment flexibility.

📌 Project Highlights
Category	Details
🎯 Project Type	Job & Placement Management Portal
👥 Users	Students and Administrators
💻 Backend	Java, Servlets, Hibernate, JDBC
🎨 Frontend	HTML, CSS, JavaScript, Bootstrap
🗄️ Database	MySQL
📦 Build Tool	Maven
🐱 Server	Apache Tomcat 10.1.57
☕ JDK	JDK 25
🏗️ Architecture	Layered Architecture
🔐 Access Control	Role-Based Access
📄 Resume	PDF / DOC / DOCX
📊 Application Status	Pending / Shortlisted / Selected / Rejected

👩‍💻 Developer
Purva Dere

Java Full Stack Developer

🔗 GitHub:
https://github.com/purvadere05-eng

📂 Project Repository:
https://github.com/purvadere05-eng/CareerConnect

📄 License

This project was developed for educational, learning, and portfolio purposes.

⭐ If you find this project useful, consider giving the repository a star!
