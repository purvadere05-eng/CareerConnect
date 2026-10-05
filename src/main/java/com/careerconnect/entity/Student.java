package com.careerconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   
    private String fullName;

    private String email;

    private String phone;

    private String qualification;

    private String skills;
   

    // Profile photo filename/path
    @Column(name = "profile_photo")
    private String profilePhoto;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    // =========================
    // DEFAULT CONSTRUCTOR
    // =========================

    public Student() {
    }


    // =========================
    // CONSTRUCTOR
    // =========================

    public Student(
            String fullName,
            String email,
            String phone,
            String qualification,
            String skills,
            User user) {

        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.qualification = qualification;
        this.skills = skills;
        this.user = user;
    }


    // =========================
    // GETTERS AND SETTERS
    // =========================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }


    // =========================
    // PROFILE PHOTO
    // =========================

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }


    // =========================
    // USER
    // =========================

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}