document.addEventListener("DOMContentLoaded", function () {

    console.log("ADMIN JOBS JS LOADED");

    loadJobs();

    const jobForm = document.querySelector("#jobForm");

    if (jobForm) {
        jobForm.addEventListener("submit", saveJob);
    }

});


// =====================================================
// LOAD ALL JOBS
// =====================================================

function loadJobs() {

    fetch("admin/jobs")

        .then(response => {

            console.log("Load jobs status:", response.status);

            if (!response.ok) {
                throw new Error("Unable to load jobs");
            }

            return response.json();
        })

        .then(jobs => {

            console.log("Jobs received:", jobs);

            displayJobs(jobs);

        })

        .catch(error => {

            console.error("Load jobs error:", error);

            const container =
                document.querySelector("#jobsContainer");

            if (container) {
                container.textContent =
                    "Unable to load jobs.";
            }

        });
}


// =====================================================
// DISPLAY JOBS
// =====================================================

function displayJobs(jobs) {

    const container =
        document.querySelector("#jobsContainer");

    if (!container) {
        console.error("jobsContainer not found");
        return;
    }

    container.innerHTML = "";

    if (!jobs || jobs.length === 0) {

        container.innerHTML =
            "<p>No jobs available.</p>";

        return;
    }


    jobs.forEach(job => {

        console.log(
            "Displaying Job:",
            job.id,
            job.title
        );


        // =================================================
        // JOB CARD
        // =================================================

        const jobCard =
            document.createElement("div");

        jobCard.className = "job-card";


        // =================================================
        // JOB INFORMATION
        // =================================================

        const title =
            document.createElement("h3");

        title.textContent =
            job.title || "No title";


        const company =
            document.createElement("p");

        company.innerHTML =
            "<strong>Company:</strong> " +
            (job.company || "N/A");


        const location =
            document.createElement("p");

        location.innerHTML =
            "<strong>Location:</strong> " +
            (job.location || "N/A");


        const salary =
            document.createElement("p");

        salary.innerHTML =
            "<strong>Salary:</strong> " +
            (job.salary || "N/A");


        const skills =
            document.createElement("p");

        skills.innerHTML =
            "<strong>Skills:</strong> " +
            (job.skills || "N/A");


        const jobType =
            document.createElement("p");

        jobType.innerHTML =
            "<strong>Type:</strong> " +
            (job.jobType || "Not specified");


        const description =
            document.createElement("p");

        description.innerHTML =
            "<strong>Description:</strong> " +
            (job.description || "N/A");


        const status =
            document.createElement("p");

        status.innerHTML =
            "<strong>Status:</strong> " +
            (job.status || "OPEN");


        // =================================================
        // EDIT BUTTON
        // =================================================

        const editButton =
            document.createElement("button");

        editButton.type = "button";

        editButton.textContent = "Edit";

        editButton.style.display = "inline-block";

        editButton.style.margin = "5px";

        editButton.onclick = function () {

            editJob(job.id);

        };


        // =================================================
        // DELETE BUTTON
        // =================================================

        const deleteButton =
            document.createElement("button");

        deleteButton.type = "button";

        deleteButton.textContent = "Delete";

        deleteButton.style.display = "inline-block";

        deleteButton.style.margin = "5px";

        deleteButton.onclick = function () {

            deleteJob(job.id);

        };


		// =================================================
		// CLOSE JOB BUTTON
		// =================================================

		const closeButton =
		    document.createElement("button");

		closeButton.type = "button";
		closeButton.textContent = "Close Job";

		// Same style as Edit and Delete buttons
		closeButton.style.display = "inline-block";
		closeButton.style.visibility = "visible";
		closeButton.style.opacity = "1";

		closeButton.style.margin = "5px";
		closeButton.style.padding = "8px 15px";

		closeButton.style.backgroundColor = "#2463c5";
		closeButton.style.color = "white";

		closeButton.style.border = "none";
		closeButton.style.borderRadius = "6px";

		closeButton.style.cursor = "pointer";
		closeButton.style.fontSize = "18px";
		closeButton.style.fontWeight = "normal";

		console.log(
		    "Close button created for Job:",
		    job.id
		);

		closeButton.onclick = function () {
		    closeJob(job.id);
		};

        // =================================================
        // ADD EVERYTHING TO CARD
        // =================================================

        jobCard.appendChild(title);

        jobCard.appendChild(company);

        jobCard.appendChild(location);

        jobCard.appendChild(salary);

        jobCard.appendChild(skills);

        jobCard.appendChild(jobType);

        jobCard.appendChild(description);

        jobCard.appendChild(status);

        jobCard.appendChild(editButton);

        jobCard.appendChild(deleteButton);

        jobCard.appendChild(closeButton);


        // =================================================
        // HORIZONTAL LINE
        // =================================================

        const hr =
            document.createElement("hr");

        jobCard.appendChild(hr);


        // =================================================
        // ADD CARD TO CONTAINER
        // =================================================

        container.appendChild(jobCard);

    });

}


