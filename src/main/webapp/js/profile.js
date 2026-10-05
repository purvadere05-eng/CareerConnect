document.addEventListener("DOMContentLoaded", function () {

    loadProfile();

});


// ==========================================
// LOAD PROFILE
// ==========================================

function loadProfile() {

    console.log("Loading profile...");

    fetch("profile")

        .then(response => {

            console.log(
                "Profile response:",
                response.status
            );

            if (!response.ok) {

                throw new Error(
                    "Unable to load profile. Status: "
                    + response.status
                );
            }

            return response.json();
        })

        .then(student => {

            console.log(
                "Student data:",
                student
            );


            // =========================
            // PROFILE INFORMATION
            // =========================

            document.getElementById("fullName").textContent =
                student.fullName || "";

            document.getElementById("email").textContent =
                student.email || "";

            document.getElementById("phone").textContent =
                student.phone || "";

            document.getElementById("qualification").textContent =
                student.qualification || "";

            document.getElementById("skills").textContent =
                student.skills || "";


            // =========================
            // PROFILE PHOTO
            // =========================

            const profilePhoto =
                document.getElementById("profilePhoto");

            if (student.profilePhoto) {

                profilePhoto.src =
                    "profile-image?t="
                    + new Date().getTime();

            } else {

                profilePhoto.src =
                    "images/default-profile.png";
            }

        })

        .catch(error => {

            console.error(
                "Profile error:",
                error
            );

        });

}



// ==========================================
// UPLOAD PROFILE PHOTO
// ==========================================

function uploadPhoto() {

    const fileInput =
        document.getElementById("photoInput");

    const file =
        fileInput.files[0];


    // =========================
    // CHECK FILE
    // =========================

    if (!file) {

        alert(
            "Please select a photo first."
        );

        return;
    }


    // =========================
    // CHECK FILE SIZE
    // =========================

    if (file.size > 5 * 1024 * 1024) {

        alert(
            "Photo size must be less than 5 MB."
        );

        return;
    }


    // =========================
    // CHECK IMAGE TYPE
    // =========================

    if (!file.type.startsWith("image/")) {

        alert(
            "Please select an image file."
        );

        return;
    }


    // =========================
    // CREATE FORM DATA
    // =========================

    const formData =
        new FormData();


    // IMPORTANT:
    // This MUST match:
    // request.getPart("profilePhoto")

    formData.append(
        "profilePhoto",
        file
    );


    console.log(
        "Uploading photo..."
    );


    // =========================
    // SEND TO SERVLET
    // =========================

    fetch(
        "profile-photo",
        {
            method: "POST",
            body: formData
        }
    )

    .then(response => {

        console.log(
            "Upload response:",
            response.status
        );


        if (!response.ok) {

            throw new Error(
                "Photo upload failed. Status: "
                + response.status
            );
        }


        return response.json();

    })

    .then(data => {

        console.log(
            "Upload response:",
            data
        );


        alert(
            data.message
        );


        // =========================
        // DISPLAY NEW PHOTO
        // =========================

        document.getElementById(
            "profilePhoto"
        ).src =
            "profile-image?t="
            + new Date().getTime();


        // Clear file input

        fileInput.value = "";

    })

    .catch(error => {

        console.error(
            "Photo upload error:",
            error
        );


        alert(
            "Unable to upload profile photo."
        );

    });

}