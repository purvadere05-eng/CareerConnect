document.addEventListener("DOMContentLoaded", function () {

    loadProfileForEdit();

    document.getElementById("updateProfileForm")
        .addEventListener("submit", updateProfile);

});


// Load existing profile data
function loadProfileForEdit() {

    fetch("profile", {
        method: "GET"
    })

    .then(response => {

        console.log("Profile status:", response.status);

        if (!response.ok) {
            throw new Error("Unable to load profile");
        }

        return response.json();

    })

    .then(student => {

        console.log("Profile for edit:", student);

        document.getElementById("editFullName").value =
            student.fullName || "";

        document.getElementById("editEmail").value =
            student.email || "";

        document.getElementById("editPhone").value =
            student.phone || "";

        document.getElementById("editQualification").value =
            student.qualification || "";

        document.getElementById("editSkills").value =
            student.skills || "";

    })

    .catch(error => {

        console.error("Load error:", error);

        alert("Unable to load profile");

    });

}


// Update profile
function updateProfile(event) {

    event.preventDefault();

    const updatedStudent = {

        fullName:
            document.getElementById("editFullName").value,

        email:
            document.getElementById("editEmail").value,

        phone:
            document.getElementById("editPhone").value,

        qualification:
            document.getElementById("editQualification").value,

        skills:
            document.getElementById("editSkills").value

    };


    console.log("Sending update:", updatedStudent);


    fetch("profile", {

        method: "PUT",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(updatedStudent)

    })

    .then(response => {

        console.log("Update status:", response.status);

        if (!response.ok) {

            throw new Error(
                "Update failed: " + response.status
            );

        }

        return response.json();

    })

    .then(data => {

        console.log("Update response:", data);

        alert(data.message);

        // Go back to profile page
        window.location.href = "profile.html";

    })

    .catch(error => {

        console.error("Update error:", error);

        alert("Profile update failed");

    });

}