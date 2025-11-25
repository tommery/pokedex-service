function openRegisterModal() {
    document.getElementById("registerModal").style.display = "block";
}

function closeRegisterModal() {
    document.getElementById("registerModal").style.display = "none";
}

document.addEventListener("DOMContentLoaded", function() {

    const registerForm = document.getElementById("registerForm");

    if (registerForm) {
        registerForm.addEventListener("submit", async function (event) {
            event.preventDefault();

            const form = event.target;  

            const email = form.elements["email"].value;
            const username = form.elements["nickname"].value;
            const password = form.elements["password"].value;

            const response = await fetch("./api/v1/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, nickname, password })
            });

            const message = await response.text();
			closeRegisterModal();
        });
    }
    
});