// =====================================================
// EDIT JOB
// =====================================================

function editJob(id) {

    console.log(
        "Edit button clicked. Job ID:",
        id
    );

    window.location.href =
        "edit-job.html?id=" + id;
}


// =====================================================
// SAVE NEW JOB
// =====================================================

function saveJob(event) {

    event.preventDefault();


    const job = {

        title:
            document.querySelector("#title").value,

        company:
            document.querySelector("#company").value,

        location:
            document.querySelector("#location").value,

        salary:
            document.querySelector("#salary").value,

        skills:
            document.querySelector("#skills").value,

        jobType:
            document.querySelector("#jobType").value,

        description:
            document.querySelector("#description").value

    };


    console.log(
        "Adding job:",
        job
    );


    fetch("admin/jobs", {

        method: "POST",

        headers: {

            "Content-Type":
                "application/json"

        },

        body:
            JSON.stringify(job)

    })

    .then(response => {

        console.log(
            "Add job status:",
            response.status
        );

        if (!response.ok) {

            throw new Error(
                "Unable to add job"
            );

        }

        return response.json();

    })

    .then(data => {

        console.log(
            "Add job response:",
            data
        );

        alert(data.message);

        cancelForm();

        loadJobs();

    })

    .catch(error => {

        console.error(
            "Add job error:",
            error
        );

        alert(
            "Unable to add job."
        );

    });

}


// =====================================================
// CLOSE JOB
// =====================================================

function closeJob(id) {

    console.log(
        "Closing job:",
        id
    );


    const confirmed =
        confirm(
            "Are you sure you want to close this job?"
        );


    if (!confirmed) {
        return;
    }


    fetch(
        "admin/jobs?action=CLOSE",
        {

            method: "PUT",

            headers: {

                "Content-Type":
                    "application/json"

            },

            body:
                JSON.stringify({
                    id: id
                })

        }
    )

    .then(response => {

        console.log(
            "Close job status:",
            response.status
        );


        if (!response.ok) {

            return response.text()
                .then(message => {

                    throw new Error(
                        message ||
                        "Unable to close job"
                    );

                });

        }


        return response.json();

    })

    .then(data => {

        console.log(
            "Close job response:",
            data
        );

        alert(data.message);

        loadJobs();

    })

    .catch(error => {

        console.error(
            "Close job error:",
            error
        );

        alert(
            "Unable to close job: " +
            error.message
        );

    });

}

// =====================================================
// DELETE JOB
// =====================================================

function deleteJob(id) {

    console.log("Deleting job ID:", id);

    const confirmed = confirm(
        "Are you sure you want to delete this job?"
    );

    if (!confirmed) {
        return;
    }

    fetch("admin/jobs?id=" + id, {
        method: "DELETE"
    })

    .then(response => {

        console.log(
            "Delete job status:",
            response.status
        );

        // Read response as text first
        return response.text().then(text => {

            console.log(
                "Delete server response:",
                text
            );

            let data;

            try {
                data = JSON.parse(text);
            } catch (e) {
                data = {
                    message: text || "Unknown server response"
                };
            }

            if (!response.ok) {

                throw new Error(
                    data.message ||
                    "Unable to delete job"
                );
            }

            return data;
        });
    })

    .then(data => {

        console.log(
            "Delete successful:",
            data
        );

        alert(data.message);

        // Reload jobs
        loadJobs();
    })

    .catch(error => {

        console.error(
            "Delete job error:",
            error
        );

        alert(
            "Delete failed: " +
            error.message
        );
    });
}


// =====================================================
// SHOW ADD JOB FORM
// =====================================================

function showAddForm() {

    console.log(
        "Add New Job clicked"
    );


    const form =
        document.querySelector("#jobForm");

    const container =
        document.querySelector("#jobFormContainer");

    const title =
        document.querySelector("#formTitle");


    if (form) {
        form.reset();
    }

    if (title) {
        title.textContent =
            "Add New Job";
    }

    if (container) {
        container.style.display =
            "block";
    }

}


// =====================================================
// CANCEL FORM
// =====================================================

function cancelForm() {

    const form =
        document.querySelector("#jobForm");

    const container =
        document.querySelector("#jobFormContainer");


    if (container) {

        container.style.display =
            "none";

    }


    if (form) {

        form.reset();

    }

}