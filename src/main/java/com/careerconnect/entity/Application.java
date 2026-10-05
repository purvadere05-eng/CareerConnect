package com.careerconnect.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

@Entity
@Table(
    name = "applications",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"student_id", "job_id"}
        )
    }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;


    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;


    @Column(nullable = false)
    private String status;


    @Column(name = "applied_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appliedAt;
    
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String resumeFileName;


    public Application() {
    }


    public Application(Student student, Job job) {

        this.student = student;
        this.job = job;
        this.status = "PENDING";
        this.appliedAt = LocalDateTime.now();
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public Student getStudent() {
        return student;
    }


    public void setStudent(Student student) {
        this.student = student;
    }


    public Job getJob() {
        return job;
    }


    public void setJob(Job job) {
        this.job = job;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }


    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }
}