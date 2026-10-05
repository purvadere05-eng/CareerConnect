document.addEventListener("DOMContentLoaded", function () {

    console.log("Job Details page loaded.");

    // Get Job ID from URL
    const params = new URLSearchParams(window.location.search);
    const jobId = params.get("id");

    console.log("Job ID:", jobId);

    if (!jobId) {

        document.getElementById("jobDetails").innerHTML =
            "<p>Job ID is missing.</p>";

        return;
    }

    // ==========================================
    // APPLY BUTTON
    // ==========================================

    const applyButton =
        document.getElementById("applyButton");

    applyButton.addEventListener("click", function () {

        console.log("Apply Now button clicked.");
        console.log("Redirecting to application form...");

        window.location.href =
            "application-form.html?id=" + jobId;
    });


    // ==========================================
    // LOAD JOB DETAILS
    // ==========================================

    fetch("student/jobs?id=" + jobId)

        .then(response => {

            console.log(
                "Job details response:",
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

            console.log("Job details:", job);

            displayJob(job);

        })

        .catch(error => {

            console.error(
                "Job details error:",
                error
            );

            document.getElementById(
                "jobDetails"
            ).innerHTML =
                "<p>Unable to load job details.</p>";
        });

});


// ==========================================
// DISPLAY JOB
// ==========================================

function displayJob(job) {

    const container =
        document.getElementById("jobDetails");

    container.innerHTML = `

        <h2>${job.title || "No title"}</h2>

        <p>
            <strong>Company:</strong>
            ${job.company || "Not specified"}
        </p>

        <p>
            <strong>Location:</strong>
            ${job.location || "Not specified"}
        </p>

        <p>
            <strong>Salary:</strong>
            ${job.salary || "Not specified"}
        </p>

        <p>
            <strong>Skills:</strong>
            ${job.skills || "Not specified"}
        </p>

        <p>
            <strong>Job Type:</strong>
            ${job.jobType || "Not specified"}
        </p>

        <p>
            <strong>Description:</strong>
            ${job.description || "No description available"}
        </p>

    `;
}