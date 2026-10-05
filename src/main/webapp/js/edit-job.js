document.addEventListener("DOMContentLoaded", function () {

    loadJob();

    document
        .getElementById("editJobForm")
        .addEventListener("submit", updateJob);

});


// ======================================
// LOAD JOB
// ======================================

function loadJob() {

    const params =
        new URLSearchParams(window.location.search);

    const id = params.get("id");

    console.log("Job ID from URL:", id);


    if (!id) {

        alert("Job ID is missing.");

        window.location.href = "admin-jobs.html";

        return;
    }


    fetch("admin/jobs?id=" + id)

        .then(response => {

            console.log(
                "Load job status:",
                response.status
            );


            if (!response.ok) {

                throw new Error(
                    "Unable to load job"
                );

            }

            return response.json();

        })


        .then(job => {

            console.log(
                "Job loaded:",
                job
            );


            document.getElementById("jobId").value =
                job.id;

            document.getElementById("title").value =
                job.title || "";

            document.getElementById("company").value =
                job.company || "";

            document.getElementById("location").value =
                job.location || "";

            document.getElementById("salary").value =
                job.salary || "";

            document.getElementById("skills").value =
                job.skills || "";

            document.getElementById("jobType").value =
                job.jobType || "Full Time";

            document.getElementById("description").value =
                job.description || "";

        })


        .catch(error => {

            console.error(
                "Load job error:",
                error
            );

            alert(
                "Unable to load job."
            );

        });

}


// ======================================
// UPDATE JOB
// ======================================

function updateJob(event) {

    event.preventDefault();


    const id =
        document.getElementById("jobId").value;


    const updatedJob = {

        id: parseInt(id),

        title:
            document.getElementById("title").value,

        company:
            document.getElementById("company").value,

        location:
            document.getElementById("location").value,

        salary:
            document.getElementById("salary").value,

        skills:
            document.getElementById("skills").value,

        jobType:
            document.getElementById("jobType").value,

        description:
            document.getElementById("description").value

    };


    console.log(
        "Updating job:",
        updatedJob
    );


    fetch("admin/jobs", {

        method: "PUT",

        headers: {

            "Content-Type":
                "application/json"

        },

        body:
            JSON.stringify(updatedJob)

    })


    .then(response => {

        console.log(
            "Update status:",
            response.status
        );


        if (!response.ok) {

            throw new Error(
                "Update failed"
            );

        }

        return response.json();

    })


    .then(data => {

        console.log(
            "Update response:",
            data
        );


        alert(
            data.message
        );


        // Go back to Manage Jobs

        window.location.href =
            "admin-jobs.html";

    })


    .catch(error => {

        console.error(
            "Update error:",
            error
        );


        alert(
            "Unable to update job."
        );

    });

}


// ======================================
// CANCEL
// ======================================

function goBack() {

    window.location.href =
        "admin-jobs.html";

}