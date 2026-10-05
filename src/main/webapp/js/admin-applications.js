document.addEventListener("DOMContentLoaded", function () {

    console.log("Admin Applications page loaded.");

    loadApplications();

});



// ======================================
// LOAD APPLICATIONS
// ======================================

function loadApplications() {

    console.log("Loading admin applications...");

    fetch("admin/applications")

        .then(response => {

            console.log(
                "Applications response status:",
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
                "Applications:",
                applications
            );

            displayApplications(applications);

        })

        .catch(error => {

            console.error(
                "Load applications error:",
                error
            );

            document.getElementById(
                "applicationsContainer"
            ).textContent =
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
		
		console.log("RESUME FILE NAME:", application.resumeFileName);

        const card =
            document.createElement("div");

        card.className =
            "application-card";


        // ======================================
        // RESUME SECTION
        // ======================================

        let resumeHTML = "Not uploaded";


        if (
            application.resumeFileName &&
            application.resumeFileName.trim() !== ""
        ) {

            resumeHTML = `
                <a
                    href="admin/resume?file=${encodeURIComponent(application.resumeFileName)}"
                    target="_blank"
                    class="resume-link"
                >
                    📄 View Resume
                </a>
            `;

        }


        // ======================================
        // APPLICATION CARD
        // ======================================

        card.innerHTML = `

            <h3>
                ${application.jobTitle}
            </h3>


            <p>
                <strong>Student:</strong>
                ${application.studentName}
            </p>


            <p>
                <strong>Email:</strong>
                ${application.studentEmail}
            </p>


            <p>
                <strong>Company:</strong>
                ${application.company}
            </p>


            <p>
                <strong>Location:</strong>
                ${application.location}
            </p>


            <p>
                <strong>Salary:</strong>
                ${application.salary}
            </p>


            <p>
                <strong>Skills:</strong>
                ${application.skills}
            </p>


            <p>
                <strong>Job Type:</strong>
                ${application.jobType || "Not specified"}
            </p>


            <p>
                <strong>Resume:</strong>
                ${resumeHTML}
            </p>


            <p>
                <strong>Applied At:</strong>
                ${formatDate(application.appliedAt)}
            </p>


            <p>
                <strong>Status:</strong>
                ${application.status}
            </p>


            <label>
                Update Status:
            </label>


            <select
                onchange="updateStatus(
                    ${application.id},
                    this.value
                )"
            >

                <option
                    value="PENDING"
                    ${application.status === "PENDING" ? "selected" : ""}
                >
                    PENDING
                </option>


                <option
                    value="SHORTLISTED"
                    ${application.status === "SHORTLISTED" ? "selected" : ""}
                >
                    SHORTLISTED
                </option>


                <option
                    value="SELECTED"
                    ${application.status === "SELECTED" ? "selected" : ""}
                >
                    SELECTED
                </option>


                <option
                    value="REJECTED"
                    ${application.status === "REJECTED" ? "selected" : ""}
                >
                    REJECTED
                </option>

            </select>


            <hr>

        `;


        container.appendChild(card);

    });

}



// ======================================
// UPDATE APPLICATION STATUS
// ======================================

function updateStatus(applicationId, status) {

    console.log(
        "Updating application:",
        applicationId,
        "Status:",
        status
    );


    fetch(
        "admin/applications?id="
        + applicationId
        + "&status="
        + encodeURIComponent(status),

        {
            method: "PUT"
        }
    )

    .then(response => {

        console.log(
            "Update response status:",
            response.status
        );


        if (!response.ok) {

            throw new Error(
                "Unable to update status. Status: "
                + response.status
            );

        }


        return response.json();

    })


    .then(data => {

        console.log(
            "Update response:",
            data
        );


        alert(data.message);


        loadApplications();

    })


    .catch(error => {

        console.error(
            "Update status error:",
            error
        );


        alert(
            "Unable to update application status."
        );

    });

}



// ======================================
// FORMAT APPLIED DATE
// ======================================

function formatDate(dateTime) {

    if (!dateTime) {

        return "Not available";

    }


    const date =
        new Date(dateTime);


    return date.toLocaleString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            hour12: true
        }
    );

}