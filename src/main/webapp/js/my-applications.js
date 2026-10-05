document.addEventListener("DOMContentLoaded", function () {

    loadApplications();

});


// ======================================
// LOAD MY APPLICATIONS
// ======================================

function loadApplications() {

    console.log("Loading my applications...");

    fetch("student/applications")

        .then(response => {

            console.log(
                "Applications status:",
                response.status
            );

            if (!response.ok) {

                throw new Error(
                    "Unable to load applications. Status: "
                    + response.status
                );

            }

            return response.json();

        })

        .then(applications => {

            console.log(
                "My applications:",
                applications
            );

            displayApplications(applications);

        })

        .catch(error => {

            console.error(
                "Applications error:",
                error
            );

            document
                .getElementById("applicationsContainer")
                .textContent =
                "Unable to load applications.";

        });

}


// ======================================
// DISPLAY APPLICATIONS
// ======================================

function displayApplications(applications) {

    const container =
        document.getElementById(
            "applicationsContainer"
        );

    container.innerHTML = "";


    if (!applications ||
        applications.length === 0) {

        container.innerHTML =
            "<p>No applications found.</p>";

        return;
    }


    applications.forEach(application => {

        const job = application.job;


        const applicationCard =
            document.createElement("div");


        applicationCard.className =
            "application-card";


        applicationCard.innerHTML = `

            <h3>
                ${job.title}
            </h3>

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
                <strong>Status:</strong>
                ${application.status}
            </p>

            <p>
                <strong>Applied At:</strong>
                ${application.appliedAt || "N/A"}
            </p>

            <hr>

        `;


        container.appendChild(
            applicationCard
        );

    });

}