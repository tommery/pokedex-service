function openLoginModal() {
    document.getElementById("loginModal").style.display = "block";
}

function closeLoginModal() {
    document.getElementById("loginModal").style.display = "none";
}

document.addEventListener("DOMContentLoaded", function() {

    const registerForm = document.getElementById("loginForm");
    if (registerForm) {
        registerForm.addEventListener("submit", async function (event) {
            event.preventDefault();

			const form = event.target;  
			
			const email = form.elements["email"].value;
			const password = form.elements["password"].value;

            const response = await fetch("api/v1/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, password })
            });

            const message = await response.text();
			localStorage.setItem("jwt", message);
        });
    }
    closeLoginModal();
});
