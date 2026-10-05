document.addEventListener("DOMContentLoaded", function () {

    loadJobs();

    document
        .getElementById("searchButton")
        .addEventListener("click", searchJobs);

    document
        .getElementById("clearButton")
        .addEventListener("click", loadJobs);

});


// ======================================
// LOAD JOBS
// ======================================

function loadJobs() {

    console.log("Loading student jobs...");

    fetch("student/jobs")

        .then(response => {

            console.log(
                "Student jobs status:",
                response.status
            );

            if (!response.ok) {
                throw new Error(
                    "Unable to load jobs"
                );
            }

            return response.json();
        })

        .then(jobs => {

            console.log(
                "Student jobs:",
                jobs
            );

            displayJobs(jobs);
        })

        .catch(error => {

            console.error(
                "Load jobs error:",
                error
            );

            document
                .getElementById("jobsContainer")
                .textContent =
                "Unable to load jobs.";
        });
}


// ======================================
// DISPLAY JOBS
// ======================================

function displayJobs(jobs) {

    const container =
        document.getElementById("jobsContainer");

    container.innerHTML = "";

    if (jobs.length === 0) {

        container.innerHTML =
            "<p>No jobs available.</p>";

        return;
    }

    jobs.forEach(job => {

        const jobCard =
            document.createElement("div");

        jobCard.className = "job-card";

        jobCard.innerHTML = `

            <h3>${job.title}</h3>

            <p>
                <strong>Company:</strong>
                ${job.company}
            </p>

            <p>
                <strong>Location:</strong>
                ${job.location}
            </p>

            <p>
                <strong>Salary:</strong>
                ${job.salary}
            </p>

            <p>
                <strong>Skills:</strong>
                ${job.skills}
            </p>

            <p>
                <strong>Job Type:</strong>
                ${job.jobType || "Not specified"}
            </p>

            <p>
                <strong>Description:</strong>
                ${job.description}
            </p>

            <button onclick="viewJob(${job.id})">
                View Details
            </button>

            <hr>
        `;

        container.appendChild(jobCard);
    });
}


// ======================================
// SEARCH JOBS
// ======================================

function searchJobs() {

    const keyword =
        document
            .getElementById("keyword")
            .value
            .toLowerCase()
            .trim();

    const location =
        document
            .getElementById("location")
            .value
            .toLowerCase()
            .trim();

    fetch("student/jobs")

        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Unable to load jobs"
                );
            }

            return response.json();
        })

        .then(jobs => {

            const filteredJobs =
                jobs.filter(job => {

                    const title =
                        (job.title || "")
                            .toLowerCase();

                    const skills =
                        (job.skills || "")
                            .toLowerCase();

                    const jobLocation =
                        (job.location || "")
                            .toLowerCase();

                    const keywordMatch =
                        keyword === "" ||
                        title.includes(keyword) ||
                        skills.includes(keyword);

                    const locationMatch =
                        location === "" ||
                        jobLocation.includes(location);

                    return (
                        keywordMatch &&
                        locationMatch
                    );
                });

            displayJobs(filteredJobs);
        })

        .catch(error => {

            console.error(
                "Search error:",
                error
            );
        });
}


// ======================================
// VIEW JOB DETAILS
// ======================================

function viewJob(id) {

    console.log(
        "Opening job:",
        id
    );

    window.location.href =
        "job-details.html?id=" + id;
}


// ======================================
// APPLY FOR JOB
// ======================================

function applyForJob(jobId) {

    const confirmed =
        confirm(
            "Do you want to apply for this job?"
        );

    if (!confirmed) {
        return;
    }

    console.log(
        "Applying for job:",
        jobId
    );

    fetch(
        "student/apply?jobId=" + jobId,
        {
            method: "POST"
        }
    )

    .then(async response => {

        const data =
            await response.json();

        console.log(
            "Application status:",
            response.status
        );

        console.log(
            "Application response:",
            data
        );

        if (!response.ok) {

            throw new Error(
                data.message ||
                "Application failed."
            );
        }

        return data;
    })

    .then(data => {

        alert(data.message);

    })

    .catch(error => {

        console.error(
            "Application error:",
            error
        );

        alert(error.message);
    });
}