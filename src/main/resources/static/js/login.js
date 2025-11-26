function openLoginModal() {
    document.getElementById("loginModal").style.display = "block";
}

function closeLoginModal() {
    document.getElementById("loginModal").style.display = "none";
}

function showToast(message, isError = false) {
    const toast = document.getElementById("toast");
    toast.textContent = message;

    toast.classList.remove("hidden");
    toast.classList.add("show");

    if (isError) toast.classList.add("error");
    else toast.classList.remove("error");

    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => toast.classList.add("hidden"), 400);
    }, 3000);
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
			closeLoginModal();
			
			if (response.ok) {
			    // save token only if login succeeded
			    localStorage.setItem("jwt", message);
			    showToast("Login successful!");
			} else {
			    showToast("Login failed!", true);
			}
        });
    }
    
});
