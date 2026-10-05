document.addEventListener("DOMContentLoaded", function () {

    console.log("Application form page loaded.");

    loadApplicationForm();

});


// ==========================================
// LOAD APPLICATION FORM
// ==========================================

function loadApplicationForm() {

    const params =
        new URLSearchParams(window.location.search);

    const jobId =
        params.get("id");

    console.log("Job ID:", jobId);


    // ==========================================
    // CHECK JOB ID
    // ==========================================

    if (!jobId) {

        document.getElementById("message").innerHTML =
            `<div class="alert alert-danger">
                Job ID is missing.
            </div>`;

        return;
    }


    // ==========================================
    // STORE JOB ID
    // ==========================================

    document.getElementById("jobId").value = jobId;


    // ==========================================
    // LOAD JOB DETAILS
    // ==========================================

    fetch("student/jobs?id=" + jobId)

        .then(response => {

            console.log(
                "Job response status:",
                response.status
            );

            if (!response.ok) {

                throw new Error(
                    "Unable to load job details."
                );
            }

            return response.json();

        })

        .then(job => {

            console.log("Job:", job);


            // Fill Job Title

            document.getElementById("jobTitle").value =
                job.title || "";


            // Fill Company

            document.getElementById("company").value =
                job.company || "";


            // Fill Location

            document.getElementById("location").value =
                job.location || "";

        })

        .catch(error => {

            console.error(
                "Job loading error:",
                error
            );

            document.getElementById("message").innerHTML =
                `<div class="alert alert-danger">
                    Unable to load job details.
                </div>`;

        });


    // ==========================================
    // SUBMIT FORM
    // ==========================================

    document
        .getElementById("applicationForm")
        .addEventListener("submit", function (event) {

            event.preventDefault();

            submitApplication();

        });

}


// ==========================================
// SUBMIT APPLICATION
// ==========================================

function submitApplication() {

    console.log(
        "Submit Application clicked."
    );


    // ==========================================
    // GET JOB ID
    // ==========================================

    const params =
        new URLSearchParams(window.location.search);

    const jobId =
        params.get("id");


    // ==========================================
    // GET COVER LETTER
    // ==========================================

    const coverLetter =
        document.getElementById(
            "coverLetter"
        ).value.trim();


    // ==========================================
    // GET RESUME
    // ==========================================

    const resume =
        document.getElementById(
            "resume"
        ).files[0];


    // ==========================================
    // GET MESSAGE ELEMENT
    // ==========================================

    const message =
        document.getElementById(
            "message"
        );


    // ==========================================
    // GET SUBMIT BUTTON
    // ==========================================

    const submitButton =
        document.getElementById(
            "submitApplication"
        );


    // ==========================================
    // VALIDATE JOB ID
    // ==========================================

    if (!jobId) {

        message.innerHTML =
            `<div class="alert alert-danger">
                Job ID is missing.
            </div>`;

        return;
    }


    // ==========================================
    // VALIDATE RESUME
    // ==========================================

    if (!resume) {

        message.innerHTML =
            `<div class="alert alert-warning">
                Please upload your resume.
            </div>`;

        return;
    }


    // ==========================================
    // CHECK RESUME TYPE
    // ==========================================

    const allowedTypes = [

        "application/pdf",

        "application/msword",

        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

    ];


    if (!allowedTypes.includes(resume.type)) {

        message.innerHTML =
            `<div class="alert alert-danger">
                Please upload PDF, DOC or DOCX resume.
            </div>`;

        return;
    }


    // ==========================================
    // CHECK RESUME SIZE
    // ==========================================

    if (resume.size > 5 * 1024 * 1024) {

        message.innerHTML =
            `<div class="alert alert-danger">
                Resume size must be less than 5 MB.
            </div>`;

        return;
    }


    // ==========================================
    // CREATE FORM DATA
    // ==========================================

    const formData =
        new FormData();


    formData.append(
        "jobId",
        jobId
    );


    formData.append(
        "coverLetter",
        coverLetter
    );


    formData.append(
        "resume",
        resume
    );


    console.log(
        "Submitting application..."
    );


    // ==========================================
    // DISABLE BUTTON
    // ==========================================

    submitButton.disabled = true;

    submitButton.innerText =
        "Submitting...";


    // ==========================================
    // SEND TO SERVLET
    // ==========================================

    fetch(
        "student/apply",
        {
            method: "POST",
            body: formData
        }
    )

        .then(response => {

            console.log(
                "Application response status:",
                response.status
            );


            return response.json()

                .then(data => {

                    return {
                        status: response.status,
                        data: data
                    };

                });

        })

        .then(result => {

            console.log(
                "Application response:",
                result
            );


            // ==========================================
            // SUCCESS
            // ==========================================

            if (result.status === 200) {

                message.innerHTML =
                    `<div class="alert alert-success">
                        ✅ ${result.data.message}
                    </div>`;


                console.log(
                    "Application submitted successfully."
                );


                // Reset form

                document.getElementById(
                    "applicationForm"
                ).reset();


                // Restore Job ID

                document.getElementById(
                    "jobId"
                ).value = jobId;


                // Redirect after 1.5 seconds

                setTimeout(function () {

                    window.location.href =
                        "my-applications.html";

                }, 1500);

            }


            // ==========================================
            // ERROR
            // ==========================================

            else {

                message.innerHTML =
                    `<div class="alert alert-danger">
                        ❌ ${result.data.message}
                    </div>`;

            }

        })

        .catch(error => {

            console.error(
                "Application submission error:",
                error
            );


            message.innerHTML =
                `<div class="alert alert-danger">
                    ❌ Unable to submit application.
                </div>`;

        })

        .finally(() => {

            submitButton.disabled = false;

            submitButton.innerText =
                "📝 Submit Application";

        });

}